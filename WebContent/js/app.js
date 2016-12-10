angular.module("Fish", ["ui.bootstrap"])
	.controller("FishStatsController", function($scope, $http) {
		// initialize table settings
		$scope.order = 'weight';
		$scope.reverse = true;
		// $scope.includeBaitedCheck = true; // AngularJS breaks 'checked="checked"' so I set it manually
		// $scope.isUnbaited = function(fish) {
			// return $scope.includeBaitedCheck || !fish.isBaited;
		// };
		
		var date = new Date();
		// $scope.year = date.getFullYear();
		// $scope.month = getMonthMM();
		var ajaxParams = "length=100"; // initialize it to length 100 so it doesn't explode at the start
		
		var refreshTimer; // declare auto-refresh timer (gets toggled in updateRefreshCondition())
		
		$scope.changeFile = function() {
			// disable auto-refresh
			clearInterval(refreshTimer);
			var year = "";
			var month = "";
			if ($scope.fishfile.match(/\d{6}/)) { // all-time selected
				year = $scope.fishfile.substring(0, 4);
				month = $scope.fishfile.substring(4, 6);
				ajaxParams = "month=" + year + month;
			} else {
				ajaxParams = "length=100";
			}
			$scope.refreshFish();
			
			// turn on auto-refresh only if current month is displayed
			if (year == date.getFullYear() && month == getMonthMM()) {
				$scope.updateRefreshCondition();
			}
		}
		
		$scope.refreshFish = function() {
			$scope.loading = true;
			// $http.get("data/" + $scope.year + $scope.month + "-fish.json")
			$http.get("./fish.php?q=fish&" + ajaxParams)
				.success(function(data) {
					$scope.loading = false;
					$scope.fishes = data;
				});
			//console.log("fish refreshed");
			//console.log($scope.fishes);
		};
		
		$scope.updateRefreshCondition = function() {
			if ($scope.autoRefreshCheck) {
				refreshTimer = window.setInterval($scope.refreshFish, 5 * 1000); // reload fish every 5 sec
			} else {
				clearInterval(refreshTimer);
			}
		}
		
		$scope.toggleOrder = function(col) {
			if ($scope.order === col) {
				$scope.reverse = !$scope.reverse;
			} else {
				$scope.order = col;
				$scope.reverse = false;
			}
		};
		
		/*
			returns two-digit current month
		*/
		function getMonthMM() {
			var month = date.getMonth() + 1;
			if (month < 10) {
				month = "0" + month;
			}
			return month;
		}
		
		//pagination stuff
		// $scope.filteredFishes = [];
		// $scope.currentPage = 1;
		// $scope.numPerPage = 10;
		// $scope.maxSize = 5;
		// $scope.changePage = function() {
			// var first = ($scope.currentPage - 1) * $scope.numPerPage;
			// var last = first + $scope.numPerPage;
			// $scope.filteredFishes = $scope.fishes.slice(first, last);
			// console.log("page changed: ", $scope.currentPage);
		// };
	});