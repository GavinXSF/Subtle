package com.example.subtle;

import android.Manifest;
import android.content.Intent;
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

    public void initObj(){
        Obj obj1 = new Obj("Name","Description",R.drawable.add,"initDate","loop");
        Obj obj2 = new Obj("Name2","Description",R.drawable.add,"initDate","loop");
        myObj.add(obj1);
        myObj.add(obj2);

    }
}