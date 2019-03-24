package com.example.westroad.lanya.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.westroad.lanya.MainActivity;
import com.example.westroad.lanya.R;
import com.example.westroad.lanya.bluetoothUtil.BluetoothTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 北行_yangyimin on 2018/2/24.
 */

public class ServiceFragment extends Fragment {
    @Nullable
    ListView testlist;
    int connectId=0;
    private LinearLayout search;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String,Object>> servicees = new ArrayList<HashMap<String,Object>>();
    String[] test = {"service0","service1","service2"};
    String[] isConnected = {"未连接","未连接","未连接"};
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        mainActivity=(MainActivity)getActivity();
        testlist= (ListView) view.findViewById(R.id.testlist);
        setAdapter();
        search= (LinearLayout) view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent startSearchIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
                getActivity().sendBroadcast(startSearchIntent);
                Log.d("connect","ddd");
            }
        });
        testlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                connectId=position;
//                if(test[position].equals("service"+position))
//                    return;
                Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);
                selectDeviceIntent.putExtra(BluetoothTools.DEVICE, mainActivity.deviceList.get(position));
                getActivity().sendBroadcast(selectDeviceIntent);
                HashMap<String,Object> m=servicees.get(position);
               m.remove("connectState");
                m.put("connectState","连接中");
                servicees.remove(position);
                servicees.add(position,m);
                simpleAdapter.notifyDataSetChanged();

            }
        });

        return view;
    }
    void setAdapter(){
        for(int i=0 ;i<test.length;i++){
            HashMap<String,Object> m = new HashMap();
            m.put("serviceName",test[i]);
            m.put("connectState",isConnected[i]);
            servicees.add(m);
        }
        simpleAdapter = new SimpleAdapter(getContext(),servicees,R.layout.layout_item,new String[]{"serviceName","connectState"}, /*传入上面定义的键值对的键名称,会自动根据传入的键找到对应的值*/
                new int[]{R.id.serviceName,R.id.connectState});

        testlist.setAdapter(simpleAdapter);
    }
    public void DateChanged(List<BluetoothDevice> deviceList){
        servicees.clear();
        simpleAdapter.notifyDataSetChanged();
        for(int i=0 ;i<deviceList.size();i++){
            HashMap<String,Object> m = new HashMap();
           String Name ="S"+deviceList.get(i).getName();
            if(Name.trim().equals("S")){
                Name=deviceList.get(i).getAddress();
            }
            else{
                Name=Name.substring(1);
            }
            m.put("serviceName",Name);
            m.put("connectState","未连接");
            servicees.add(m);
        }
        simpleAdapter.notifyDataSetChanged();
    }
   public void  connected(boolean isSuccess){
        HashMap<String,Object> m=servicees.get(connectId);
        m.remove("connectState");
       if(isSuccess){
           m.put("connectState","已连接");
       }
       else{
           m.put("connectState","连接失败");
       }
        servicees.remove(connectId);
        servicees.add(connectId,m);
        simpleAdapter.notifyDataSetChanged();
    }


}
