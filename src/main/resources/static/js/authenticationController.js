angular.module('authenticationController', [])

    .controller('loginController', ['$scope', '$rootScope', '$location', '$http', '$log',
        function($scope, $rootScope, $location, $http, $log) {
            $scope.user = {
                username: '',
                password: ''
            };
            $scope.errorMessage = '';
            $scope.login = function login(user) {
                $http.post('/api/user/signin', user)
                    .then(function successCallback(response) {
                        $log.info(response);
                        $rootScope.username = user.username;
                        $location.path("/stocks");
                    }).catch(function errorCallback(response) {
                        $scope.errorMessage = response.data.message;
                    })
            }
        }
    ])

    .controller('logoutController', ['$scope', '$rootScope', '$location', '$http', '$log',
        function($scope, $rootScope, $location, $http, $log) {
            $scope.errorMessage = '';
            $http.get('/api/user/logout')
                .then(function successCallback(response) {
                    $log.info(response);
                    $rootScope.stockPrices = [];
                    $rootScope.stockPricesMap = new Map();
                    $rootScope.username = '';
                    $rootScope.apply();
                    $location.path("/");
                }).catch(function errorCallback(response) {
                    $scope.errorMessage = response.data.message;
            })
        }
    ])

    .controller('registrationController', ['$scope', '$location', '$http',
        function($scope, $location, $http) {
            $scope.user = {
                username: '',
                password: '',
                confirmPassword: '',
                money: ''
            };
            $scope.errorMessage = '';
            $scope.registration = function registration(user) {
                $http.post('/api/user/registration', user)
                    .then(function successCallback(response) {
                        $location.path("/");
                    }).catch(function errorCallback(response) {
                        $scope.errorMessage = response.data.message;
                    })
            }
        }
    ]);