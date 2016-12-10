<?php
error_reporting(E_ALL);

$FISH_FILE = "./data/201609-fish.json";
// $FISH_PATH = "./data";

// chdir($FISH_PATH); // takes us to the directory of THE FISH CONGLOMERATE

$fish = array(); // THE FISH CONGLOMERATE
$fish_files = glob("./data/*-fish.json");
foreach ($fish_files as $file) {
	$file_json = json_decode(file_get_contents($file), true);
	$fish = array_merge($fish, $file_json);
}

$fish = array_sort($fish, "weight", SORT_DESC);

// if (file_exists($FISH_FILE)) {
	// $fish_text = file_get_contents($FISH_FILE);
// } else {
	// $fish_text = "";
// }
// $fish = json_decode($fish_text, true); // the array of ALL the fish

if (!isset($_GET["q"])) { // "q" parameter is required
	header($_SERVER["SERVER_PROTOCOL"] . " 400 Invalid Request");
	die($_SERVER["SERVER_PROTOCOL"] . " 400 Invalid Request: missing required parameter 'q'");
}

$q = $_GET["q"];

switch ($q) {
	case "average":
		header("Content type: text/plain");
		echo get_average($fish);
		break;
	case "count":
		header("Content type: text/plain");
		echo count($fish);
		break;
	case "fish":
		$start = 0;
		$length = count($fish);
		$month = "";
		if (isset($_GET["start"])) {
			$start = $_GET["start"];
		}
		if (isset($_GET["length"])) {
			$length = $_GET["length"];
		}
		if (isset($_GET["month"])) {
			$month = $_GET["month"];
		}
		echo get_fish_json($fish, $start, $length, $month);
		break;
}


/*
	returns average of $fish
*/
function get_average($fish) {
	$sum = 0;
	$n = count($fish);
	foreach ($fish as $f) {
		$sum += (float) $f["weight"];
	}
	return round($sum / $n, 4, PHP_ROUND_HALF_DOWN);
}


/*
	returns an array of $fish caught during $month (yyyymm) starting from the $start index, 
	counting up to $length as JSON text
*/
function get_fish_json($fish, $start, $length, $month) {
	if ($month === "") {
		header("Content type: application/json");
		return json_encode(array_slice($fish, $start, $length));
	} else {
		// in this case we don't actually need the $fish because we're getting it straight from the file
		$filename = "./data/" . $month . "-fish.json";
		if (!file_exists($filename)) {
			header($_SERVER["SERVER_PROTOCOL"] . " 400 Invalid Request");
			die($_SERVER["SERVER_PROTOCOL"] . " 400 Invalid Request: parameter 'month' not valid");
		}
		header("Content type: application/json");
		$result = json_decode(file_get_contents($filename));
		return json_encode(array_slice($result, $start, $length));
	}
}

/*
	sorts an array by a specific key
	
	returns the sorted array
*/
function array_sort($array, $on, $order=SORT_ASC) {
    $new_array = array();
    $sortable_array = array();

    if (count($array) > 0) {
        foreach ($array as $k => $v) {
            if (is_array($v)) {
                foreach ($v as $k2 => $v2) {
                    if ($k2 == $on) {
                        $sortable_array[$k] = $v2;
                    }
                }
            } else {
                $sortable_array[$k] = $v;
            }
        }

        switch ($order) {
            case SORT_ASC:
                asort($sortable_array);
            break;
            case SORT_DESC:
                arsort($sortable_array);
            break;
        }

        foreach ($sortable_array as $k => $v) {
            $new_array[$k] = $array[$k];
        }
    }

    return $new_array;
}






// $start = 0;
// $limit = count($fish);

// $start = $_GET["start"];
// if ($start > count($fish) - 1) {
	// header($_SERVER["SERVER_PROTOCOL"] . " 400 Invalid Request");
	// die($_SERVER["SERVER_PROTOCOL"] . " 400 Invalid Request: parameter '$start' too large.");
// }
// $limit = $_GET["limit"];

?>