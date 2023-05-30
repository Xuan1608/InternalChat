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

import java.util.List;

public class SearchAdapter extends ArrayAdapter<DataClass> {

    public SearchAdapter(@NonNull Context context, List<DataClass> dataClassArrayList_Search) {
       super(context,0,dataClassArrayList_Search);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.recyclerview_item, parent, false);
        }
        DataClass dataClass = getItem(position);

        ImageView gridImage = listitemView.findViewById(R.id.item_image_recycle);
        TextView gridTitle = listitemView.findViewById(R.id.name_item_recycle);
        TextView gridPrice = listitemView.findViewById(R.id.price_item_recycle);
        TextView gridTime = listitemView.findViewById(R.id.time_item_recycle);
        TextView gridLocation = listitemView.findViewById(R.id.location_item_recycler);


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
