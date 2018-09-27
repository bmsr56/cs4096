package com.example.nathan.automaticalcohol.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.BluetoothSupport;
import com.example.nathan.automaticalcohol.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.automaticalcohol.MESSAGE";
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothSocket bluetoothSocket = null;
    private BluetoothSupport bluetoothSupport;           // bluetooth - sends data back and fourth

    private BluetoothSupport.ConnectedThread a = null;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //device does not support bluetooth
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


//
//
//
//
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("B8:27:EB:C7:30:39");
//
//        boolean fail = false;
//
//        try {
//            bluetoothSocket = createBluetoothSocket(device);
//        } catch (IOException e) {
//            fail = true;
//            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
//        }
//
//        try {
//            bluetoothSocket.connect();
//        } catch (IOException e) {
//            try {
//                fail = true;
//                bluetoothSocket.close();
////                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
//            } catch (IOException e2) {
//                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (!fail) {
//
//            bluetoothSupport = new BluetoothSupport();
//            BluetoothSupport.ConnectedThread a = bluetoothSupport.new ConnectedThread(bluetoothSocket);
//            a.start();
//
//
//
//        }
//

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        Button login_button = findViewById(R.id.email_sign_in_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = email.getText().toString();
                String password_text = password.getText().toString();

                a = bluetoothSupport.new ConnectedThread(bluetoothSocket);

                a.write("login+"+email_text+"_"+password_text);

                a.run();
            }
        });

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, uuid);
        } catch (Exception e) {
            Log.e("createBluetoothSocket", "Could not create Insecure RFComm Connection", e);
        }
        return device.createInsecureRfcommSocketToServiceRecord(uuid);
    }

}
