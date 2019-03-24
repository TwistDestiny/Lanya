package com.example.westroad.lanya;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.westroad.lanya.bluetoothUtil.BluetoothClientService;
import com.example.westroad.lanya.bluetoothUtil.BluetoothTools;
import com.example.westroad.lanya.bluetoothUtil.TransmitBean;
import com.example.westroad.lanya.fragments.FragmentController;
import com.example.westroad.lanya.widge.pressSelect;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public int[] data = new int[100];
    int count=0;
    public int setPress=0;
    public static FragmentController controller;
    public static long StartTime;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_live:;
                    controller.showFragment(0);
                    return true;
                case R.id.navigation_record:
                    controller.showFragment(1);
                    return true;
                case R.id.navigation_user:
                    controller.showFragment(2);
                    return true;
            }
            return false;
        }
    };
    public   List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
//    private List<String> deviceListName = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
            } else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
                BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
                if(!deviceList.contains(device)){
                    deviceList.add(device);
                    controller.serviceFragment.DateChanged(deviceList);

                }
            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                controller.serviceFragment.connected(true);
                StartTime=System.currentTimeMillis();
                Toast.makeText(getApplication(),"ACTION_CONNECT_SUCCESS",Toast.LENGTH_SHORT).show();
                Log.d("connect","CONNECT_SUCCESS");
            } else if (BluetoothTools.ACTION_CONNECT_ERROR.equals(action)) {
                controller.serviceFragment.connected(false);
                Log.d("connect","ACTION_CONNECT_ERROR");

            } else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
                //????????
                TransmitBean data1 = (TransmitBean)intent.getExtras().getSerializable(BluetoothTools.DATA1);
                String current;
                int asc = (int)data1.getMsg().charAt(0);
                if(asc>9){
                    current = ""+asc;
                }
                else{
                    current = "0"+asc;
                }
                controller.manageFragment.currentPress(current,asc);
                data[count]=asc;
                count++;Log.d("count1","count1:"+current);
                Log.d("count1","setPress:"+setPress);
                if(count==100){
                    count=0;
                    int newPress;
                    newPress= pressSelect.getAppropriatePressure(data,setPress);
                    Log.d("count1","ssssr");
                    if(newPress!=setPress){
                        Log.d("count1","rrrr");
                        setPress=newPress;
                        mListener.onSetPresChanged(newPress);
                    }
                }

            }
        }

    };
    public interface pressChangeListener {
        public void onSetPresChanged(int newpress);
    }

    private pressChangeListener mListener;
    public void setPressChangeListener(pressChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        controller = FragmentController.getInstance(this,R.id.content);
        controller.showFragment(0);
        checkBluetoothPermission();
    }

    @Override
    protected void onResume() {
        deviceList.clear();
        //???????service
        Intent startService = new Intent(MainActivity.this, BluetoothClientService.class);
        startService(startService);

        //???BoradcasrReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
        intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
        intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        registerReceiver(broadcastReceiver, intentFilter);
        Log.d("connect","sde");

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


    /*
        校验蓝牙权限
       */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        }
    }
}
