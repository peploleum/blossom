var mission = [];
mission.module = angular.module('blossom.mission', ['blossom.misc','blossom.form','blossom.table']);


mission.module.controller('MissionCtrl', function($scope, $http,$modal, uuidFactory,TableFactory, PopFormFactory,TableGetFactory)
{
	var navbarul = d3.selectAll('ul#navbarul>li');
	navbarul.attr("class", null);
	d3.select('#missionNavItem').attr("class", "active");
	$scope.tablerows = [];
	
	$scope.onRefClicked = function($event) {
		console.log("ref clicked " + $event);
		PopFormFactory.call($event.currentTarget.childNodes[0].data);
	}
	
	$scope.addMission = function()
	{
		console.log("new mission");
		newMission = {
				"id" : uuidFactory.generateUUID(),
				"name" : $scope.mission
			};
		$http.post('./rest/mission/addMission', newMission).success(function(data, status, headers, config) {
			console.log("success");
			onSuccess(data, status, headers, config);
		}).error(function(data, status, headers, config) {
			onError(data, status, headers, config, 'Add mission failed');
		});
	}
	
	onSuccess = function(data, status, headers, config) {
		console.log("success " + status + " " + data + " " + config);
		$scope.serviceError = false;
		$scope.serviceSuccess = true;
		$scope.errorMessage = '';
		blossomtable.loadTable($scope,TableGetFactory);
	}
	onError = function(data, status, headers, config, message) {
		console.log("error " + status + " " + data + " " + config);
		$scope.serviceError = true;
		$scope.errorMessage = message;
		$scope.serviceSuccess = false;
	}
	blossomtable.loadTable($scope,TableGetFactory);
})
