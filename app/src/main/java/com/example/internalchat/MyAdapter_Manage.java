package com.example.internalchat;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter_Manage extends ArrayAdapter<DataClass> {

    public MyAdapter_Manage(@NonNull Context context, ArrayList<DataClass> dataClassArrayList) {
       super(context,0,dataClassArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView_manage = convertView;
        if (listitemView_manage == null) {
            listitemView_manage = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }
        DataClass dataClass = getItem(position);

        ImageView gridImage = listitemView_manage.findViewById(R.id.item_image_sp);
        TextView gridTitle = listitemView_manage.findViewById(R.id.name_item);
        TextView gridPrice = listitemView_manage.findViewById(R.id.price_item);
        TextView gridTime = listitemView_manage.findViewById(R.id.time_item);
        TextView gridLocation = listitemView_manage.findViewById(R.id.location_item);


        Picasso.get().load(dataClass.getImageURL()).into(gridImage);
        gridTitle.setText(dataClass.getTitle());
        gridPrice.setText(dataClass.getPrice()+" "+"triệu/tháng");
        gridLocation.setText(dataClass.getLocation());
        gridTime.setText(" "+dataClass.getTimeItem());
        listitemView_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_manage = new Intent(getContext(),Detail_Room_Manage_Activity.class);
              Toast.makeText(getContext(), "Detail Room: " + dataClass.getTitle(), Toast.LENGTH_SHORT).show();
                intent_manage.putExtra("post_id",dataClass.getPostId());
                Log.d(TAG,dataClass.getPostId()+"...............");
                getContext().startActivity(intent_manage);
            }
        });
        return listitemView_manage;
    }
}
