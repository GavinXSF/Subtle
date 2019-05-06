package com.example.subtle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AddContactActivity extends AppCompatActivity {
    //    private String name, start_year,start_month,start_day,loop_year,loop_month,loop_day,description;
    private String name, loop, birthday, hobby, imgUri="";

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView picture_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        this.setTitle("Add new contact");

        final EditText name_input = findViewById(R.id.contact_name_input);
        final EditText hobby_input = findViewById(R.id.hobby_input);
        final Spinner birthYear = findViewById(R.id.birthday_year);
        final Spinner birthMonth = findViewById(R.id.birthday_month);
        final Spinner birthDay = findViewById(R.id.birthday_day);
        final Spinner loopYear = findViewById(R.id.contact_loop_year);
        final Spinner loopMonth = findViewById(R.id.contact_loop_month);
        final Spinner loopDay = findViewById(R.id.contact_loop_day);

        Button btn_confirm = findViewById(R.id.contact_btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_input.getText().toString();
                hobby = hobby_input.getText().toString();
                birthday = birthYear.getSelectedItem().toString()+"-"+birthMonth.getSelectedItem().toString()+"-"+birthDay.getSelectedItem().toString();
                loop = loopYear.getSelectedItem().toString()+"-"+loopMonth.getSelectedItem().toString()+"-"+loopDay.getSelectedItem().toString();

                SharedPreferences pref = getSharedPreferences("Contacts",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String ContactNameList = pref.getString("ContactNameList","");
                if(ContactNameList.equals("")){
                    ContactNameList = name;
                }else{
                    ContactNameList += "--" + name;
                }
                editor.putString(name+"hobby",hobby);
                editor.putString(name+"birthday",birthday);
                editor.putString(name+"loop",loop);
                editor.remove("ContactNameList");
                editor.putString("ContactNameList",ContactNameList);
                if(!imgUri.equals("")){
                    editor.putString(name+"Uri",imgUri);
                }
                editor.apply();

                Toast.makeText(getBaseContext(),"New contact stored",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddContactActivity.this,ContactActivity.class);
                startActivity(intent);
            }
        });



        //picture from user gallery
        picture_input = findViewById(R.id.contact_picture_input);

        picture_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.picture_input:
                        Intent galleryIntent = new Intent(Intent. ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            picture_input.setImageURI(selectedImage);
            imgUri = selectedImage.toString();
        }
    }

}
