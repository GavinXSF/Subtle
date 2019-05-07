package com.example.subtle;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRV;
    private List<Obj> myObj;
    private int calCount = 0;
    private long accountID = 0;

    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";



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
                if(calCount == 0){
                    addCal();
                    calCount = 1;
                }

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
//            Obj obj1 = new Obj("TestObj","Description",Null,"initDate","loop");
//            myObj.add(obj1);
        }else{
            for (String name:ObjNameList.split("--")){
                String initDate;
                String loop;
                String description;
                Obj obj = new Obj(name,
                       description = preferences.getString(name+"description",""),
                        preferences.getString(name+"Uri",""),
                        initDate = preferences.getString(name+"initDate",""),
                        loop = preferences.getString(name+"loop",""));

                String[] initDateArray = initDate.split("-");
                String[] loopArray = loop.split("-");
                String initYear = initDateArray[0];
                String initMonth = initDateArray[1];
                String initDay = initDateArray[2];
                String loopYear = loopArray[0];
                String loopMonth = loopArray[1];
                String loopDay = loopArray[2];

                String title = name;
                String dscrp = description;
                String initD = initYear + initMonth + initDay;
                int interval = (Integer.parseInt(loopYear) * 365 + Integer.parseInt(loopMonth) * 30 + Integer.parseInt(loopDay));
                myObj.add(obj);
                addEvent(title, dscrp, initD, interval);
            }
        }
    }

    /**
     * insert Cal
     * @return
     */
    private void addCal(){

        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        StringBuilder sb = new StringBuilder();
        Uri accountUri = null;


        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, "subtle");
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        contentValues.put(CalendarContract.Calendars.NAME, "Subtle");
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Subtle");
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, "232323");
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, "test_account");
        contentValues.put(CalendarContract.Calendars.ALLOWED_REMINDERS, "METHOD_ALERT, METHOD_EMAIL, METHOD_ALARM");
        contentValues.put(CalendarContract.Calendars.ALLOWED_ATTENDEE_TYPES, "TYPE_OPTIONAL, TYPE_REQUIRED, TYPE_RESOURCE");
        contentValues.put(CalendarContract.Calendars.ALLOWED_AVAILABILITY, "AVAILABILITY_BUSY, AVAILABILITY_FREE, AVAILABILITY_TENTATIVE");
        uri = uri
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "Test")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(PackageManager.PERMISSION_GRANTED == checkSelfPermission("android.permission.WRITE_CALENDAR")){
                accountUri = cr.insert(uri, contentValues);
            }else{
               // return "Please grant access to calendar.";
            }
        }else{
           // accountUri = cr.insert(uri, contentValues);
        }
        //accountID = Long.parseLong(accountUri.getLastPathSegment());
        System.out.println("ID = " + accountID);
    }





    /**
     * add Event
     *
     * @return
     */
    private void addEvent(String title, String description, String initDate, int interval){

        Uri uri = CalendarContract.Events.CONTENT_URI;
        Uri eventUri = null;
        StringBuilder sb = new StringBuilder();

        System.out.println("event!");

        ContentResolver cr = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DURATION, "PT10M");
        contentValues.put(CalendarContract.Events.RRULE, "FREQ=DAILY; INTERVAL = " + interval);
        contentValues.put(CalendarContract.Events.RDATE, initDate);
        contentValues.put(CalendarContract.Events.TITLE, title);
        contentValues.put(CalendarContract.Events.DESCRIPTION, description);
        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        contentValues.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        contentValues.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        cr.insert(Uri.parse(CALANDER_EVENT_URL), contentValues);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(PackageManager.PERMISSION_GRANTED == checkSelfPermission("android.permission.WRITE_CALENDAR")){
                eventUri = cr.insert(uri, contentValues);
            }else{
                //return "Please grant access to calendar.";
            }
        }else{
            eventUri = cr.insert(uri, contentValues);
        }
        // get the event ID that is the last element in the Uri
        //long eventID = Long.parseLong(eventUri.getLastPathSegment());
    }

}
