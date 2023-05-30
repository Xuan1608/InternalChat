package com.example.internalchat;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhotoAdapter1 extends RecyclerView.Adapter<PhotoAdapter1.PhotoViewHoder>{

    private ArrayList<Uri> uriArrayList;


    public PhotoAdapter1(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public PhotoViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_photo1,parent,false);
        return new PhotoViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHoder holder, int position) {
        holder.imageView.setImageURI(uriArrayList.get(position));


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uriArrayList.remove(uriArrayList.get(position));
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position,getItemCount());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(uriArrayList==null){
            return 0;
        }else {
            return uriArrayList.size();
        }
    }

    public class PhotoViewHoder extends RecyclerView.ViewHolder{
        ImageView imageView, delete;
        public PhotoViewHoder(@NonNull View itemView) {
            super(itemView);
//            delete = imageView.findViewById(R.id.delete_image);
            imageView = itemView.findViewById(R.id.img_photo1);
        }
    }
}
