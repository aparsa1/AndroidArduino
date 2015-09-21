package com.luc.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class BluetoothActivity extends Activity  implements SensorEventListener {

    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    boolean LightsOn;
    volatile boolean stopWorker;
    String messageSent;
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private Button openButton;
    private Button btnLight,btnFoward,btnBackward,btnRight,btnLeft,btnHorn;
    private TextView txtTemp,txtHumidity;
    private SeekBar seekBar;
 public static BluetoothArduino bluetoothArduino;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        messageSent="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButtonTouchListener buttonTouchListener=new ButtonTouchListener();
        openButton = (Button) findViewById(R.id.open);
        btnFoward= (Button) findViewById(R.id.btnForward);
        btnBackward= (Button) findViewById(R.id.btnReverse);
        btnRight=(Button)findViewById(R.id.btnRight);
        btnLeft=(Button)findViewById(R.id.btnLeft);
        btnHorn=(Button)findViewById(R.id.btnHorn);
        btnLight=(Button)findViewById(R.id.btnLight);
        btnHorn.setOnTouchListener(buttonTouchListener);
        btnBackward.setOnTouchListener(buttonTouchListener);
        btnFoward.setOnTouchListener(buttonTouchListener);
        btnLight.setOnTouchListener(buttonTouchListener);
        LightsOn=false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        txtTemp= (TextView)findViewById(R.id.txtTemp);
        txtHumidity=(TextView)findViewById(R.id.txtHumidity);
        seekBar=(SeekBar)findViewById(R.id.seekBar);

        bluetoothArduino=new BluetoothArduino();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.control) {
            Intent intent =new Intent(getApplicationContext(),AutomaticActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void automateControl(View view) {

        Intent intent =new Intent(getApplicationContext(),AutomaticActivity.class);
        startActivity(intent);
    }

    public void Slave(View view) {
        Intent intent =new Intent(getApplicationContext(),SlaveActivity.class);
        startActivity(intent);

    }


//Interface code for moving the right,left, forward and backward=====================================================================

    public class ButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {

            // TODO Auto-generated method stub

            int action = arg1.getAction();
            int id = arg0.getId();

            if (action == MotionEvent.ACTION_DOWN) {


                switch (id) {
                    case R.id.btnForward:
                        sendData("f");
                        //readData();
                        btnFoward.setBackgroundColor(Color.YELLOW);

                        break;
                    case R.id.btnReverse:

                        sendData("b");
                        btnBackward.setBackgroundColor(Color.YELLOW);

                        break;

                    case R.id.btnHorn:

                        sendData("t");
                        btnHorn.setBackgroundColor(Color.YELLOW);
                        break;
                    case R.id.btnLight:
                        if(!LightsOn) {
                            sendData("d");
                            btnLight.setBackgroundColor(Color.YELLOW);
                            LightsOn=true;
                        }
                        else
                        {
                            sendData("e");
                            btnLight.setBackgroundColor(Color.WHITE);
                            LightsOn=false;
                        }
                        break;

                }

            } else if (action == MotionEvent.ACTION_UP) {

                switch (id) {
                    case R.id.btnForward:

                        sendData("z");
                        btnFoward.setBackgroundColor(Color.BLUE);

                        break;

                    case R.id.btnReverse:

                        sendData("y");
                        btnBackward.setBackgroundColor(Color.BLUE);

                        break;

                    case R.id.btnHorn:
                        sendData("x");
                        btnHorn.setBackgroundColor(Color.WHITE);
                        break;

                }

            }

            return true;
        }

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];

        if(roll_angle>20)
        {
            btnLeft.setBackgroundColor(Color.YELLOW);
            btnRight.setBackgroundColor(Color.BLUE);
            sendData("l");
            //Toast.makeText(this,"Turn Left",Toast.LENGTH_SHORT).show();
        }
        else if (roll_angle<-20)
        {
            btnRight.setBackgroundColor(Color.YELLOW);
            btnLeft.setBackgroundColor(Color.BLUE);
            sendData("r");
            // Toast.makeText(this,"Turn Right",Toast.LENGTH_SHORT).show();
        }
        else
        {
            btnRight.setBackgroundColor(Color.BLUE);
            btnLeft.setBackgroundColor(Color.BLUE);
            sendData("v");
        }

    }






    //BLUETOOTH  communication==============================================================================================================


    public void findBT(View view) {

        Toast.makeText(this,"Connection...",Toast.LENGTH_SHORT).show();
        if (bluetoothArduino.isConnected()) {  //if it is already connect , disconnect it
                sendData("s");                 //stop robot
                closeBT();                     //Close connection
                openButton.setBackgroundColor(Color.BLUE);
                return;
        }



        String result="ok";
         try {
             bluetoothArduino.findBT();    //Try to open the connection
         }
         catch (IOException io)
         {
             result=io.getMessage();
             Toast.makeText(this,"Error "+result,Toast.LENGTH_LONG).show();
             return;
         }

         if(result.equals("no")) {    //Device doesn't have Bluetooth
           Toast.makeText(this,"No bluetooth adapter available",Toast.LENGTH_SHORT).show();
        }

        else if(result.equals("enable")) {   ///Device's Bluetooth is not enabled
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        else if (result.equals("ok"))  //Connection is open
         {
             beginListenForData();   //Start listening for incomming data
             openButton.setBackgroundColor(Color.YELLOW);
             Toast.makeText(this, "Bluetooth Opened", Toast.LENGTH_SHORT).show();
         }


    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {

                while(!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        byte[] packetBytes =bluetoothArduino.readData();

                        if(packetBytes.length > 0) {



                            for(int i=0;i<packetBytes.length;i++) {
                                byte b = packetBytes[i];
                                if(b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            if(!data.isEmpty()) {

                                                String  code =data.substring(0,1);

                                                if(code.equals("l"))
                                                {
                                                    seekBar.setProgress(ProcessInput.processLight(data));
                                                }
                                                else if(code.equals("t"))
                                                {
                                                    txtTemp.setText(ProcessInput.processTemperature(data)+" f");
                                                }
                                                else if(code.equals("d"))
                                                {
                                                    txtHumidity.setText(ProcessInput.processDistance(data));
                                                }
                                            }
                                        }
                                    });
                                }
                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }


    public    void sendData(String msg) {
        msg += "\n";
        if(!msg.equals(messageSent)) {

                try {
                    bluetoothArduino.sendData(msg);
                    messageSent=msg;
                } catch (IOException io) {
                    Toast.makeText(this, "Connection Failed, " + io.getMessage(), Toast.LENGTH_SHORT);
                }

        }
    }
    public void closeBT() {
        stopWorker = true;
        try {
            bluetoothArduino.closeBT();
            Toast.makeText(this, "Bluetooth Closed", Toast.LENGTH_SHORT);
        }
        catch (IOException ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
        }
    }

}