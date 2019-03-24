package com.example.westroad.lanya.widge;

import android.util.Log;

//(Math.random()*0.2+1)*45
public class pressSelect {

	
	public static int getAppropriatePressure(int[] recentPressData,int currentPress) {
		Log.d("count1","recentPressData:"+recentPressData[1]);
		int zerotime=0;
		int onetime=0;
		int twotime=0;
		int[] getValue=new int[10];
		for(int j=0;j<10;j++){
		int sum=0;
		int fangcha=0;
		int avg=0;
		for(int i=0;i<10;i++){
		getValue[i]=(int)((Math.random()*0.2+1)*recentPressData[i+j*10]);
		sum+=getValue[i];
		}
		avg=sum/10;
		
		for(int i=0;i<10;i++){
		fangcha+=(getValue[i]-avg)*(getValue[i]-avg);
		}
		fangcha=fangcha/10;		
		if(fangcha>=0&&fangcha<=3)
			zerotime++;
		else if(fangcha>=4&&fangcha<=7)
			onetime++;
		else twotime++;

		System.out.println(fangcha);
		
	}
		System.out.println(zerotime+" "+onetime+" "+twotime+" ");
		Log.d("count1",zerotime+"zerotime"+onetime+"onetime"+twotime+"twotime");
//		int sumtime=zerotime;
//		sumtime+=onetime;
//		sumtime+=twotime;
		switch(currentPress){
			case 45:
				if(zerotime==0) return 50;//模拟公路
			case 50:
				if(zerotime!=0)return 45;
				if(zerotime==0&&twotime>=onetime) return 55;//模拟山路
			case 55:
				if(zerotime!=0)return 45;
				if(zerotime==0&&onetime>=twotime) return 50;//模拟泥泞路
		}
		return currentPress;
	}
	

}
