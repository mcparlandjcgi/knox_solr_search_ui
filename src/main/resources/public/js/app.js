(function() {
  "use strict";

  var knoxSolrSearch = angular.module('knoxSolrSearch', [
    'ngRoute'
  ]);

  knoxSolrSearch
    .config(function ($routeProvider) {
      $routeProvider.when('/', {
        templateUrl: '#',
        controller: 'SearchController',
        controllerAs: 'vm'
      });
    });

  knoxSolrSearch
    .controller('SearchController', ['$http', '$route', '$window', function ($http, $route, $window) {
        var vm = this;
        vm.q = "";
        
      vm.searchSolr = function() {
        vm.q = vm.query;
        vm.doQuery();
      };

      vm.refresh = function() {
        $window.location.reload();
      };
      
      vm.doQuery = function() {
          var url = "/search?solrQuery=" + vm.query;
          $http.get(url)
          .then(function (response) {
            vm.products = response.data.response.docs;
          }, function (response) {
            console.log('Error: ' + response.error);
          });
      };

    }]);

})();
