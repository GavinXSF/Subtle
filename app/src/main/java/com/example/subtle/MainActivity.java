package com.example.subtle;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRV;
    private List<Obj> myObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myObj = new ArrayList<Obj>();
        initObj();
        myRV = findViewById(R.id.myRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myRV.setLayoutManager(layoutManager);
        ObjAdapter objAdapter = new ObjAdapter(myObj);
        myRV.setAdapter(objAdapter);

        Button addBtn = findViewById(R.id.add);
        addBtn.bringToFront();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddObjActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        initObj();
        ObjAdapter objAdapter = new ObjAdapter(myObj);
        myRV.setAdapter(objAdapter);
    }

    public void initObj(){
        myObj = new ArrayList<Obj>();
        SharedPreferences preferences = getSharedPreferences("Objects",MODE_PRIVATE);
        String ObjNameList = preferences.getString("ObjNameList","");
        if(ObjNameList.equals("")){
            Obj obj1 = new Obj("TestObj","Description",R.drawable.add,"initDate","loop");
            myObj.add(obj1);
        }else{
            for (String name:ObjNameList.split("--")){
                Obj obj = new Obj(name,
                        preferences.getString(name+"description",""),
                        R.drawable.add,
                        preferences.getString(name+"initDate",""),
                        preferences.getString(name+"loop",""));
                myObj.add(obj);
            }
        }

    }
}