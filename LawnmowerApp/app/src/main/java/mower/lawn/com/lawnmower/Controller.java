package mower.lawn.com.lawnmower;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.UUID;

public class Controller extends AppCompatActivity {

    Button Disconnect;
    ImageButton Up, Down, Left, Right;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(BlueToothConnect.EXTRA_ADDRESS);

        setContentView(R.layout.activity_controller);

        Disconnect = findViewById(R.id.buttonDisconnect);
        Up = findViewById(R.id.buttonUp);
        Down = findViewById(R.id.buttonDown);
        Left = findViewById(R.id.buttonLeft);
        Right = findViewById(R.id.buttonRight);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Controller.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(), "Connection Failed. Is it a SPP Bluetooth? Try again." ,Toast.LENGTH_LONG).show();
                //msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Connected." ,Toast.LENGTH_LONG).show();
                //msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    public void Disconnect(View v)
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Error." ,Toast.LENGTH_LONG).show();
                //msg("Error");
            }
        }
        finish(); //return to the first layout
    }

    public void Up(View v)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("Up".toString().getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Error." ,Toast.LENGTH_LONG).show();
                //msg("Error");
            }
        }
    }

    public void Down(View v)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("Dn".toString().getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Error." ,Toast.LENGTH_LONG).show();
                //msg("Error");
            }
        }
    }

    public void Left(View v)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("Lf".toString().getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Error." ,Toast.LENGTH_LONG).show();
                //msg("Error");
            }
        }
    }

    public void Right(View v)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("Rt".toString().getBytes());
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "Error." ,Toast.LENGTH_LONG).show();
                //msg("Error");
            }
        }
    }
}