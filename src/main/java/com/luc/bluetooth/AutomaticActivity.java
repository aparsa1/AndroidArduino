package com.luc.bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class AutomaticActivity extends Activity
{
    TableLayout tableLayout;
    Thread workerThread;
    volatile boolean stopWorker;
    BluetoothArduino bluetoothArduino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic);



         tableLayout=(TableLayout) findViewById(R.id.tlMain);
        bluetoothArduino=BluetoothActivity.bluetoothArduino;
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_automatic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.control) {
            Intent intent =new Intent(getApplicationContext(),BluetoothActivity.class);
            startActivity(intent);
        }
        else if (id==R.id.slave)
        {

            Intent intent =new Intent(getApplicationContext(),SlaveActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public void Start(View view) {

            run();
    }
public void PerformAction(String Action,int time)
{
    try {

        if(time>0) {
            if (Action.trim().equals("Forward")) {
                bluetoothArduino.sendData("f\n");
            } else if (Action.trim().equals("Reverse")) {
                bluetoothArduino.sendData("b\n");
            } else if (Action.trim().equals("ReverseRight")) {
                bluetoothArduino.sendData("r\n");
                bluetoothArduino.sendData("b\n");
            } else if (Action.trim().equals("ReverseLeft")) {
                bluetoothArduino.sendData("l\n");
                bluetoothArduino.sendData("b\n");
            } else if (Action.trim().equals("ForwardRight")) {
                bluetoothArduino.sendData("r\n");
                bluetoothArduino.sendData("f\n");
            } else if (Action.trim().equals("ForwardLeft")) {
                bluetoothArduino.sendData("l\n");
                bluetoothArduino.sendData("f\n");
            } else if (Action.trim().equals("Horn")) {
                bluetoothArduino.sendData("t\n");
            } else if (Action.trim().equals("Light")) {
                bluetoothArduino.sendData("d\n");
            } else if (Action.trim().equals("Stop")) {
                bluetoothArduino.sendData("s\n");
            }

            for (int i = 0; i < time; i++) {
                bluetoothArduino.sendData("w\n");

            }
            bluetoothArduino.sendData("s\n");
        }
    }
    catch (IOException io)
    {

        Toast.makeText(getApplicationContext(),io.getMessage(),Toast.LENGTH_LONG).show();

    }

}

    public void run() {
          //stopWorker=false;
              //  while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {

                        int Seconds = 0;
                        String Action = "v";


                        for (int i = 0; i < tableLayout.getChildCount(); i++) {
                            View r = tableLayout.getChildAt(i);
                            if (r instanceof TableRow) {

                                TableRow row = (TableRow) r;
                                for (int j = 0; j < row.getChildCount(); j++) {
                                    View v = row.getChildAt(j);

                                    if (v instanceof EditText) {
                                        EditText temptxt = (EditText) v;
                                        Seconds = Integer.parseInt(temptxt.getText().toString().trim());
                                    } else if (v instanceof Spinner) {
                                        Spinner tempspinner = (Spinner) v;
                                        Action = tempspinner.getSelectedItem().toString();
                                    }

                                }

                                try {
                                    PerformAction(Action, Seconds);
                                 //  Thread.sleep(Seconds * 1000);                 //1000 milliseconds is one second.
                                } catch (Exception ex) {

                                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();

                                    //  PerformAction(ex.getMessage(), Seconds);
                                //    Thread.currentThread().interrupt();
                                }
                            }
                        }

                        //stopWorker=true;
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();

                    }
            //    }
            }




    public void AddAction(View view) {
        TableRow  tableRow=new TableRow(this);
            tableRow.setOrientation(LinearLayout.HORIZONTAL);
            EditText txtSec=new EditText(this);
        txtSec.setWidth(200);
        String[] strings={"Forward","Reverse","ForwardLeft","ForwardRight","ReverseLeft","ReverseRight","Horn","Light","Stop"};
        Spinner spinner=new Spinner(this);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strings));

            tableRow.addView(spinner);
            tableRow.addView(txtSec);


            tableLayout.addView(tableRow);
        }



}
