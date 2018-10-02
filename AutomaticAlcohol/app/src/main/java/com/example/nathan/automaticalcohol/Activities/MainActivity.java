package com.example.nathan.automaticalcohol.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.BluetoothConnect;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;

public class MainActivity extends AppCompatActivity {
    private static final String ACCEPT = "accept";
    public static final String EXTRA_MESSAGE = "com.example.automaticalcohol.MESSAGE";

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;

    private EditText email;
    private EditText password;

    private BluetoothConnect bluetoothConnect;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_WRITE:
                    Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    if ("bartender".equals(writeMessage.substring(0, msg.arg1))) {
                        Intent intent = new Intent(MainActivity.this, BartenderActivity.class);
                        startActivity(intent);
                        bluetoothConnect.close();

                    } else if ("user".equals(writeMessage.substring(0, msg.arg1))) {
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent);
                        bluetoothConnect.close();

                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //device does not support bluetooth
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
        }

        bluetoothConnect = new BluetoothConnect(mHandler);
        connectDevice();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button login_button = findViewById(R.id.email_sign_in_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if bluetooth is not enabled, enable it then
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    Toast.makeText(MainActivity.this, "Bluetooth must be on", Toast.LENGTH_LONG).show();
                } else {
                    // bluetooth is enabled
                    String email_text = email.getText().toString();
                    String password_text = password.getText().toString();

                    TextView textView = findViewById(R.id.test_textView);
                    textView.setText("login+"+email_text+"_"+password_text);

                    sendMessage("login+"+email_text+"_"+password_text);
                }
            }
        });
    }

    private void sendMessage(String message) {
        if (message.length() > 0) {
            byte[] data = message.getBytes();
            bluetoothConnect.write(data);
        }
    }

    private void connectDevice() {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("B8:27:EB:C7:30:39");
        bluetoothConnect.connect(device);
    }
}
