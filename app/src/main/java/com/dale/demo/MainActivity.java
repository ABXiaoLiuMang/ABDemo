package com.dale.demo;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cn.div.util.LiveDataEventBus;
import com.dale.data.Item;
import com.dale.data.Person;
import com.dale.dialog.BaseDialog;

public class MainActivity extends AppCompatActivity {

    BaseDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basetest);

//         dialog = new CustomDialog.Builder(this).create();
//
//
//        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.showDialog();
//            }
//        });
//
//        findViewById(R.id.tv_dismiss).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismissDialog();
//            }
//        });



        LiveDataEventBus.with(String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(MainActivity.this,"string:"+s,Toast.LENGTH_LONG).show();
            }
        });

        LiveDataEventBus.with(Item.class).observe(this, new Observer<Item>() {
            @Override
            public void onChanged(@Nullable Item s) {
                Toast.makeText(MainActivity.this,"Item:"+s.toString(),Toast.LENGTH_LONG).show();
            }
        });

        LiveDataEventBus.with(Person.class).observe(this, new Observer<Person>() {
            @Override
            public void onChanged(@Nullable Person s) {
                Toast.makeText(MainActivity.this,"Person:"+s.toString(),Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataEventBus.with(String.class).setValue("nimei");
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setHead("狗头");
                LiveDataEventBus.with(Item.class).setValue(item);
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person person = new Person();
                person.setAge(18);
                person.setName("亚麻跌");
                LiveDataEventBus.with(Person.class).setValue(person);
            }
        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataEventBus.removeAll();
            }
        });

        LiveData<String> liveData = new MutableLiveData();
//        LiveData<Person> liveData1 = new MutableLiveData();
//        MediatorLiveData liveDataMerger = new MediatorLiveData<>();
//        liveDataMerger.addSource(liveData1, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//
//            }
//        });
//        liveDataMerger.addSource(liveData, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String o) {
//
//            }
//        });
//
//
        LiveData<Person> userName = Transformations.map(liveData, new Function<String, Person>() {

            @Override
            public Person apply(String input) {
                return null;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveDataEventBus.removeAll();
    }
}
