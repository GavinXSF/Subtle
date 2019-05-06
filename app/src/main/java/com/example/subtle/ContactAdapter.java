package com.example.subtle;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> contactList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView contact_photo;
        TextView contact_name, contact_birthday, contact_loop, contact_hobby;
        public ViewHolder(View v){
            super(v);
            contact_photo = v.findViewById(R.id.contact_img);
            contact_name = v.findViewById(R.id.contact_name);
            contact_hobby = v.findViewById(R.id.contact_hobby);
            contact_birthday = v.findViewById(R.id.contact_birthday);
            contact_loop = v.findViewById(R.id.contact_loop);
        }
    }

    public ContactAdapter(List<Contact> objs){
        contactList = objs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Contact contact = contactList.get(position);
        if(contact.getPhoto().equals("")){
            holder.contact_photo.setImageResource(R.drawable.add);
        }else{
            holder.contact_photo.setImageURI(Uri.parse(contact.getPhoto()));
        }
        holder.contact_name.setText(contact.getName());
        holder.contact_hobby.setText(contact.getHobby());

        String tempDate = "Born on " + contact.getBirthday();
        holder.contact_birthday.setText(tempDate);

        StringBuilder tempLoop = new StringBuilder();
        int cnt = 0;
        for (String temp : contact.getLoop().split("-")){
            if (!temp.equals("0")){
                switch (cnt){
                    case 0:
                        tempLoop.append("Every ").append(temp).append(" years");
                        break;
                    case 1:
                        if(tempLoop.toString().equals("")){
                            tempLoop.append("Every ").append(temp).append(" months");
                        }else{
                            tempLoop.append(" ").append(temp).append(" months");
                        }
                        break;
                    case 2:
                        if(tempLoop.toString().equals("")){
                            tempLoop.append("Every ").append(temp).append(" days");
                        }else{
                            tempLoop.append(" ").append(temp).append(" days");
                        }
                        break;
                }
            }
            cnt += 1;
        }
        if (tempLoop.length() == 0)
            tempLoop.append("No reminder");
        holder.contact_loop.setText(tempLoop);
    }

    @Override
    public int getItemCount(){
        return contactList.size();
    }
}
