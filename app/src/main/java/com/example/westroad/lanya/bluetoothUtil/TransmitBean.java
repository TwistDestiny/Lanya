package com.example.westroad.lanya.bluetoothUtil;

import java.io.Serializable;

import android.R.string;
import android.os.Bundle;
import android.os.Handler;
import android.os.MessageQueue;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.TimeUtils;

/**
 * ??????????????
 * @author GuoDong
 *
 */
public class TransmitBean implements Serializable {

	private String msg ;
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
}


