angular.module('assetController', [])

    .controller('assetsController', ['$scope', '$rootScope', '$http', 'WebsocketService',
        function($scope, $rootScope, $http, WebsocketService) {
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
                            asset.stockPrice = parseFloat(stockPrice).toFixed(4);
                            asset.value = parseFloat(stockPrice * asset.stockAmount).toFixed(4);
                        });
                        $rootScope.user.money = parseFloat(response.data.money).toFixed(4);
                    }).catch(function errorCallback(response) {
                        $scope.errorMessage = 'Problem with getting user assets';
                    })
            }
        }
    ]);