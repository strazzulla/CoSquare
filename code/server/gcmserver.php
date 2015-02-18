<!DOCTYPE HTML>
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

//returns true if regid isn't in the database
function checkRegid($regid,$d){
    $query = "SELECT count('regid') AS count FROM users WHERE regid=?";
    $sth = $d->prepare($query);
    $sth->execute(array($regid));
    $a = $sth->fetch(PDO::FETCH_ASSOC);
    return $a['count']<1;
}
//returns the number of user with specified login in the database
function checkCouple($login,$d){
    $query = "SELECT count('login') AS count FROM users WHERE login=?";
    $sth = $d->prepare($query);
    $sth->execute(array($login));
    $a = $sth->fetch(PDO::FETCH_ASSOC);
    return $a['count'];
}
//links in the database the two bffs
function linkCouple($login,$d){
    $query1 = "SELECT * FROM users WHERE login=?";
    $sth1 = $d->prepare($query1);
    $sth1->execute(array($login));
    $a = $sth1->fetch(PDO::FETCH_ASSOC);
    $b = $sth1->fetch(PDO::FETCH_ASSOC);

    $query2 = "UPDATE users SET bffid=? WHERE regid=?";
    $sth2 = $d->prepare($query2);
    $sth2->execute(array($a['regid'],$b['regid']));

    $query3 = "UPDATE users SET bffid=? WHERE regid=?";
    $sth3 = $d->prepare($query3);
    $sth3->execute(array($b['regid'],$a['regid']));
    
    //Sends a message to both phone with the name of their bff to check ok
    sendMessage($a['regid'],"link","",$b['name']);
    sendMessage($b['regid'],"link","",$a['name']);
    sendMessage($a['regid'],"bffid","",$b['regid']);
    sendMessage($b['regid'],"bffid","",$a['regid']);
    
    return "couple linked";
}
//returns the name of a user
function getName($regid,$d){
    $query = "SELECT name FROM users WHERE regid=?";
    $sth = $d->prepare($query);
    $sth->execute(array($regid));
    $a = $sth->fetch(PDO::FETCH_ASSOC);
    return $a['name'];
}
//returns the bff's id (NULL if no Bff)
function getBffId($regid,$d){
    $query = "SELECT bffid FROM users WHERE regid=?";
    $sth = $d->prepare($query);
    $sth->execute(array($regid));
    $a = $sth->fetch(PDO::FETCH_ASSOC);
    return $a['bffid'];
}
//registers a user in the database with no Bff
function registerID($name,$regid,$login,$d){
    $d->prepare("INSERT INTO users (name,regid,login) VALUES (?,?,?)")->execute(array($name,$regid,$login));
    return "registered OK";
}
function updateID($name,$regid,$login,$d){
    $d->prepare("UPDATE users SET name = ?,login = ? WHERE regid = ?")->execute(array($name,$login,$regid));
    return "registered OK";
}
//record any action from users
function adddata($regid,$type,$time,$message,$d){
    $d->prepare("INSERT INTO data (regid,type,time,message) VALUES (?,?,?,?)")->execute(array($regid,$type,$time,$message));
}
//sends a message to the user's phone with payload { type:$type; message:$message }
function sendMessage($to,$type,$time,$message){
    $data = array( 'message' => $message, 'type' => $type, 'time' => $time );
    $ids = array( $to );
    $apiKey = 'AIzaSyCQfbAN222cUeQY4R3GEF3ZXrA5s0INKQo';
    $url = 'https://android.googleapis.com/gcm/send';
    $post = array('registration_ids'  => $ids,'data' => $data,);
    $headers = array('Authorization: key=' . $apiKey,'Content-Type: application/json');
    $ch = curl_init();
    curl_setopt( $ch, CURLOPT_URL, $url );
    curl_setopt( $ch, CURLOPT_POST, true );
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers );
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $post ) );
    $result = curl_exec( $ch );
    if ( curl_errno( $ch ) ){
        echo 'GCM error: ' . curl_error( $ch );
    }
    curl_close( $ch );
    echo $result;
}
//tests others functions
function runTests($d){
        echo 'début des tests : ';
        echo "<br/>test 1 : ";
            if (checkRegid("0123456789", $d))echo registerID("Louis","0123456789","love",$d); else echo 'regid already in use';
        echo "<br/>test 2 : ";
            $t1 = getName("0123456789", $d);
            if ($t1==NULL) echo "NULL";
            else echo "\t".$t1;        
        echo "<br/>test 3 : ";
            $t2 = getBffId("0123456789", $d);
            if ($t2==NULL) echo "NULL";
            else echo "\t".$t2;
        echo "<br/>test 4 : ";
            echo checkCouple("love", $d);
        echo "<br/>test 5 : ";
            if (checkRegid("9876543210", $d))echo registerID("Louise","9876543210","love",$d); else echo 'regid already in use';
        echo "<br/>test 6 : ";
            if (checkCouple("love", $d)==2)echo linkCouple("love",$d);
        echo "<br/>Done";
}

