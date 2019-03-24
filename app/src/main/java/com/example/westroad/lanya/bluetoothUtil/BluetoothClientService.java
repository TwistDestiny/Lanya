package com.example.westroad.lanya.bluetoothUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * ?????????????????Service
 * @author GuoDong
 *
 */
public class BluetoothClientService extends Service {
	
	//?????????????????
	private List<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	
	//??????????
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	//?????????
	private BluetoothCommunThread communThread;
	
	//????????????????
	private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_START_DISCOVERY.equals(action)) {
				//???????
				discoveredDevices.clear();	//????????????
				bluetoothAdapter.enable();	//??????
				bluetoothAdapter.startDiscovery();	//???????
				Log.d("connect","dddd6");
			} else if (BluetoothTools.ACTION_SELECTED_DEVICE.equals(action)) {
				//?????????????????
				BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothTools.DEVICE);
				
				//?????????????
				new BluetoothClientConnThread(handler, device).start();
			} else if (BluetoothTools.ACTION_STOP_SERVICE.equals(action)) {
				//?????????
				if (communThread != null) {
					communThread.isRun = false;
				}
				stopSelf();
				
			} else if (BluetoothTools.ACTION_DATA_TO_SERVICE.equals(action)) {
				//???????
				TransmitBean data = (TransmitBean)intent.getExtras().getSerializable(BluetoothTools.DATA);
				if (communThread != null) {
					communThread.write(data.getMsg().getBytes());
				}
				
			}
		}
	};
	
	//?????????????????
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//???????Action
			String action = intent.getAction();

			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				//???????
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				//?????????????
				//?????
				BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				discoveredDevices.add(bluetoothDevice);

				//???????????
				Intent deviceListIntent = new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
				deviceListIntent.putExtra(BluetoothTools.DEVICE, bluetoothDevice);
				sendBroadcast(deviceListIntent);
				
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				//????????
				if (discoveredDevices.isEmpty()) {
					//??????????????????????
					Intent foundIntent = new Intent(BluetoothTools.ACTION_NOT_FOUND_SERVER);
					sendBroadcast(foundIntent);
				}
			}
		}
	};
	
	//????????????????Handler
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//???????
			switch (msg.what) {
			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				//???????
				//????????????
				Intent errorIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				break;
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				//??????
				//?????????
				communThread = new BluetoothCommunThread(handler, (BluetoothSocket)msg.obj);
				communThread.start();
				
				//????????????
				Intent succIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(succIntent);
				break;
			case BluetoothTools.MESSAGE_READ_OBJECT:
				//?????????
				//??????????????????????
				TransmitBean data = new TransmitBean();
				Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_GAME);
				data.setMsg((String) msg.obj);
				sendDataIntent.putExtra(BluetoothTools.DATA1, data);
				sendBroadcast(sendDataIntent);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	/**
	 * ????????
	 * @return
	 */
	public BluetoothCommunThread getBluetoothCommunThread() {
		return communThread;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	/**
	 * Service?????????????
	 */
	@Override
	public void onCreate() {
		//discoveryReceiver??IntentFilter
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		Log.d("connect","dddd1");
		//controlReceiver??IntentFilter
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
		controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
		controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
		controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
		
		//???BroadcastReceiver
		registerReceiver(discoveryReceiver, discoveryFilter);
		registerReceiver(controlReceiver, controlFilter);
		super.onCreate();
	}
	
	/**
	 * Service?????????????
	 */
	//@Override
	public void onDestroy() {
		if (communThread != null) {
			communThread.isRun = false;
		}
		//?????
		unregisterReceiver(discoveryReceiver);
		unregisterReceiver(controlReceiver);
		super.onDestroy();
	}

}
