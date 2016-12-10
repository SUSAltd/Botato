<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="css/bootstrap.min.css" />
	<link rel="stylesheet" href="css/main.css" />
	
	<script src="js/lib/angular.min.js"></script>
	<script src="js/lib/ui-bootstrap-tpls-0.13.0.min.js"></script>
	<script src="js/app.js"></script>
	<title>
		!fish
	</title>
</head>
<body ng-app="Fish" ng-controller="FishStatsController" ng-init="refreshFish()">
	<div class="container">
		<h1>Botato</h1>
		<p>
			Here you can view the top 100 fish of all time or view fish from any individual month.
		</p>
		<div class="form-group form-inline">
			<input type="text" class="search" placeholder="Search these fish" ng-model="search" />
			<select ng-change="changeFile()" ng-model="fishfile">
				<option value="" ng-selected="true">All-time</option>
				
				<?php
				// creates the dropdown menu for selecting months
				chdir("data");
				$fishfiles = array_reverse(glob("*-fish.json"));
				// $flag = true; // only put "selected" on the first option
				foreach ($fishfiles as $filename) {
					$date = substr($filename, 0, 6); // yyyymm
					$year = substr($date, 0, 4);
					$month = substr($date, 4);
					?>
					<option value="<?= $date ?>"><?= $year . "-" . $month ?></option>
					<?php
					// $flag = false; // only put "selected" on the first option
				}
				?>
			</select>
			<button class="fish-refresh-button" ng-click="refreshFish()">Refresh</button>
			<img src="img/ajax-spinner-sm.gif" alt="loading" class="loading" ng-if="loading == true" />
			<br />
			<label><input type="checkbox" checked="checked" ng-change="updateRefreshCondition()" ng-model="autoRefreshCheck" /> refresh automatically</label>
			<!-- <br />
			<label><input type="checkbox" checked="checked" ng-model="includeBaitedCheck" /> include baited fish</label> -->
		</div>
		
		<!-- displays if there are 0 fish -->
		<div class="error" ng-if="!fishes.length">No fish have been caught yet!</div>
		
		<!-- table of stats is displayed if there are fish -->
		<div class="row">
			<div class="col-md-12">
				<table class="table table-bordered table-condensed table-responsive" ng-if="fishes.length">
					<thead>
						<tr>
							<th>#</th>
							<th class="sortable catcher" ng-class="order == 'catcher' ? 'active' : ''" ng-click="toggleOrder('catcher')">Caught by</th>
							<th class="sortable name" ng-class="order == 'name' ? 'active' : ''" ng-click="toggleOrder('name')">Fish</th>
							<th class="sortable weight" ng-class="order == 'weight' ? 'active' : ''" ng-click="toggleOrder('weight')">Weight</th>
							<th class="sortable optional bait-name" ng-class="order == 'baitName' ? 'active' : ''" ng-click="toggleOrder('baitValue')">Bait used</th>
							<!-- <th class="sortable optional bait-value" ng-class="order == 'baitValue' ? 'active' : ''" ng-click="toggleOrder('baitValue')">Bait value</th> -->
							<th class="sortable optional date-caught" ng-class="order == 'dateCaught' ? 'active' : ''" ng-click="toggleOrder('dateCaught')">Date caught</th>
						</tr>
					</thead>
					<tr ng-repeat="fish in fishes | orderBy:order:reverse | filter:search | filter:isUnbaited">
						<td>{{$index + 1}}</td>
						<td>{{fish.catcher}}</td>
						<td>{{fish.name}}</td>
						<td>{{fish.weight}}</td>
						<td class="optional">{{fish.baitName}} ({{fish.baitValue}})</td>
						<!-- <td class="optional">{{fish.baitValue}}</td> -->
						<td class="optional">{{fish.dateCaught}}</td>
					</tr>
				</table>
			</div>
			<!-- <div class="col-md-3">
				<h2>Monthly statistics</h2>
			</div> -->
		</div>
		
<!-- 		<pagination 
			ng-change="changePage()"
			ng-model="currentPage"
			total-items="fishes.length"
			max-size="maxSize"
			boundary-links="true">
		</pagination> -->
	</div>
</body>
</html>