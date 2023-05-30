package com.example.internalchat.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internalchat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TinTucAdapter extends RecyclerView.Adapter<TinTucAdapter.PhotoViewHoder>{

    private List<newsItem> newsList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(newsItem newsItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public TinTucAdapter(List<newsItem> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tintuc_item,parent,false);
        return new PhotoViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHoder holder, int position) {
        newsItem newsItem = newsList.get(position);

        holder.title.setText(newsItem.getTitle());
        holder.time.setText(newsItem.getTime());
        Picasso.get().load(newsItem.getImageUrl()).into(holder.imageView);

        // Xử lý sự kiện onClick cho item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(newsItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(newsList==null){
            return 0;
        }else {
            return newsList.size();
        }
    }

    public class PhotoViewHoder extends RecyclerView.ViewHolder{
        ImageView imageView, delete;
        TextView title,time;
        public PhotoViewHoder(@NonNull View itemView) {
            super(itemView);
//            delete = imageView.findViewById(R.id.delete_image);
            title = itemView.findViewById(R.id.name_item_tintuc);
            time = itemView.findViewById(R.id.time_tintuc);
            imageView = itemView.findViewById(R.id.item_image_tintuc);
        }
    }
}
