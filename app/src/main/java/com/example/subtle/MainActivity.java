package com.example.subtle;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRV;
    private List<Obj> myObj;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//            Obj obj1 = new Obj("TestObj","Description",Null,"initDate","loop");
//            myObj.add(obj1);
        }else{
            for (String name:ObjNameList.split("--")){
                String initDate;
                String loop;
                Obj obj = new Obj(name,
                        preferences.getString(name+"description",""),
                        preferences.getString(name+"Uri",""),
                        initDate = preferences.getString(name+"initDate",""),
                        loop = preferences.getString(name+"loop",""));
                myObj.add(obj);

                //set loop alarm
                String[] initDateArray = initDate.split("-");
                String[] loopArray = loop.split("-");

                String initYear = initDateArray[0];
                String initMonth = initDateArray[1];
                String initDay = initDateArray[2];

                String loopYear = loopArray[0];
                String loopMonth = loopArray[1];
                String loopDay = loopArray[2];

                Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                int interval = (((Integer.parseInt(loopYear) * 12 + Integer.parseInt(loopMonth)) * 30 + Integer.parseInt(loopDay)) * 24 * 60 * 60 * 1000);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.YEAR, Integer.parseInt(initYear));
                calendar.set(Calendar.MONTH,Integer.parseInt(initMonth));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(initDay));

                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
            }
        }

    }
}
