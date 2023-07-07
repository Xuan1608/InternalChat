package com.example.internalchat;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.HolderVideo>{
    private Context context;
    private ArrayList<ModelVideo> videoArrayList;

    public VideoAdapter(Context context, ArrayList<ModelVideo> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public HolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video,parent,false);

        return new HolderVideo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVideo holder, int position) {
        ModelVideo modelVideo = videoArrayList.get(position);

        String id = modelVideo.getId();
        String title = modelVideo.getTitle();
//        String timestamp = modelVideo.getTimestamp();
        String videoUrl = modelVideo.getVideoUrl();

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(Long.parseLong(timestamp));
//        String date = DateFormat.format("dd/MM/yyyy K:mm a", calendar).toString();



        holder.title.setText(title);
//        holder.time.setText(date);
        setVideoUrl(modelVideo,holder);
    }

    private void setVideoUrl(ModelVideo modelVideo, HolderVideo holder) {
      holder.progressBar.setVisibility(View.VISIBLE);
      String videoUrl = modelVideo.getVideoUrl();
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.videoView);
        Uri videoUri = Uri.parse(videoUrl);
        holder.videoView.setMediaController(mediaController);
        holder.videoView.setVideoURI(videoUri);

        holder.videoView.requestFocus();
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        holder.videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        holder.progressBar.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        holder.progressBar.setVisibility(View.GONE);
                        return true;
                }

                return false;
            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    class HolderVideo extends RecyclerView.ViewHolder{
        VideoView videoView;
        TextView title, time;
        ProgressBar progressBar;


        public HolderVideo(@NonNull View itemView) {
            super(itemView);

            videoView= itemView.findViewById(R.id.videoViewl);
            title=itemView.findViewById(R.id.title_tv);
            time = itemView.findViewById(R.id.time_tv);
            progressBar = itemView.findViewById(R.id.progress_video);
        }
    }
}
