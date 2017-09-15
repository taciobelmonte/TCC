<?php
//Template Name: Batimentos Cardiacos
include "header.php";?>

    <!-- Body -->
    <main>
        <div class=" ">
            <div class="row">
                <div class="col s12 m12 l12">
                    <div id="broker" class="center">
                        <h2>Batimentos Cardíacos:</h2>
                        <p>Conectar ao broker para receber os dados em tempo real</p>
                        <input class="btn red" id="connect-button" type="button" value="Conectar">
                    </div>
                    <br />

                    <div class="center">
                        <canvas id="myChart" width="400" height="100"></canvas>
                        <script type="text/javascript">
                            $(document).ready(function() {
                                var chart;
                                var count = 0;

                                client = new Paho.MQTT.Client("m12.cloudmqtt.com", 36068, "ExampleAndroidClient1");
                                $('#connect-button').on('click', function () {
                                    client.connect(options);
                                });

                                // set callback handlers
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
                                    client.subscribe("sensor/bpm");
                                    message = new Paho.MQTT.Message("Hello CloudMQTT");
                                    message.destinationName = "sensor/bpm";
                                    client.send(message);

                                    //Get the context of the canvas element we want to select
                                    var ctx = document.getElementById("myChart").getContext("2d");
                                    chart = new Chart(ctx, {
                                        type: "line",
                                        data: {
                                            labels: [],
                                            datasets: [{
                                                data: [],
                                                label: "Batimentos Cardíacos",
                                                borderColor: "#ff0000",
                                                fill: false
                                            }
                                            ]
                                        },
                                        options: {}

                                    });
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

                                    if(!isNaN(message.payloadString)){

                                        if(message.payloadString != '0.00'){
                                            chart.data.labels.push(parseFloat(message.payloadString));
                                            chart.data.datasets[0].data.push(parseFloat(message.payloadString));
                                            chart.update();

                                            if(count > 50){
                                                chart.data.labels.splice(0, 1);
                                                chart.data.datasets[0].data.shift();
                                            }
                                            count++;
                                        }
                                    }
                                }
                            });
                        </script>
                    </div>

                    <br />
                    <br />
                    <br />

                    <?php /*<div class="center">
                        <h2>Histórico:</h2>
                        <input class="btn green" type="button" value="Visualizar Histórico">
                    </div>*/?>

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
                        <a class="btn red" href="<?php echo site_url();?>">Voltar para Home</a>
                    </div>
                </div>
            </div>
        </div>
    </main>
<?php include "footer.php";?>
