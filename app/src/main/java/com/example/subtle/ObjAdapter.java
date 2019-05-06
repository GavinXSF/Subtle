package com.example.subtle;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ObjAdapter extends RecyclerView.Adapter<ObjAdapter.ViewHolder> {
    private List<Obj> objList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView obj_img;
        TextView obj_name, obj_initDate, obj_loop, obj_description;
        public ViewHolder(View v){
            super(v);
            obj_img = v.findViewById(R.id.img);
            obj_name = v.findViewById(R.id.name);
            obj_description = v.findViewById(R.id.description);
            obj_initDate = v.findViewById(R.id.initData);
            obj_loop = v.findViewById(R.id.loop_add);
        }
    }

    public ObjAdapter(List<Obj> objs){
        objList = objs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obj_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Obj obj = objList.get(position);
        if(obj.getImgID().equals("")){
            holder.obj_img.setImageResource(R.drawable.add);
        }else{
            holder.obj_img.setImageURI(Uri.parse(obj.getImgID()));
        }
        holder.obj_name.setText(obj.getName());
        holder.obj_description.setText(obj.getDescription());
        holder.obj_initDate.setText(obj.getInitDate());
        holder.obj_loop.setText(obj.getLoop());
    }

    @Override
    public int getItemCount(){
        return objList.size();
    }
}
