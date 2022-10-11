package com.example.cartracking;

import static android.service.controls.ControlsProviderService.TAG;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CarStart extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    private final BroadcastReceiver broadCastReceiver= new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.R)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        }
                        break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_start);
        Button startCar = findViewById(R.id.startCar);
        startCar.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show());
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableDisabledBluetooth();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void enableDisabledBluetooth() {
        if (bluetoothAdapter == null) {
            Log.d(TAG, "enabledDisabled: Does not have bluetooth capabilities");
        } else if (bluetoothAdapter.isEnabled()) {
            Intent enabledIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(CarStart.this,"Permission not Granted",Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(enabledIntent);
            IntentFilter intentFilter= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(broadCastReceiver,intentFilter);
        }
        if (bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();
            IntentFilter intentFilter= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(broadCastReceiver,intentFilter);
        }

    }
    public void requestPermission(View view){

        if (ContextCompat.checkSelfPermission(CarStart.this, Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CarStart.this,new String[]{
                    Manifest.permission.BLUETOOTH
            },100);
        }
    }
}