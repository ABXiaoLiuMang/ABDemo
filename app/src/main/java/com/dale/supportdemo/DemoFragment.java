package com.dale.supportdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dale.demo.R;

/**
 * 文件描述:
 * 作者Dale:2019/4/28
 */
public class DemoFragment extends  BaseSupportFragment{

    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_demo,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView.findViewById(R.id.btn_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dream","DemoFragment onViewCreated");
                startFragment(new SecondFragment());
            }
        });

        ((Button)rootView.findViewById(R.id.btn_demo)).setText("DemoFragment");
        setBackActivity(true);
    }

}
