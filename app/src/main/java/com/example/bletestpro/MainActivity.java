package com.example.bletestpro;

//android:maxSdkVersion="30"

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    private static final int REQUEST_ENABLE_BT = 1;


    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler;


//    private final BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
//    private final Handler handler = new Handler();

    //Stop scanning;
    private static final long SCAN_PERIOD = 10000;


    private RecyclerView devicesRecyclerView;

    private ArrayList<BluetoothDevice> bluetoothDevices;

    private DevicesRecViewAdapter recViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicesRecyclerView = findViewById(R.id.bLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothManager = getSystemService(BluetoothManager.class);
        }
        bluetoothAdapter = bluetoothManager.getAdapter();

//        //Scanning
//        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
//        handler = new Handler();

//        bluetoothDevices = new ArrayList<>();
        recViewAdapter = new DevicesRecViewAdapter(this);
//        recViewAdapter.setBluetoothDeviceArrayList();

                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Toast.makeText(this, "ble_supported", Toast.LENGTH_LONG).show();
        }

        locationPermission();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
//                Toast.makeText(this, "Scanning...", Toast.LENGTH_SHORT).show();
                turnOnBluetooth();
                return true;
            case R.id.menu_stop:
//                Toast.makeText(this, "Stop Scanning...", Toast.LENGTH_SHORT).show();
                scanDevice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void turnOnBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BLUETOOTH_CONNECT)){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);

                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
                    startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
                    Toast.makeText(this, "enableBluetooth", Toast.LENGTH_SHORT).show();
                    System.out.println("enableBluetooth++++++++++++++++++++++++++++");
                }
//                Toast.makeText(this, "enableBluetooth", Toast.LENGTH_SHORT).show();
//                System.out.println("enableBluetooth++++++++++++++++++++++++++++");
//                startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
                return;
            }
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
            Toast.makeText(this, "enableBluetooth", Toast.LENGTH_SHORT).show();
            System.out.println("Per Given++++++++++++++++++++++++++++");
//            startActivity(enableBluetooth);
        }
    }

    private void scanDevice() {

        //Scanning
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();

//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, "ble_not_supported", Toast.LENGTH_LONG).show();
//            finish();
//        }
//        else {
//            Toast.makeText(this, "ble_supported", Toast.LENGTH_LONG).show();
//        }
        if (!scanning) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BLUETOOTH_SCAN)){
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                        }

                        else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                        }

                        Toast.makeText(MainActivity.this, "stopScan", Toast.LENGTH_SHORT).show();
                        System.out.println("stopScan++++++++++++++++++++++++++++");
//                        bluetoothLeScanner.stopScan(leScanCallback);
                        return;

                    }
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {


                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BLUETOOTH_SCAN)){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                }

                System.out.println("startScan++++++++++++++++++++++++++++");
//                bluetoothLeScanner.startScan(leScanCallback);
                return;
            }
            bluetoothLeScanner.startScan(leScanCallback);
            Toast.makeText(this, "Good Scan", Toast.LENGTH_SHORT).show();


            invalidateOptionsMenu();
        } else {
            scanning = false;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {


                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BLUETOOTH_SCAN)){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
                }
                Toast.makeText(this, "stopScan", Toast.LENGTH_SHORT).show();
                System.out.println("stopScan++++++++++++++++++++++++++++");
//                bluetoothLeScanner.stopScan(leScanCallback);
                return;
            }
            bluetoothLeScanner.stopScan(leScanCallback);
            Toast.makeText(this, "Bad Scan", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
        }
    }

    private void locationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
               ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            System.out.println("*++++++++++==================+++++++++"+errorCode);
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            System.out.println("****************************" + result);
            recViewAdapter.setBluetoothDeviceArrayList(result.getDevice());
            devicesRecyclerView.setAdapter(recViewAdapter);


        }
    };


}