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

      // var url = "http://hdp24sandbox.ukwest.cloudapp.azure.com:8983/solr/KnoxIntegrationConfig_shard1_replica1/select?q=*%3A*&wt=json&indent=true";
      //var url = "js/data.json";
      var url = "/search";
      $http.get(url)
      .then(function (response) {
        vm.products = response.data.response.docs;
      }, function (response) {
        console.log('Error: ' + response.error);
      });

      vm.searchSolr = function() {
        vm.q = vm.query;
      };

      vm.refresh = function() {
        $window.location.reload();
      };

    }]);

})();
