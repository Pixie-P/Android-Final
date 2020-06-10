package com.example.myvideo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecViewHolder> {

    private static final String TAG = "GreenAdapter";
    private List<Video> mylist = new ArrayList<>();
    private ListItemClickListener mOnClickListener;
    private Context context;


    public void setData(String jsonStr){
        JsonToObj(jsonStr);
    }

    public void setOnClickListener(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_video_snapshot;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecViewHolder viewHolder = new RecViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder recViewHolder, int position) {

        loadCover(recViewHolder.imageView, mylist.get(position).getFeedurl(), context);
        recViewHolder.nickname.setText("@"+mylist.get(position).getNickname());
        recViewHolder.description.setText(mylist.get(position).getDescription());
        Log.d("adapter",mylist.get(position).getDescription());
        Log.d(TAG, "onBindViewHolder: #" + position);
        recViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class RecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nickname;
        private final TextView description;
        private final ImageView imageView;

        public RecViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.tv_nickname);
            description = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.image_snapshot);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }
    }

    public static void loadCover(ImageView imageView, String url, Context context) {

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                )
                .load(url)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(imageView);
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
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
