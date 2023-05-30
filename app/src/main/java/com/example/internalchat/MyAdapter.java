package com.example.internalchat;

import android.content.Context;
import android.content.Intent;
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

public class MyAdapter extends ArrayAdapter<DataClass> {

    private int itemHeight;

    public MyAdapter(@NonNull Context context, ArrayList<DataClass> dataClassArrayList) {
       super(context,0,dataClassArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        DataClass dataClass = getItem(position);

        ImageView gridImage = listitemView.findViewById(R.id.item_image_sp);
        TextView gridTitle = listitemView.findViewById(R.id.name_item);
        TextView gridPrice = listitemView.findViewById(R.id.price_item);
        TextView gridTime = listitemView.findViewById(R.id.time_item);
        TextView gridLocation = listitemView.findViewById(R.id.location_item);


        Picasso.get().load(dataClass.getImageURL()).into(gridImage);
        gridTitle.setText(dataClass.getTitle());
        gridPrice.setText(dataClass.getPrice()+" "+"triệu/tháng");
        gridLocation.setText(dataClass.getLocation());
        gridTime.setText(" "+dataClass.getTimeItem());
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Detail_Room_Activity.class);
              Toast.makeText(getContext(), "Detail Room: " + dataClass.getTitle(), Toast.LENGTH_SHORT).show();
                intent.putExtra("id",dataClass.getPostId());
                getContext().startActivity(intent);
            }
        });
        return listitemView;
    }
}
