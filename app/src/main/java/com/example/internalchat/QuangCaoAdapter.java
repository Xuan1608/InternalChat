package com.example.internalchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class QuangCaoAdapter extends PagerAdapter {
    private Context context;
    private List<QuangCao> listQuangCao;

    public QuangCaoAdapter(Context context, List<QuangCao> listQuangCao) {
        this.context = context;
        this.listQuangCao = listQuangCao;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_quangcao,container,false);
        ImageView imgQuangCao =view.findViewById(R.id.img_quang_cao);

        QuangCao quangCao =listQuangCao.get(position);
        if (quangCao!=null){
            Glide.with(context).load(quangCao.getResourceId()).into(imgQuangCao);

            container.addView(view);
        }
        return view;

    }

    @Override
    public int getCount() {
        if(listQuangCao != null ){
            return listQuangCao.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
