package com.example.westroad.lanya.bluetoothUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
	
/**
 * ?????????
 * @author GuoDong
 *
 */

public class BluetoothCommunThread extends Thread {

	private Handler serviceHandler;		//??Service????Handler
	private BluetoothSocket socket;
	private InputStream inStream=null;		//??????
	private OutputStream outStream=null;	//?????
	public volatile boolean isRun = true;	//?????????
	public String smsg = "";    //????????????
	public String fmsg = "";    //?????????????


	/**
	 * ??????
	 * @param handler ??????????
	 * @param socket
	 */
	public BluetoothCommunThread(Handler handler, BluetoothSocket socket) {
		this.serviceHandler = handler;
		this.socket = socket;
		try {
			this.outStream = socket.getOutputStream();
			this.inStream = socket.getInputStream();
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//??????????????
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int num = 0;
		byte[] buffer = new byte[1024];
		byte[] buffer_new = new byte[1024];
		int i = 0;
		int n = 0;
		while (true) {
			if (!isRun) {
				break;
			}
			try {
				while(inStream.available()==0){
				}
				fmsg="";
				smsg="";
				while(true){
					num = inStream.read(buffer);         //????????
					n=0;

					String s0 = new String(buffer,0,num);
					fmsg=s0;    //???????????
					for(i=0;i<num;i++){
						if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
							buffer_new[n] = 0x0a;
							i++;
						}else{
							buffer_new[n] = buffer[i];
						}
						n++;
					}
					String s = new String(buffer_new,0,n);
					smsg+=s;   //??????????
					if(inStream.available()==0)break;  //????????????????????????
				}
				//??????????????????????????obj???????????????
				Message msg = serviceHandler.obtainMessage();
				msg.what = BluetoothTools.MESSAGE_READ_OBJECT;
				msg.obj = fmsg;
				msg.sendToTarget();
			} catch (Exception ex) {
				//??????????????
				serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
				ex.printStackTrace();
				return;
			}
		}

		//?????
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * ????????????????????
	 * @param obj
	 */
	public void write(byte[] obj) {
		try {
			outStream.flush();
			outStream.write(obj);
			outStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
