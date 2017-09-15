<?php
//Template Name: Oximetria
include "header.php";?>

    <!-- Body -->
    <main>
        <div class=" ">
            <div class="row">
                <div class="col s12 m12 l12">
                    <div id="broker" class="center">
                        <h2>Oximetria:</h2>
                        <p>Conectar ao broker para receber os dados em tempo real</p>
                        <input class="btn white" id="connect-button" type="button" value="Conectar">
                    </div>
                    <br />

                    <div class="center">
                        <div id="chart_div" style="width: 295px; height: 120px; margin:0 auto"></div>
                        <script type="text/javascript">

                            $(document).ready(function() {
                                var data;
                                var chart;
                                var count = 0;
                                var options;

                                client = new Paho.MQTT.Client("m12.cloudmqtt.com", 36068, "ExampleAndroidClient1");

                                $('#connect-button').on('click', function () {
                                    client.connect(options);
                                });
                                $('#disconnect-button').on('click', function () {

                                });

                                client.onConnectionLost = onConnectionLost;
                                client.onMessageArrived = onMessageArrived;
                                var options = {
                                    useSSL: true,
                                    userName: "clbrpfgu",
                                    password: "WtYj2-uGmzKu",
                                    onSuccess: onConnect,
                                    onFailure: doFail
                                };
                                function onConnect() {
                                    console.log("onConnect");

                                    client.subscribe("sensor/oximetria");
                                    message = new Paho.MQTT.Message("Hello CloudMQTT");
                                    message.destinationName = "sensor/oximetria";
                                    client.send(message);

                                    google.charts.load('current', {'packages':['gauge']});
                                    google.charts.setOnLoadCallback(drawChart);

                                    function drawChart() {
                                        data = google.visualization.arrayToDataTable([
                                            ['Oximetria', 'Value'],
                                            ['Oximetria', 0],
                                        ]);
                                        options = {
                                            width: 600, height: 300,
                                            minorTicks: 5
                                        };

                                        chart = new google.visualization.Gauge(document.getElementById('chart_div'));
                                        chart.draw(data, options);
                                    }
                                }
                                function doFail(e) {
                                    console.log(e);
                                }

                                function onConnectionLost(responseObject) {
                                    if (responseObject.errorCode !== 0) {
                                        console.log("onConnectionLost:" + responseObject.errorMessage);
                                    }
                                }

                                function onMessageArrived(message) {
                                    console.log("onMessageArrived:" + message.payloadString);
                                    if(message.payloadString != '0' && message.payloadString != 'Hello CloudMQTT'){

                                        var string = message.payloadString;
                                        var float = parseFloat(string[0]);

                                        data.setValue(0, 1, string);
                                        chart.draw(data, options);
                                        count++;
                                    }

                                }
                            });
                        </script>
                    </div>

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />

                    <div id="broker" class="center">
                        <a class="btn blue" href="<?php echo site_url();?>">Voltar para Home</a>
                    </div>
                </div>
            </div>
        </div>
    </main>
<?php include "footer.php";?>