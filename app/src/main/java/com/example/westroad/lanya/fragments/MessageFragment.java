package com.example.westroad.lanya.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.westroad.lanya.MessageItemActivity;
import com.example.westroad.lanya.R;


/**
 * Created by 北行_yangyimin on 2018/2/24.
 */

public class MessageFragment extends Fragment {
    @Nullable
    LinearLayout usermsg,help,presschange,fewtips;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("Name",v.getId());
            intent.setClass(getActivity(), MessageItemActivity.class);
            startActivity(intent);
          //  getActivity().finish();
        }
    };
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        usermsg = (LinearLayout) view.findViewById(R.id.usermsg);
        help= (LinearLayout) view.findViewById(R.id.help);
        presschange= (LinearLayout) view.findViewById(R.id.presschange);
        fewtips= (LinearLayout) view.findViewById(R.id.fewtips);
        usermsg.setOnClickListener(onClickListener);
        help.setOnClickListener(onClickListener);
        presschange.setOnClickListener(onClickListener);
        fewtips.setOnClickListener(onClickListener);

        return view;
    }
}
