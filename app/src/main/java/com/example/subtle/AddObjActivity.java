package com.example.subtle;

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

public class AddObjActivity extends AppCompatActivity {
//    private String name, start_year,start_month,start_day,loop_year,loop_month,loop_day,description;
    private String name, loop, initDate, description, imgUri="";
    private int imgID;

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView picture_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_obj);
        this.setTitle("Add new object");

        final EditText name_input = findViewById(R.id.name_input);
        final EditText description_input = findViewById(R.id.description_input);
        final Spinner initYear = findViewById(R.id.initDate_year);
        final Spinner initMonth = findViewById(R.id.initDate_month);
        final Spinner initDay = findViewById(R.id.initDate_day);
        final Spinner loopYear = findViewById(R.id.loop_year);
        final Spinner loopMonth = findViewById(R.id.loop_month);
        final Spinner loopDay = findViewById(R.id.loop_day);

        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_input.getText().toString();
                description = description_input.getText().toString();
                initDate = initYear.getSelectedItem().toString()+"-"+initMonth.getSelectedItem().toString()+"-"+initDay.getSelectedItem().toString();
                loop = loopYear.getSelectedItem().toString()+"-"+loopMonth.getSelectedItem().toString()+"-"+loopDay.getSelectedItem().toString();

                SharedPreferences pref = getSharedPreferences("Objects",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String ObjNameList = pref.getString("ObjNameList","");
                if(ObjNameList.equals("")){
                    ObjNameList = name;
                }else{
                    ObjNameList += "--" + name;
                }
                editor.putString(name+"description",description);
                editor.putString(name+"initDate",initDate);
                editor.putString(name+"loop",loop);
                editor.remove("ObjNameList");
                editor.putString("ObjNameList",ObjNameList);
                if(!imgUri.equals("")){
                    editor.putString(name+"Uri",imgUri);
                }
                editor.apply();

                Toast.makeText(getBaseContext(),"New object stored",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddObjActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });



        //picture from user gallery
        picture_input = (ImageView) findViewById(R.id.picture_input);

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
