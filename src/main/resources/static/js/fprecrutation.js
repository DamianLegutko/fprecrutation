var fprecrutation = angular.module('fprecrutation', ['ngRoute', 'authenticationController', 'assetController']);

fprecrutation.service('WebsocketService', function($rootScope) {
    var service = this;
    service.ws = new WebSocket("ws://webtask.future-processing.com:8068/ws/stocks");

    service.ws.onmessage = function(message) {
        $rootScope.stockPrices = [];
        $rootScope.stockPricesMap = new Map();

        var Items = JSON.parse(message.data).Items;

        angular.forEach(Items, function(value) {
            $rootScope.stockPrices.push(value);
            var stockInfo = {
                price: value.Price,
                amount: value.Unit
            };
            $rootScope.stockPricesMap.set(value.Code, stockInfo);
        });
        $rootScope.$apply();
    };

    return service;
});

fprecrutation.directive('ngConfirmClick', [
    function(){
        return {
            link: function (scope, element, attr) {
                var msg = attr.ngConfirmClick || "Are you sure?";
                var clickAction = attr.confirmedClick;
                element.bind('click',function (event) {
                    if ( window.confirm(msg) ) {
                        scope.$eval(clickAction)
                    }
                });
            }
        };
    }]);

fprecrutation.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/stocks', {
            templateUrl: 'stocks.html',
            controller: 'assetsController'
        }).
        when('/', {
            templateUrl: 'login.html',
            controller: 'loginController'
        }).
        when('/registration', {
            templateUrl: 'registration.html',
            controller: 'registrationController'
        }).
        when('/logout', {
            templateUrl: 'login.html',
            controller: 'logoutController'
        }).
        otherwise({
            redirectTo: '/'
        });
    }]);



