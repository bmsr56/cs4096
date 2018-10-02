package com.example.nathan.automaticalcohol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnect {
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "BLUETOOTH_CONNECT";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;

    private Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    public BluetoothConnect(Handler handler) {
        // MIGHT NEED TO PASS AN ACTIVITY IN HERE TOO

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevice = mBluetoothAdapter.getRemoteDevice("B8:27:EB:C7:30:39");
        mHandler = handler;
    }

    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
//            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    public synchronized void close() {
        ConnectThread r;
        synchronized (this) {
            r = mConnectThread;
        }
        r.cancel();
    }

    private synchronized void manageMyConnectedSocket(BluetoothSocket socket) {
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
    }

    private class ConnectThread extends Thread {
        private static final String TAG = "Connect_Thread";

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;


        public ConnectThread(BluetoothDevice device) {
            // use temp obj that is later assigned to mmSocket because mmSocket is final
            BluetoothSocket temp = null;
            mmDevice = device;

            try {
                // get a BluetoothSocket to connect with the given BluetoothDevice
                // uuid is the app's UUID string, also used in the server (pi) code
                temp = device.createInsecureRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = temp;
        }

        public void run() {
            // cancel discovery because it slows down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // connect to the remote device through the socket
                // This call blocks until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // unable to connect; close socket and return
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }

            // connection attempt succeeded
            // perform work associated with the connection in a separate thread
            manageMyConnectedSocket(mmSocket);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final static String TAG = "Connected_Thread";
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;
        private byte[] mmBuffer;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tempIn = null;
            OutputStream  tempOut = null;

            // get the input and output streams
            // member streams final -> use temp values
            try {
                tempIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating Input Stream", e);
            }
            try {
                tempOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating Output Stream", e);
            }

            mmInputStream = tempIn;
            mmOutputStream = tempOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes;  // bytes returned from read()

            // keep listening to the input stream until an exception occurs
            while (true) {
                try {
                    // read from the input stream
                    numBytes = mmInputStream.read(mmBuffer);
                    // send the obtained bytes to the UI Activity
                    Message readMsg = mHandler.obtainMessage(
                            Constants.MESSAGE_READ, numBytes, -1, mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // call this from the main activity to send data to the remote device
        public void write(byte[] data) {
            try {
                mmOutputStream.write(data);

                // share send message with UI Activity
                // TODO: let the user know their drink has been ordered
                Message writtenMsg = mHandler.obtainMessage(
                        Constants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // send failure message back to activity
                Message writeErrorMsg = mHandler.obtainMessage(
                        Constants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                // TODO: this is like "Error placing order"
                bundle.putString("toast", "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }

        // call this method from Main Activity to shut down the connection
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connected socket");
            }
        }
    }

}
