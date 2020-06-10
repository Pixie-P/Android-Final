package com.example.myvideo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity<AsyncHttpClient> extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener{

    private RecyclerView rvlist;
    private RecyclerViewAdapter mAdapter;
    private String jsonStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvlist = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvlist.setLayoutManager(layoutManager);
        rvlist.setHasFixedSize(true);
        mAdapter = new RecyclerViewAdapter();
        mAdapter.setOnClickListener(this);
        fetchData();
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beiyou.bytedance.com/api/invoke/video/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getData().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        jsonStr = new String(response.body().bytes());
                        Log.d("json",jsonStr);
                        mAdapter.setData(jsonStr);
                        rvlist.setAdapter(mAdapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit", t.getMessage());
            }
        });

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("data", jsonStr);
        intent.putExtra("index",clickedItemIndex);
        startActivity(intent);
    }
}
