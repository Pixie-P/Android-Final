package com.example.myvideo;

import android.app.Service;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class PlayActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private String data;
    private int index;
    private ViewPagerAdapter viewPagerAdapter;
    private AudioManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent().getStringExtra("data");
        index = getIntent().getIntExtra("index", 0);
        setContentView(R.layout.activity_play);
        manager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, 4, 0);
        viewPager2 = findViewById(R.id.viewpager2);
        viewPagerAdapter = new ViewPagerAdapter(data, manager);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setCurrentItem(index);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPagerAdapter.getCurrentItem().videoView.start();
                
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPager2.getCurrentItem();
    }
}
