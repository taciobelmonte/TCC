package taciobelmonte.appoximetria;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.ArrayList;
import java.util.UUID;

import static java.util.UUID.*;

public class DeviceList extends Activity {

    //Variável para liberar acesso discovering do bluetooth no Mashmallow
    private static final int MY_PERMISSION_REQUEST_CONSTANT = 1;

    //Variables to control BT and Layouts
    private BluetoothAdapter bluetooth                  = null;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> otherDevices     = new ArrayList<BluetoothDevice>();
    private ListView pairedDevicesList;
    private ListView otherDevicesList;
    public boolean isBtConnected                        = false;
    public boolean ConnectSuccess                       = true;
    static final UUID myUUID                            = fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket                            = null;
    String address                                      = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_device_list);

         //Define os elementos do layout
        pairedDevicesList = (ListView) findViewById(R.id.pairedDevicesList);
        otherDevicesList = (ListView) findViewById(R.id.otherDevices);

        //Recupera adaptador bluetooth
        bluetooth = BluetoothAdapter.getDefaultAdapter();

        //Dispositivo tem adaptador bluetooth?
        if (bluetooth == null) {

            message("Bluetooth não disponível");
            finish();

        } else {

            //Bluetooth Ativo?
            if (bluetooth.isEnabled()) {

                //Solicita permissão
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CONSTANT);

                message("Buscando dispositivos...");
                bluetooth.startDiscovery();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(mReceiver, filter);

                //Buscar dispositivos pareados
                displayPairedDevices();

            } else {

                //Liga bluetooth
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon, 1);

                //Buscar dispositivos pareados
                displayPairedDevices();
            }
        }
    }

    void loadDataActivity() {

        Intent intent = new Intent();
        intent.setClass(DeviceList.this, DataDisplay.class);

        intent.putExtra("isBtConnected", isBtConnected);
        intent.putExtra("address", address);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    //Define receiver para buscar dispositivos que não estão pareados
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            ArrayList devices = new ArrayList();

            //Dispositivo encontrado!
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!pairedDevices.contains(device) && !otherDevices.contains(device)) {

                    message("Dispositivos encontrados! Aguarde finalizar a busca!");

                    otherDevices.add(device);
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                message("Busca finalizada!");

                if (otherDevices.size() > 0) {
                    otherDevicesList.invalidateViews();

                    for (BluetoothDevice bt : otherDevices)
                        devices.add(bt.getName() + "\n" + bt.getAddress());

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, devices);

                    otherDevicesList.setAdapter(adapter);
                    otherDevicesList.setOnItemClickListener(connectDevice);

                } else {

                    Toast.makeText(DeviceList.this, "Nenhum dispositivo encontrado!", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        }
    };

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    message("Dispositivo habilitado para buscar devices com bluetooth ativo!");
                }
                return;
            }
        }
    }

    //Method to display Paired Devices
    private void displayPairedDevices() {

        pairedDevices = bluetooth.getBondedDevices();
        ArrayList lista = new ArrayList();

        //Detecta Dispositivos Pareados
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice bt : pairedDevices)
                lista.add(bt.getName() + "\n" + bt.getAddress());

            //Adiciona dispositivos pareados encontrados na ListView
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
            pairedDevicesList.setAdapter(adapter);
            pairedDevicesList.setOnItemClickListener(connectDevice);

        } else {
            message("Não foram encontrados dispositivos pareados!");
        }
    }

    //Method called when clicking on any Bluetooth device listed
    private AdapterView.OnItemClickListener connectDevice = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);

            loadDataActivity();
        }
    };

    public void connectBT() {
        try {
            if (btSocket == null || isBtConnected) {

                BluetoothDevice dispositivo = bluetooth.getRemoteDevice(address);
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                bluetooth.cancelDiscovery();

                btSocket.connect();
            }
        } catch (IOException e) {
            ConnectSuccess = false;
        }

        if (!ConnectSuccess) {

            finish();

        } else {
            isBtConnected = true;

            loadDataActivity();
        }
    }

    //Method to display Message Dialog
    private void message(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}



