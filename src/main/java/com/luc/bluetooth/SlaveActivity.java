package com.luc.bluetooth;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class SlaveActivity extends Activity {
    BluetoothArduino bluetoothArduino;
  String commandSent="";

    Thread workerThread;
   volatile boolean stopWorker;
    TextView lblDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);
        lblDefault=(TextView)findViewById(R.id.lblDefault);
      lblDefault.setText("App is in Salve mode. Webservice has the control...");
       bluetoothArduino= BluetoothActivity.bluetoothArduino;
        beginListenForData();


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopWorker=true;
        try {
            bluetoothArduino.sendData("s");
        }
        catch (Exception ex)
        {}
    }
    @Override
    protected void onResume() {
        super.onPause();
        stopWorker=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    void beginListenForData() {
        stopWorker = false;
        System.out.println("Started");
        final Handler handler = new Handler();
        workerThread = new Thread(new Runnable() {
            public void run() {

                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        try {
                            new MyAsyncTask().execute("").get();// myAsyncTask.execute().get();
              //              lblDefault.setText(booksRequestedRepresentation.getLinks().get(0).getUrl());
                        }
                        catch (Exception ex)
                        {

                        }
                        try {
                             Thread.sleep(200);
                         }
                         catch (InterruptedException ex)
                         { }
                          //  }
                        //});
                              //Do the get here
                    } catch (Exception ex) {
                        stopWorker = true;
                      System.out.println("fuck "+ ex.getMessage());
                           // Toast.makeText(    getApplicationContext(),ex.   getMessage(),Toast.LENGTH_SHORT).show();

                        //setContentView(R.layout.activity_slave);
                    }
                }
            }
        });

        workerThread.start();
    }


    class MyAsyncTask extends AsyncTask<String,Void,SlaveCommand> {

        @Override
        protected SlaveCommand doInBackground(String... urls) {
           return   CallAPI.get("http://rayana.azurewebsites.net/service.aspx");

           // return booksRequestedRepresentation;
        }

        @Override
        protected void onPostExecute(SlaveCommand command) {
            try {
              String tempCommand=command.getCommand();
                if(!tempCommand.equals(commandSent)) {
                    bluetoothArduino.sendData(tempCommand);
                    commandSent = tempCommand;
                }
               //System.out.println(command.getCommand());

            }
            catch (Exception ex)
            {
            }
        }
    }

}


