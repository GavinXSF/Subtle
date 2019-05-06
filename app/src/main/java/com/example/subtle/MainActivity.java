package com.example.subtle;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView myRV;
    private List<Obj> myObj;
    
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView picture_input;
    
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
        
        //picture from user gallery
        setContentView(R.layout.activity_add_obj);
        picture_input = (ImageView) findViewById(R.id.picture_input);

        picture_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.picture_input:
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        }
    }

    public void initObj(){
        Obj obj1 = new Obj("Name","Description",R.drawable.add,"initDate","loop");
        Obj obj2 = new Obj("Name2","Description",R.drawable.add,"initDate","loop");
        myObj.add(obj1);
        myObj.add(obj2);

    }
}