if(array_key_exists('todo',$_GET)){
    if($_GET['todo'] == "regid"){
        if(checkRegid($_POST['regid'],$dbh)){
            //TODO update user info
            $i = checkCouple($_POST['login'],$dbh);
            if($i==0){
                registerID($_POST['name'],$_POST['regid'],$_POST['login'],$dbh);
                sendMessage($_POST['regid'],"registration",$_POST['name'],$_POST['login']);
            }
            if($i==1){
                registerID($_POST['name'],$_POST['regid'],$_POST['login'],$dbh);
                sendMessage($_POST['regid'],"registration",$_POST['name'],$_POST['login']);
                linkCouple($_POST['login'],$dbh);
            }
            if($i==2){
                sendMessage($_POST['regid'],"loginincorrect","","");
            }
            
        }
        else{
            //TODO smth for updates.
            $i = checkCouple($_POST['login'],$dbh);
            if($i==0){
                updateID($_POST['name'],$_POST['regid'],$_POST['login'],$dbh);
                sendMessage($_POST['regid'],"registration",$_POST['name'],$_POST['login']);
            }
            if($i==1){
                updateID($_POST['name'],$_POST['regid'],$_POST['login'],$dbh);
                sendMessage($_POST['regid'],"registration",$_POST['name'],$_POST['login']);
                linkCouple($_POST['login'],$dbh);
            }
        }
        echo 'regid'.PHP_EOL;
    }
    if($_GET['todo'] == "send"){
        sendMessage($_POST['to'],$_POST['type'],$_POST['time'],$_POST['message']);        
    }
    if($_GET['todo'] == "colortoken"){
        sendMessage($_POST['to'],"colortokenreceived",$_POST['time'],$_POST['message']);
        sendMessage($_POST['from'],"colortokensent",$_POST['time'],$_POST['message']);
        adddata($_POST['from'], "colortoken", $_POST['time'], $_POST['message'], $dbh);
    }
    if($_GET['todo'] == "locationtoken"){
        //TODO record data
        sendMessage($_POST['to'],'locationtokenreceived',$_POST['time'],$_POST['message']);
        sendMessage($_POST['from'],'locationtokensent',$_POST['time'],$_POST['message']);
        adddata($_POST['from'], "locationtoken", $_POST['time'], $_POST['message'], $dbh);
    }
    if($_GET['todo'] == "click"){
        adddata($_POST['from'], "like", $_POST['time'], $_POST['message'], $dbh);
    }
    if($_GET['todo'] == "test"){
        runTests($dbh);
    }
    if($_GET['todo'] == "see"){
        $query = "SELECT * FROM users";
        $sth = $dbh->prepare($query);
        $sth->execute(array($regid));
        $n=0;
        while($n<100){
            $a = $sth->fetch(PDO::FETCH_ASSOC);
            echo "<br/>" + $a['login'] + "_" + $a['name'];
            $n++;
        }
    }
}

//include("index.php");

?>




