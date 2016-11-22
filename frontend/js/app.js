(function() {
  "use strict";

  var knoxSolrSearch = angular.module('knoxSolrSearch', [
    'ngResource'
  ]);

  knoxSolrSearch
    .controller('SearchController', ['$http', function ($http) {
        var vm = this;
        vm.q = "";

      // var url = "http://hdp24sandbox.ukwest.cloudapp.azure.com:8983/solr/KnoxIntegrationConfig_shard1_replica1/select?q=*%3A*&wt=json&indent=true";
      var url = "js/data.json";

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
        $route.reload();
      };

    }]);

})();
