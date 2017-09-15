import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.Set;
import java.util.UUID;

import static java.util.UUID.fromString;

public class App extends Application {

    private BluetoothAdapter bluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    String address = null;
    BluetoothSocket btSocket = null;
    public boolean isBtConnected = false;
    public boolean ConnectSuccess = true;
    static final UUID myUUID = fromString("00001101-0000-1000-8000-00805F9B34FB");


    public void setBluetooth(BluetoothAdapter bluetooth) {
        this.bluetooth = bluetooth;
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return pairedDevices;
    }

    public void setPairedDevices(Set<BluetoothDevice> pairedDevices) {
        this.pairedDevices = pairedDevices;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BluetoothSocket getBtSocket() {
        return btSocket;
    }

    public void setBtSocket(BluetoothSocket btSocket) {
        this.btSocket = btSocket;
    }

    public boolean isBtConnected() {
        return isBtConnected;
    }

    public void setBtConnected(boolean btConnected) {
        isBtConnected = btConnected;
    }

    public boolean isConnectSuccess() {
        return ConnectSuccess;
    }

    public void setConnectSuccess(boolean connectSuccess) {
        ConnectSuccess = connectSuccess;
    }

    public static UUID getMyUUID() {
        return myUUID;
    }
}