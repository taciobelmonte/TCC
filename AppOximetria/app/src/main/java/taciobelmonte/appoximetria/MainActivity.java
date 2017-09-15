package taciobelmonte.appoximetria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.MQTTHelper;

public class MainActivity extends Activity {

    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_activity);

        //Associa botão criado
        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startMqtt();
            }
        });
    }

    private void startMqtt(){
        MQTTHelper mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

                Log.w("Debug","conectou!");
                loadDeviceListActivity();

            }

            @Override
            public void connectionLost(Throwable throwable) {

                message("conexao perdida!");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                //Log.w("Debug",mqttMessage.toString());
                Log.w("Debug",mqttMessage.getPayload().toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    //Method to display Message Dialog
    private void message(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    void loadDeviceListActivity() {

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DeviceList.class);

        startActivity(intent);
        finish();

    }
}
