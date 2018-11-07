<?php

function getPaddedVersion($version) {
  $version_exploded = explode(".", $version);
  $version_padded = "";
  foreach ($version_exploded as $version_part) {
      if (strlen($version_padded) > 0)
          $version_padded .= ".";
      $version_padded .= str_pad($version_part, 4, "0", STR_PAD_LEFT);
  }
  return $version_padded;
}

// Create an array with the versions to display.
$versions = array();
$path = '.'; // '.' for current
foreach (new DirectoryIterator($path) as $file) {
  // Don't show the dot folder.
  if ($file->isDot()) continue;
	
  if ($file->isDir()) {
    $version = $file->getFilename();
    // Add the version folder to the array of versions.
    $versions[getPaddedVersion($version)] = $version;
  }
}

// Reverse sort the version (so latest is at the top).
krsort($versions);

?>
<!DOCTYPE html>
<html lang="en" class="no-js">
  <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <meta http-equiv="x-ua-compatible" content="ie=edge">
	  <link rel="shortcut icon" href="./latest/img/favicon.ico">
	  <title>CrossGenerate Documentation</title>
      <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
      <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,400i,700|Roboto+Mono">
      <style>body,input{font-family:"Roboto","Helvetica Neue",Helvetica,Arial,sans-serif}code,kbd,pre{font-family:"Roboto Mono","Courier New",Courier,monospace}</style>
      <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
  </head>
  <body>
    <h1>CrossGenerate Documentation</h1>
    The documentation of CrossGenerate can be found here.<br/>
    <br/>
    The versions of CrossGenerate are listed below. Click on a version to see its documentation.<br/>

    <ul>
    <?php
      foreach ($versions as $version) {
    ?>
      <li><a href="./<?=$version?>"><?=$version?></a></li>
    <?php
      }
    ?>
    </ul>
  </body>
</html>