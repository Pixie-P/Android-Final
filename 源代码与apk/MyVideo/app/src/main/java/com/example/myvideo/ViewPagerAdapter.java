package com.example.myvideo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewpagerHolder> {

    private String TAG="viewpagerAdapter";
    private ViewpagerHolder currentHolder;
    private String data;
    private Context context;
    private AudioManager audioManager;
    private List<Video> mylist = new ArrayList<>();

    public ViewPagerAdapter(String json, AudioManager manager){
        data = json;
        audioManager = manager;
        JsonToObj(data);
    }
    @NonNull
    @Override
    public ViewPagerAdapter.ViewpagerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_video;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ViewPagerAdapter.ViewpagerHolder viewHolder = new ViewPagerAdapter.ViewpagerHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewpagerHolder holder, int position) {
        holder.nickname.setText("@"+mylist.get(position).getNickname());
        holder.description.setText(mylist.get(position).getDescription());
        int likecount = mylist.get(position).getLikecount();
        String likeStr;
        if(likecount / 10000 > 0){
            likeStr = String.format("%.1f",(double)likecount/10000)+"W";
        }
        else likeStr =Integer.toString(likecount);
        holder.likecount.setText(likeStr);
        String feedurl = mylist.get(position).getFeedurl();
        holder.videoView.setVideoURI(Uri.parse(feedurl));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        holder.videoView.setLayoutParams(layoutParams);
        holder.videoView.start();
        currentHolder = holder;
        holder.like.setOnClickListener(view -> {
            int like;
            if(holder.flag == 0) {
                holder.flag = 1;
                like = mylist.get(position).getLikecount()+1;
                holder.like.setColorFilter(Color.RED);
                Toast.makeText(context,"已点赞~",Toast.LENGTH_SHORT).show();
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(holder.hearts,
                        "scaleX", 0.5f, 1.5f);
                scaleXAnimator.setInterpolator(new LinearInterpolator());
                scaleXAnimator.setDuration(600);
                scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleXAnimator.setRepeatCount(1);

                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(holder.hearts,
                        "scaleY", 0.5f, 1.5f);
                scaleYAnimator.setInterpolator(new LinearInterpolator());
                scaleYAnimator.setDuration(600);
                scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleYAnimator.setRepeatCount(1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
                holder.hearts.setVisibility(View.VISIBLE);
                scaleXAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        holder.hearts.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                holder.hearts.setVisibility(View.VISIBLE);
                animatorSet.start();

            }
            else{
                holder.flag = 0;
                like = mylist.get(position).getLikecount()-1;
                holder.like.setColorFilter(Color.LTGRAY);
                Toast.makeText(context,"取消点赞~",Toast.LENGTH_SHORT).show();
            }
            mylist.get(position).setLikecount(like);
            String likeS;
            if(like / 10000 > 0){
                likeS = String.format("%.1f",(double)like/10000)+"W";
            }
            else likeS =Integer.toString(like);
            holder.likecount.setText(likeS);
        });

        holder.sound.setOnClickListener(view -> {
            if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                holder.sound.setImageResource(R.drawable.ic_mute);
                Toast.makeText(context,"开启静音~",Toast.LENGTH_SHORT).show();
            }
            else{
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 4, 0);
                holder.sound.setImageResource(R.drawable.ic_sound);
                Toast.makeText(context,"关闭静音~",Toast.LENGTH_SHORT).show();
            }
        });

        holder.layout.setOnClickListener(view -> {
            if(holder.videoView.isPlaying()) {
                holder.videoView.pause();
                holder.play.setVisibility(View.VISIBLE);
            }
            else {
                holder.videoView.start();
                holder.play.setVisibility(View.GONE);
            }
        });
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public ViewPagerAdapter.ViewpagerHolder getCurrentItem() {
        return currentHolder;
    }

    public class ViewpagerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nickname;
        private final TextView description;
        public VideoView videoView;
        private final TextView likecount;
        private final ImageView like;
        private final ImageView hearts;
        private final ImageView play;
        private final RelativeLayout layout;
        private final ImageView sound;
        private int flag;

        public ViewpagerHolder(@NonNull View itemView) {
            super(itemView);
            flag = 0;
            nickname = itemView.findViewById(R.id.nickname);
            description = itemView.findViewById(R.id.description);
            videoView = itemView.findViewById(R.id.video);
            likecount = itemView.findViewById(R.id.likecount);
            like = itemView.findViewById(R.id.like);
            hearts =itemView.findViewById(R.id.hearts);
            sound = itemView.findViewById(R.id.sound);
            play = itemView.findViewById(R.id.play1);
            layout = itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            clickedposition = getAdapterPosition();
        }
    }

    private void JsonToObj(String jsonStr) {
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String feedurl = jsonObject.getString("feedurl");
                String nickname = jsonObject.getString("nickname");
                String description = jsonObject.getString("description");
                int likecount = jsonObject.getInt("likecount");
                Video video = new Video(feedurl, nickname, description, likecount);
                mylist.add(video);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

