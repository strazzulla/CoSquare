<!DOCTYPE HTML>
<h1>LifeBand Data analysis</h1>

<?php    
    try {
        $dsn = 'mysql:dbname=redblue;host=sql5.lri.fr';
        $user = 'lfaucon';
        $password = 'K4redblueSQL';
        $dbh = new PDO($dsn, $user, $password, array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"));
        $dbh->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
    } catch (PDOException $e) {
        echo 'Connexion échouée : '.$e->getMessage().PHP_EOL;
    }


    $i = 0;
    $now = time();
    $hour = 3600;
    $duration = 75;
    $users = array();
    $data = array();
    $datalocation = array();
    $datanudges = array();
    $datalike = array();
    $names= array();
    $nudgestimeline= array();
    $locationtimeline= array();
    $liketimeline= array();
    
    
    $query_user = "SELECT * FROM users";
    $sth_user = $dbh->prepare($query_user);
    if (!$sth_user->execute()) exit(0);


    while ($ligne = $sth_user->fetch(PDO::FETCH_ASSOC)) {
        $i++;
        $users[$ligne['regid']] = $i;
        $names[$i] = $ligne['name'];
        $data[$users[$ligne['regid']]] = 0;
        $datalocation[$users[$ligne['regid']]] = 0;
        $datanudges[$users[$ligne['regid']]] = 0;
        $datalike[$users[$ligne['regid']]] = 0;

    }

    echo '<p>There are '.$i.' users </p>'.PHP_EOL;

    for($j = 1; $j <= $i; $j++){
        for($k = 0; $k <= $duration; $k++){
            $locationtimeline[$j][$k]=0;
            $nudgestimeline[$j][$k]=0;
            $liketimeline[$j][$k]=0;
        }
    }
    
    $query_data = "SELECT * FROM data";
    $sth_data = $dbh->prepare($query_data);
    if (!$sth_data->execute()) exit(0);

    while ($ligne = $sth_data->fetch(PDO::FETCH_ASSOC)) {
        $data[$users[$ligne['regid']]]++;
        if($ligne['type']=='locationtoken'){
            $locationtimeline[$users[$ligne['regid']]][($now-$ligne['time']/1000)/$hour]++;
            $datalocation[$users[$ligne['regid']]]++;
        }
        if($ligne['type']=='colortoken'){
            $nudgestimeline[$users[$ligne['regid']]][($now-$ligne['time']/1000)/$hour]++;
            $datanudges[$users[$ligne['regid']]]++;
        }
        if($ligne['type']=='like'){
            $liketimeline[$users[$ligne['regid']]][($now-$ligne['time']/1000)/$hour]++;
            $datalike[$users[$ligne['regid']]]++;
        }
    }
    
/*
    for($j = 1; $j <= $i; $j++){
        echo '<p>User '.$j.' is '.$names[$j].'.</p>'.PHP_EOL;
    }
*/
    
    echo '<h3>Number of messages sent</h3>'.PHP_EOL;
    
    for($j = 1; $j <= $i; $j++){
        echo '<p>User '.$j.' sent '.$data[$j].' messages. '.$datalocation[$j].' for location, '.$datalike[$j].' for likes and '.$datanudges[$j].' for nudges</p>'.PHP_EOL;
    }
    
    echo '<h3>Timelines for nudges</h3>'.PHP_EOL;
    
    for($j = 1; $j <= $i; $j++){
        
        echo '<div style="padding:10px; color:white; vertical-align: bottom; display:inline-block; background-color:blue;"><p>User '.$j.' => </p></div>'.PHP_EOL;
        for($k = 0; $k <= $duration; $k++){
            if($nudgestimeline[$j][$k]>0){
                echo '<div style="color:white; padding:2px; vertical-align: bottom; display:inline-block; height:'.(25*$nudgestimeline[$j][$k]).'px; background-color:RoyalBlue ;">'.$nudgestimeline[$j][$k].'</div>'.PHP_EOL;
            }
            else{
                echo '<div style="color:white; padding:2px; vertical-align: bottom; display:inline-block; height:25px; background-color:white;">0</div>'.PHP_EOL;   
            }  
        }
        echo '<div style="width:max-content; height:2px; background-color:blue; margin-bottom:50px"></div>'.PHP_EOL; 


    }

    echo '<h3>Timelines for likes</h3>'.PHP_EOL;
    
    for($j = 1; $j <= $i; $j++){
        
        echo '<div style="padding:10px; color:white; vertical-align: bottom; display:inline-block; background-color:Sienna;"><p>User '.$j.' => </p></div>'.PHP_EOL;
        for($k = 0; $k <= $duration; $k++){
            if($liketimeline[$j][$k]>0){
                echo '<div style="color:white; padding:2px; vertical-align: bottom; display:inline-block; height:'.(25*$liketimeline[$j][$k]).'px; background-color:orange;">'.$liketimeline[$j][$k].'</div>'.PHP_EOL;
            }
            else{
                echo '<div style="color:white; padding:2px; vertical-align: bottom; display:inline-block; height:25px; background-color:white;">0</div>'.PHP_EOL;   
            }  
        }
        echo '<div style="width:max-content; height:2px; background-color:Sienna; margin-bottom:50px"></div>'.PHP_EOL; 


    }
    
    

    echo '<h3>Timelines for location</h3>'.PHP_EOL;
    
    for($j = 1; $j <= $i; $j++){
        
        echo '<div style="padding:10px; color:white; vertical-align: bottom; display:inline-block; background-color:green;"><p>User '.$j.' => </p></div>'.PHP_EOL;
        for($k = 0; $k <= $duration; $k++){
            if($locationtimeline[$j][$k]>0){
                echo '<div style="color:white; padding:2px; vertical-align: bottom; display:inline-block; height:'.(25*$locationtimeline[$j][$k]).'px; background-color:MediumSeaGreen ;">'.$locationtimeline[$j][$k].'</div>'.PHP_EOL;
            }
            else{
                echo '<div style="color:white; padding:2px; vertical-align: bottom; display:inline-block; height:25px; background-color:white;">0</div>'.PHP_EOL;   
            }  
        }
        echo '<div style="width:max-content; height:2px; background-color:green; margin-bottom:50px"></div>'.PHP_EOL; 


    }
        
?>

<p>The end</p>