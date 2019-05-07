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

public class ContactActivity extends AppCompatActivity {
    private RecyclerView contactRV;
    private List<Contact> myContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        this.setTitle("Subtle - Contacts List");

//        myContact = new ArrayList<Contact>();
        initContact();
        contactRV = findViewById(R.id.contactRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contactRV.setLayoutManager(layoutManager);
        final ContactAdapter contactAdapter = new ContactAdapter(myContact);
        contactRV.setAdapter(contactAdapter);

        RecyclerViewUtil util=new RecyclerViewUtil(this,contactRV);
        util.setOnItemLongClickListener(new RecyclerViewUtil.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position, View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactActivity.this);
                dialog.setTitle("Warning");
                dialog.setMessage("Are you sure to delete this contact ?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getSharedPreferences("Contacts",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        String ContactNameList = preferences.getString("ContactNameList","");
                        int cnt = 0;
                        for (String name:ContactNameList.split("--")){
                            if (cnt == position){
                                editor.remove(name+"hobby");
                                editor.remove(name+"Uri");
                                editor.remove(name+"birthday");
                                editor.remove(name+"loop");
                                editor.remove("ContactNameList");
                                if(cnt == 0){
                                    if(contactAdapter.getItemCount()==1){
                                        ContactNameList = ContactNameList.replace(name,"");
                                    }else {
                                        ContactNameList = ContactNameList.replace(name + "--", "");
                                    }
                                }else{
                                    ContactNameList = ContactNameList.replace("--" + name,"");
                                }
                                editor.putString("ContactNameList",ContactNameList);
                            }
                            cnt ++;
                        }
                        editor.apply();
                        initContact();
                        ContactAdapter contactAdapter= new ContactAdapter(myContact);
                        contactRV.setAdapter(contactAdapter);
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



        Button addBtn = findViewById(R.id.addContact);
        addBtn.bringToFront();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

        Button objBtn = findViewById(R.id.mainPage);
        objBtn.bringToFront();
        objBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactActivity.this,MainActivity.class));
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        initContact();
        ContactAdapter contactAdapter= new ContactAdapter(myContact);
        contactRV.setAdapter(contactAdapter);
    }

    public void initContact(){
        myContact = new ArrayList<Contact>();
        SharedPreferences preferences = getSharedPreferences("Contacts",MODE_PRIVATE);
        String ContactNameList = preferences.getString("ContactNameList","");
        if(ContactNameList.equals("")){
            Contact tempContact = new Contact("No contact added","Try to add contact now! ","","YYYY-MM-DD","");
            myContact.add(tempContact);
        }else{
            for (String name:ContactNameList.split("--")){
                String birthday;
                String loop;
                Contact contact = new Contact(name,
                        preferences.getString(name+"hobby",""),
                        preferences.getString(name+"Uri",""),
                        preferences.getString(name+"birthday",""),
                        preferences.getString(name+"loop",""));
                myContact.add(contact);

                //set loop alarm
//                String[] initDateArray = initDate.split("-");
//                String[] loopArray = loop.split("-");
//
//                String initYear = initDateArray[0];
//                String initMonth = initDateArray[1];
//                String initDay = initDateArray[2];
//
//                String loopYear = loopArray[0];
//                String loopMonth = loopArray[1];
//                String loopDay = loopArray[2];
//
//                Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
//                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
//
//                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                int interval = (((Integer.parseInt(loopYear) * 12 + Integer.parseInt(loopMonth)) * 30 + Integer.parseInt(loopDay)) * 1000);
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.YEAR, Integer.parseInt(initYear));
//                calendar.set(Calendar.MONTH,Integer.parseInt(initMonth));
//                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(initDay));
//
//                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
            }
        }

    }

}
