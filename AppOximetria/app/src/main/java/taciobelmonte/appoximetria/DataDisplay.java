package taciobelmonte.appoximetria;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import helpers.MQTTHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;

public class DataDisplay extends Activity {

    MQTTHelper mqttHelper;

    TextView textoBatimentos;
    TextView textoOximetria;

    BluetoothSocket btSocket = null;
    String address = null;
    private BluetoothAdapter bluetooth = null;
    boolean isBtConnected = false;
    public boolean ConnectSuccess = true;
    static final UUID myUUID = fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "bluetooth";
    Handler h;

    int counter = 0;
    int flag = 0;

    List<String> oximetria  = new ArrayList<String>();
    List<String> batimentos = new ArrayList<String>();

    final int RECIEVE_MESSAGE = 1;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data_display);

        textoBatimentos = (TextView) findViewById(R.id.textoBatimentos);
        textoOximetria = (TextView) findViewById(R.id.textoOximetria);

        Intent i = getIntent();

        isBtConnected = i.getBooleanExtra("isBtConnected", isBtConnected);
        address = i.getStringExtra("address");

        mqttHelper = new MQTTHelper(getApplicationContext());

        connectBT();

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {

                switch (msg.what) {

                    case RECIEVE_MESSAGE:
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);
                        int endOfLineIndex = sb.indexOf("\r\n");

                        if (endOfLineIndex > 0) {
                            String sbprint = sb.substring(0, endOfLineIndex);
                            sb.delete(0, sb.length());

                            String[] data = sbprint.split("/");

                            textoBatimentos.setText( data[0]);
                            textoOximetria.setText(data[1] + '%');

                            if (oximetria.isEmpty()) {

                                oximetria.add( data[1] );
                                MqttMessage oximetro = new MqttMessage(data[1].getBytes());
                                oximetro.setQos(2);
                                oximetro.setRetained(false);

                                try {
                                    mqttHelper.mqttAndroidClient.publish("sensor/oximetria", oximetro);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }

                            }else{

                                String value = oximetria.get(oximetria.size() - 1);

                                if(value.equals(data[1]) || data[1].equals("0")){
                                    //faz nada

                                }else{
                                    MqttMessage oximetro = new MqttMessage(data[1].getBytes());
                                    oximetro.setQos(2);
                                    oximetro.setRetained(false);
                                    //oximetria.add( data[1] );

                                    oximetria  = new ArrayList<String>();
                                    oximetria.add( data[1] );

                                    try {
                                        mqttHelper.mqttAndroidClient.publish("sensor/oximetria", oximetro);
                                        //  mqttHelper.mqttAndroidClient.publish("sensor/bpm", bpm);
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            //Ajusta os valores dos Batimentos
                            if (batimentos.isEmpty()) {

                                batimentos.add( data[0] );
                                MqttMessage bpm = new MqttMessage(data[0].getBytes());
                                bpm.setQos(2);
                                bpm.setRetained(false);
                                try {
                                    mqttHelper.mqttAndroidClient.publish("sensor/bpm", bpm);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                String value = batimentos.get(batimentos.size() - 1);

                                if(value.equals(data[0]) || data[0].equals("0.00")){


                                }else{
                                    MqttMessage bpm = new MqttMessage(data[0].getBytes());
                                    bpm.setQos(2);
                                    bpm.setRetained(false);

                                    batimentos  = new ArrayList<String>();
                                    batimentos.add( data[0] );


                                    try {
                                        mqttHelper.mqttAndroidClient.publish("sensor/bpm", bpm);
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        break;
                }
            };
        };

        if (isBtConnected) {

            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();

        } else {
            connectBT();
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {

                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();

            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {

                try {
                    bytes = mmInStream.read(buffer);
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }

    public void connectBT() {
        try {

            bluetooth = BluetoothAdapter.getDefaultAdapter();

            if (btSocket == null || isBtConnected) {

                BluetoothDevice dvc = bluetooth.getRemoteDevice(address);
                btSocket = dvc.createInsecureRfcommSocketToServiceRecord(myUUID);
                bluetooth.cancelDiscovery();

                btSocket.connect();
            }

        } catch (IOException e) {

            ConnectSuccess = false;
        }

        if (!ConnectSuccess) {

            message("Falha ao conectar!");
            finish();

        } else {
            message("Conectado!");
            isBtConnected = true;
        }
    }

    public boolean isEmptyStringArray(String [] array){
        for(int i=0; i<array.length; i++){
            if(array[i]!=null){
                return false;
            }
        }
        return true;
    }

    private void message(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}