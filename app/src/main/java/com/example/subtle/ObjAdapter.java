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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Obj obj = objList.get(position);
        if (obj.getImgID().equals("")) {
            holder.obj_img.setImageResource(R.drawable.default_obj);
        } else {
            holder.obj_img.setImageURI(Uri.parse(obj.getImgID()));
        }
        holder.obj_name.setText(obj.getName());
        holder.obj_description.setText(obj.getDescription());

        String tempDate = "From " + obj.getInitDate();
        holder.obj_initDate.setText(tempDate);

        StringBuilder tempLoop = new StringBuilder();
        int cnt = 0;
        if (obj.getLoop() == "") {
            tempLoop.append("No reminder");
        } else {
            for (String temp : obj.getLoop().split("-")) {
                if (!temp.equals("0")) {
                    switch (cnt) {
                        case 0:
                            tempLoop.append("Every ").append(temp).append(" years");
                            break;
                        case 1:
                            if (tempLoop.toString().equals("")) {
                                tempLoop.append("Every ").append(temp).append(" months");
                            } else {
                                tempLoop.append(" ").append(temp).append(" months");
                            }
                            break;
                        case 2:
                            if (tempLoop.toString().equals("")) {
                                tempLoop.append("Every ").append(temp).append(" days");
                            } else {
                                tempLoop.append(" ").append(temp).append(" days");
                            }
                            break;
                    }
                }
                cnt += 1;
            }
            if (tempLoop.length() == 0)
                tempLoop.append("No reminder");

        }
        holder.obj_loop.setText(tempLoop);
    }

    @Override
    public int getItemCount(){
        return objList.size();
    }
}
