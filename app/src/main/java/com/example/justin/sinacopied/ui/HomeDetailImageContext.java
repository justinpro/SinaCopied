package com.example.justin.sinacopied.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.adapter.HomeNewsRecycler;
import com.example.justin.sinacopied.util.DownloadUtil;

public class HomeDetailImageContext extends FragmentActivity {
    private ImagePagerAdapter adapter;
    private Intent getUrls;
    private ViewPager viewPager;
    private ImageView imageDownload;

    private Intent download;
    private String TAG = "shit";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_image_pager);

        getUrls = getIntent();

        viewPager = (ViewPager) this.findViewById(R.id.home_image_pager);
        imageDownload = (ImageView) this.findViewById(R.id.home_image_download);

        imageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download = new Intent(HomeDetailImageContext.this, DownloadUtil.class);
                startService(download);
                Log.i(TAG, "开始服务");
                Toast.makeText(getApplicationContext(), "download", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new ImagePagerAdapter(getSupportFragmentManager()
                , getUrls.getStringExtra("url"));
        viewPager.setAdapter(adapter);
        Toast.makeText(this, " " + getUrls.getStringExtra("url"), Toast.LENGTH_SHORT).show();
    }

    /**
     * 测试点击时间的代码
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        long startTime = ev.getDownTime();
        long endTime = ev.getEventTime();
        long touchTime = endTime - startTime;

        if (ev.getAction() == MotionEvent.ACTION_UP
                && touchTime <= 300
                && isRangeOfView(imageDownload, ev)) {
            finish();
            Toast.makeText(this, " " + touchTime + " single 外面", Toast.LENGTH_SHORT).show();
        } else if (ev.getAction() == MotionEvent.ACTION_UP
                && touchTime > 300
                && isRangeOfView(imageDownload, ev)) {

            /**
             * 要设计个长按界面现实菜单的
             */
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 限制 点击区域
     */
    private boolean isRangeOfView(View view, MotionEvent event) {
        int[] location = new int[2];
        //计算view在 屏幕 上的坐标
        view.getLocationOnScreen(location);
        //x是控件的左侧到左边的屏幕
        int x = location[0];
        //y是控件的上边到顶部的屏幕
        int y = location[1];

        //在控件外面返回true,
        if (event.getX() < x
                || event.getX() > (x + view.getWidth())
                || event.getY() < y
                || event.getY() > (y + view.getHeight())) {
            return true;
        }
        return false;
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        String imgUrls;

        public ImagePagerAdapter(FragmentManager fm, String urls) {
            super(fm);
            imgUrls = urls;
        }

        @Override
        public Fragment getItem(int position) {
            return HomeDetailImageFragment.newInstance(imgUrls);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
