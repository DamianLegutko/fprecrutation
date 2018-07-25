var fprecrutation = angular.module('fprecrutation', ['ngRoute', 'ngCookies', 'appControllers']);

fprecrutation.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/home', {
            templateUrl: 'home/home.html',
            controller: 'HomeCtrl'
        }).
        when('/error', {
            templateUrl: 'error/error.html',
            controller: 'HomeCtrl'
        }).
        when('/', {
            templateUrl: 'login/login.html',
            controller: 'LoginCtrl'
        }).
        when('/register', {
            templateUrl: 'register/register.html',
            controller: 'RegisterCtrl'
        }).
        otherwise({
            redirectTo: '/'
        });
    }]);

var appControllers = angular.module('appControllers', []);

appControllers.controller('LoginCtrl', ['$scope', '$routeParams', '$http', '$cookies',
    function($scope, $routeParams, $http, $cookies) {
        $scope.user = {
            username: '',
            password: ''
        };
     //   $http.defaults.headers.post['X-CSRFToken'] = $cookies['csrftoken'];
        $scope.login = function login(user) {
            $http.post('/api/user/signin', $scope.user)
                .then(function success(response) {
                    redirectTo: '/home';
                }).catch(function error(data, status, headers, config) {
                    alert("Exception details: " + JSON.stringify({data: data}));
            });
        }
    }
]);

appControllers.controller('RegisterCtrl', ['$scope', '$routeParams', '$http', '$cookies',
    function($scope, $routeParams, $http, $cookies) {
        $scope.user = {
            username: '',
            password: ''
        };
      //  $http.defaults.headers.post['X-CSRFToken'] = $cookies['csrftoken'];
        $scope.register = function register(user) {
            var response = $http.get('/registration/user?email=' + $scope.user.username + "&name=" + $scope.user.name + "&surname=" + $scope.user.surname + "&password=" + $scope.user.password);
            response.success(function (data, status, headers, config) {

            });
            response.error(function (data, status, headers, config) {
                alert("Exception details: " + JSON.stringify({data: data}));
            });
        }
    }
]);