package com.example.westroad.lanya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.westroad.lanya.widge.Linechart;

public class MessageItemActivity extends AppCompatActivity {
    TextView messageItem,addmsg;
    ImageView imageView;
    Linechart linechart;

    int s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_item);
        imageView= (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        addmsg= (TextView) findViewById(R.id.addmsg);
        View view =findViewById(R.id.line_chart);
        linechart =new Linechart(view);
        Intent intent = getIntent();
        s= intent.getIntExtra("Name",1);
        messageItem= (TextView) findViewById(R.id.messageItem);
        switch(s){
            case R.id.usermsg:messageItem.setText("测试用账号\n账号：1000000\n昵称：hxws982nhjd");break;
            case R.id.fewtips:imageView.setVisibility(View.VISIBLE);messageItem.setText(getString(R.string.TIPS));break;
            case R.id.help:messageItem.setText(getString(R.string.help));break;
            case R.id.presschange:messageItem.setText("开发中...");
        }
    }

    @Override
    protected void onResume() {
        linechart.lineChart.setVisibility(View.GONE);
        addmsg.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        if(s==R.id.fewtips||s==R.id.help){
            imageView.setVisibility(View.VISIBLE);

        }
        if(s==R.id.presschange){
            imageView.setVisibility(View.GONE);
            messageItem.setVisibility(View.GONE);
            linechart.lineChart.setVisibility(View.VISIBLE);
            addmsg.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
}
