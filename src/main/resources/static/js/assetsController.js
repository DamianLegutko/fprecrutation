angular.module('assetController', [])

    .controller('assetsController', ['$scope', '$rootScope', '$http', 'WebsocketService', '$log',
        function($scope, $rootScope, $http, WebsocketService, $log) {
            $scope.errorMessage = '';
            userAssets();

            $scope.buy = function buy(ev, companyCode, stockPrice, stockAmount) {
                var asset = {
                    companyCode: companyCode,
                    stockAmount: stockAmount,
                    stockPrice: stockPrice,
                    userName: $rootScope.username
                };
                $http.post('/api/asset/buy', asset)
                    .then(function successCallback(response) {
                        $rootScope.shares = response.data;
                        userAssets();
                    }).catch(function errorCallback(response) {
                        $scope.errorMessage = response.data.message;
                    })
            };

            $scope.sell = function sell(ev, companyCode) {
                var asset = {
                    companyCode: companyCode,
                    stockAmount: $rootScope.stockPricesMap.get(companyCode).amount,
                    stockPrice: $rootScope.stockPricesMap.get(companyCode).price,
                    userName: $rootScope.username
                };
                $http.post('/api/asset/sell', asset)
                    .then(function successCallback(response) {
                        $rootScope.shares = response.data;
                        userAssets();
                    }).catch(function errorCallback(response) {
                        $scope.errorMessage = response.data.message;
                    })
            };

            function userAssets() {
                $rootScope.user = {
                    assetWallet: '',
                    money: ''
                };
                $http.get('/api/asset/get/userAssets/' + $rootScope.username)
                    .then(function successCallback(response) {
                        $rootScope.user.assetWallet = response.data.assetWallet;
                        angular.forEach($rootScope.user.assetWallet, function(asset) {
                            var stockPrice = $rootScope.stockPricesMap.get(asset.companyCode).price;
                            asset.stockPrice = stockPrice;
                            asset.value = stockPrice * asset.stockAmount;
                        });
                        $rootScope.user.money = response.data.money;
                    }).catch(function errorCallback(response) {
                        $scope.errorMessage = 'err'; //response.data.message;
                    })
            }
        }
    ]);