package com.example.subtle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRV;
    private List<Obj> myObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Subtle - Objects List");

        //display objects
        myObj = new ArrayList<Obj>();
        initObj();
        myRV = findViewById(R.id.myRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myRV.setLayoutManager(layoutManager);
        final ObjAdapter objAdapter = new ObjAdapter(myObj);
        myRV.setAdapter(objAdapter);

        RecyclerViewUtil util=new RecyclerViewUtil(this,myRV);
        util.setOnItemLongClickListener(new RecyclerViewUtil.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position, View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Warning");
                dialog.setMessage("Are you sure to delete this object ?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getSharedPreferences("Objects",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        String ObjNameList = preferences.getString("ObjNameList","");
                        int cnt = 0;
                        for (String name:ObjNameList.split("--")){
                            if (cnt == position){
                                editor.remove(name+"description");
                                editor.remove(name+"Uri");
                                editor.remove(name+"initDate");
                                editor.remove(name+"loop");
                                editor.remove("ObjNameList");
                                if(cnt == 0){
                                    if(objAdapter.getItemCount()==1){
                                        ObjNameList = ObjNameList.replace(name,"");
                                    }else {
                                        ObjNameList = ObjNameList.replace(name + "--", "");
                                    }
                                }else{
                                    ObjNameList = ObjNameList.replace("--" + name,"");
                                }
                                editor.putString("ObjNameList",ObjNameList);
                            }
                            cnt ++;
                        }
                        editor.apply();
                        initObj();
                        ObjAdapter objAdapter = new ObjAdapter(myObj);
                        myRV.setAdapter(objAdapter);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
//                Toast.makeText(getApplicationContext(),position+" 长按",Toast.LENGTH_SHORT).show();
            }
        });


        Button addBtn = findViewById(R.id.add);
        addBtn.bringToFront();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddObjActivity.class);
                startActivity(intent);
            }
        });

        Button contactBtn = findViewById(R.id.contact);
        contactBtn.bringToFront();
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
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
            Obj obj1 = new Obj("No object!","Add detailed description here!","","initDate","");
            myObj.add(obj1);
        }else{
            String[] objNames = ObjNameList.split("--");

                for (String name:objNames){
                    String initDate;
                    String loop;
                    Obj obj = new Obj(name,
                            preferences.getString(name+"description",""),
                            preferences.getString(name+"Uri",""),
                            initDate = preferences.getString(name+"initDate",""),
                            loop = preferences.getString(name+"loop",""));
                    myObj.add(obj);

            }
        }

    }

}
