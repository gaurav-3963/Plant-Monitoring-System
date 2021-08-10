<?php
define('DB_USER', "id16465356_plantmonitor");     // Your database user name
define('DB_PASSWORD', "#zls=Wr?PZuV]J1*");            // Your database password (mention your db password here)
define('DB_DATABASE', "id16465356_plantmonitoringsystem"); // Your database name
define('DB_SERVER', "localhost");

$servername = "localhost";
$username = "id16465356_plantmonitor";
$password = "#zls=Wr?PZuV]J1*";
$database = "id16465356_plantmonitoringsystem";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {

    echo "Connected unsuccessfully";
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT * FROM `parameters`";
$sql2 = "SELECT * FROM parameters ORDER BY id DESC LIMIT 1;";
$sql3 = "SELECT status FROM led where id =1";

$result2 = $conn->query($sql2);
$result = $conn->query($sql);
$result3 = $conn->query($sql3);

if ($result->num_rows > 0) {
    while ($row2 = $result3->fetch_assoc()) {
        echo ($row2["status"]);
        $motorstate = $row2["status"];
    }
}

if ($result->num_rows > 0) {
    while ($row1 = $result2->fetch_assoc()) {
        $Humidity = $row1["hum"];
        $Moisture = $row1["moist"];
        $Temperature = $row1["temp"];
    }
}
?>
<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">

    <title>Plant Monitoring System</title>
</head>

<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                Plant Monitoring System
            </a>
        </div>
    </nav>

    <br>
    <br>

    <div class="card" style="text-align: center;">
        <div class="card-header" style="color: black; text-align: center; font-size: 25px;font-style: normal; font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;">
            Moisture
        </div>
        <div class="card-body">
            <div>
                <h5 class="card-title" style="border: 1px;    border-style: dotted;
                text-align: center;">Value : <?php echo ($Moisture); ?> </h5>
            </div>

            <p class="card-text">Value should be more than and less than to Maintain plant in safe state</p>
            <a href="#" class="btn btn-primary">For More Info</a>
        </div>
    </div>

    <br>
    <br>

    <div class="card" style="text-align: center;">
        <div class="card-header" style="color: black; text-align: center; font-size: 25px;font-style: normal; font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;">
            Temperature
        </div>
        <div class="card-body">
            <div>
                <h5 class="card-title" style="border: 1px;    border-style: dotted;
                text-align: center;">Value : <?php echo ($Temperature); ?> </h5>
            </div>

            <p class="card-text">Value should be more than and less than to Maintain plant in safe state</p>
            <a href="#" class="btn btn-primary">For More Info</a>
        </div>
    </div>

    <br>
    <br>

    <div class="card" style="text-align: center;">
        <div class="card-header" style="color: black; text-align: center; font-size: 25px;font-style: normal; font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;">
            Humidity
        </div>
        <div class="card-body">
            <div>
                <h5 class="card-title" style="border: 1px;    border-style: dotted;
                text-align: center;">Value : <?php echo ($Humidity); ?></h5>
            </div>

            <p class="card-text">Value should be more than and less than to Maintain plant in safe state</p>
            <a href="#" class="btn btn-primary">For More Info</a>
        </div>
    </div>

    <br>
    <br>

    <div class="card" style="text-align: center;">
        <div class="card-header" style="color: black; text-align: center; font-size: 25px;font-style: normal; font-family: Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;">
            Motor
        </div>
        <div class="card-body">
            <div>
                Current Motor Status : <?php echo ($motorstate); ?>
            </div>
            <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                <button type="button" onclick="buttonon()" class="btn btn-danger">On</button>
                <button type="button" onclick="buttonoff()" class="btn btn-success">Off</button>
            </div>
        </div>
    </div>

    <br>
    <br>

    <table class="table table-dark table-striped">
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Temperature</th>
                <th scope="col">Humidity</th>
                <th scope="col">Moisture</th>
            </tr>
        </thead>
        <tbody>
            <?php
            if ($result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
            ?>
                    <tr>
                        <th scope="row"><?php echo ($row["ID"]); ?></th>
                        <td><?php echo ($row["temp"]); ?></td>
                        <td><?php echo ($row["hum"]); ?></td>
                        <td><?php echo ($row["moist"]); ?></td>
                    </tr>
                <?php } ?>
        </tbody>
    </table>

<?php
            }
            $conn->close();
?>
<br>
<br>
<br>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>

<script type="text/javascript">
$.ajax({
    url: 'data.json',
    dataType: 'json',
    success: function(data) {
        for (var i=0; i<data.length; i++) {
            var row = $('<tr><td>' + data[i].zipcode+ '</td><td>' + data[i].city + '</td><td>' + data[i].county + '</td></tr>');
            $('#myTable').append(row);
        }
    },
    error: function(jqXHR, textStatus, errorThrown){
        alert('Error: ' + textStatus + ' - ' + errorThrown);
    }
});
</script>

<script type="text/javascript">
    $(document).ready(function() {
        setTimeout(function() {
            location.reload(true);
        }, 5000);
    });

    function buttonoff() {
        let request = new XMLHttpRequest();
        request.open("GET", "https://plantmonitoringsystem.000webhostapp.com/api/update_motor.php?id=1&status=off");
        request.send();
        request.onload = () => {
            console.log(request);
            if (request.status == 200) {
                console.log(JSON.parse(request.response));
            } else {
                console.log('error ${request.status}');
            }
        }
        location.reload(true);
    }

    function buttonon() {
        let request = new XMLHttpRequest();
        request.open("GET", "https://plantmonitoringsystem.000webhostapp.com/api/update_motor.php?id=1&status=on");
        request.send();
        request.onload = () => {
            console.log(request);
            if (request.status == 200) {
                console.log(JSON.parse(request.response));
            } else {
                console.log('error ${request.status}');
            }
        }
        location.reload(true);
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>

</body>

</html>