package com.example.westroad.lanya.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.westroad.lanya.MainActivity;
import com.example.westroad.lanya.R;
import com.example.westroad.lanya.bluetoothUtil.BluetoothTools;
import com.example.westroad.lanya.bluetoothUtil.TransmitBean;
import com.example.westroad.lanya.widge.Linechart;
import com.example.westroad.lanya.widge.NewSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.westroad.lanya.MainActivity.StartTime;
import static com.example.westroad.lanya.widge.Linechart.pressMsg;

/**
 * Created by 北行_yangyimin on 2018/2/24.
 */

public class ManageFragment extends Fragment {
    @Nullable
    SeekBar testbar;
    TextView tips;
    MainActivity mainActivity;
    TextView setpre;
    TextView  currpre;
    NewSeekBar mySeekbar;
    Spinner roadstate;
    Switch smartControl;
    boolean Checked=false;
    String[] arr={"公路","山路","泥泞路"};
    HashMap<String,Integer> road=new HashMap();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        testbar= (SeekBar) view.findViewById(R.id.testbar);
        setpre= (TextView) view.findViewById(R.id.setrpre);
        currpre=(TextView) view.findViewById(R.id.currpre);
        tips= (TextView) view.findViewById(R.id.tips);
        mySeekbar= (NewSeekBar) view.findViewById(R.id.mySeekbar);
        testbar.setOnSeekBarChangeListener(onseekbarchangedlistener);
        roadstate= (Spinner) view.findViewById(R.id.roadstate);
        mainActivity=(MainActivity) getActivity();

        road.put("公路",45);
        road.put("山路",50);
        road.put("泥泞路",55);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,arr);
        roadstate.setAdapter(adapter);
        roadstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setpre.setText("设定值 "+road.get(arr[position])+" PSI");

                Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
                TransmitBean data = new TransmitBean();
                int t=road.get(arr[position]);
                mainActivity.setPress=t;
                data.setMsg((char)t+"");

                sendDataIntent.putExtra(BluetoothTools.DATA, data);
                getActivity().sendBroadcast(sendDataIntent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        smartControl= (Switch) view.findViewById(R.id.smartControl);
        smartControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Checked=isChecked;
                Log.d("Checked", Checked+"");
                testbar.setEnabled(!Checked);
            }
        });
        testbar.setMax(60);
        mySeekbar.setMaxProgress(60);
        testbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return Checked;
            }
        });
        Resources resources =getResources();
        mainActivity.setPressChangeListener(new MainActivity.pressChangeListener(){

            @Override
            public void onSetPresChanged(int newpress) {
                if(Checked){
                    setpre.setText("设定值 "+newpress+" PSI");
                    Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
                    TransmitBean data = new TransmitBean();
                    Log.d("count1","dddd");
                    data.setMsg((char)newpress+"");
                    sendDataIntent.putExtra(BluetoothTools.DATA, data);
                    getActivity().sendBroadcast(sendDataIntent);
                    switch(newpress){
                        case 45 :roadstate.setSelection(0);break;
                        case 50 :roadstate.setSelection(1);break;
                        case 55 :roadstate.setSelection(2);break;
                    }
                }
            }
        });

        tips.setText(resources.getStringArray(R.array.Adivse)[1]);
        return view;
    }
    private SeekBar.OnSeekBarChangeListener onseekbarchangedlistener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setpre.setText("设定值 "+progress+" PSI");

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress=seekBar.getProgress();

            Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
            TransmitBean data = new TransmitBean();
            data.setMsg((char)progress+"");

            sendDataIntent.putExtra(BluetoothTools.DATA, data);
            getActivity().sendBroadcast(sendDataIntent);
        }
    };
    public void currentPress(String press,int progress){
        currpre.setText(press);
        mySeekbar.setProgress(progress);
        long nowTime = System.currentTimeMillis();
        int t = (int)(nowTime-StartTime)/1000;
        int i =t%10;
        int j=t/10;
        if(j< pressMsg.length&&i<3)
            pressMsg[pressMsg.length-j-1]=progress;
        Resources res =getResources();
// 取xml文件格式的字符数组
        String[] Adivse=res.getStringArray(R.array.Adivse);
        if(progress<25)
            tips.setText(Adivse[0]);
        else if(progress<37)
            tips.setText(Adivse[1]);
        else if(progress<51)
            tips.setText(Adivse[2]);
        else
            tips.setText(Adivse[3]);
        Log.d("edd",Adivse[0]);
    }

}
