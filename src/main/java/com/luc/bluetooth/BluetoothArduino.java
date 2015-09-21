package com.luc.bluetooth;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by aziz on 3/17/15.
 */
public class BluetoothArduino {


    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    String deviceName = "HC-06";



  public  boolean isConnected()

    {
        if(mmSocket!=null) {
            if (mmSocket.isConnected()) {
                return true;
            }
        }
        return false;

    }

    public String findBT() throws  IOException{


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            return "no"; //No bluetooth adapter available
        }

        if(!mBluetoothAdapter.isEnabled()) {
           return "enable";
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for(BluetoothDevice device : pairedDevices) {
                if(device.getName().equals(deviceName)) {
                    mmDevice = device;
                    break;
                }
            }
        }

        openBT();
        return "ok";
    }

    public void openBT() throws IOException {

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();


    }
    public byte[] readData() throws IOException {

        int bytesAvailable = mmInputStream.available();
        byte[] packetBytes = new byte[bytesAvailable];

        if (bytesAvailable > 0) {
            mmInputStream.read(packetBytes);
        }
        return packetBytes;

    }

    public    boolean sendData(String msg) throws  IOException{

        if(isConnected()) {
                mmOutputStream.write(msg.getBytes());
            return true;
        }
        return false;
   }
    public void closeBT() throws IOException {

        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
    }




}
