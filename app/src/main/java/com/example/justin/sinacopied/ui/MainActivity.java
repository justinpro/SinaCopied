package com.example.justin.sinacopied.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.justin.sinacopied.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView home_pager;
    private ImageView video_pager;
    private ImageView live_pager;
    private ImageView personal_pager;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

        manager = getSupportFragmentManager();

        home_pager = (ImageView) this.findViewById(R.id.home_pager);
        home_pager.setOnClickListener(this);
        video_pager = (ImageView) this.findViewById(R.id.video_pager);
        video_pager.setOnClickListener(this);
        live_pager = (ImageView) this.findViewById(R.id.live_pager);
        live_pager.setOnClickListener(this);
        personal_pager = (ImageView) this.findViewById(R.id.personal_pager);
        personal_pager.setOnClickListener(this);

        //设置默认页面
        //而且这个必须放在 home_pager初始化的后面，否则无法运行
        home_pager.setImageResource(R.drawable.home_selected);

        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            HomePager homePager = new HomePager();
            transaction.replace(R.id.frame_layout, homePager, "HomePager");
            transaction.commit();
        }
    }


    @Override
    public void onClick(View v) {
        Map<ImageView, Integer> logoMsg = getLogoMsg();
        Iterator<Map.Entry<ImageView, Integer>> iterator = logoMsg.entrySet().iterator();
        transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.home_pager:
                //把所有非点击图标全部设为非选择
                while (iterator.hasNext()) {
                    Map.Entry<ImageView, Integer> maps = iterator.next();
                    ImageView imageView = maps.getKey();
                    int logoId = maps.getValue();
                    if (imageView != home_pager){
                        imageView.setImageResource(logoId);
                    }
                }
                //点击图标则设置为点击
                home_pager.setImageResource(R.drawable.home_selected);

                HomePager homePager = new HomePager();
                transaction.replace(R.id.frame_layout, homePager, "HomePager");
                transaction.commit();
                break;
            case R.id.video_pager:
                while (iterator.hasNext()) {
                    Map.Entry<ImageView, Integer> maps = iterator.next();
                    ImageView imageView = maps.getKey();
                    int logoId = maps.getValue();
                    if (imageView != video_pager){
                        imageView.setImageResource(logoId);
                    }
                }
                video_pager.setImageResource(R.drawable.video_selected);

                VideoPager videoPager = new VideoPager();
                transaction.replace(R.id.frame_layout, videoPager, "VideoPager");
                transaction.commit();
                break;
            case R.id.live_pager:
                while (iterator.hasNext()) {
                    Map.Entry<ImageView, Integer> maps = iterator.next();
                    ImageView imageView = maps.getKey();
                    int logoId = maps.getValue();
                    if (imageView != live_pager){
                        imageView.setImageResource(logoId);
                    }
                }
                live_pager.setImageResource(R.drawable.live_selected);

                LivePager livePager = new LivePager();
                transaction.replace(R.id.frame_layout, livePager, "VideoPager");
                transaction.commit();
                break;
            case R.id.personal_pager:
                while (iterator.hasNext()) {
                    Map.Entry<ImageView, Integer> maps = iterator.next();
                    ImageView imageView = maps.getKey();
                    int logoId = maps.getValue();
                    if (imageView != personal_pager){
                        imageView.setImageResource(logoId);
                    }
                }
                personal_pager.setImageResource(R.drawable.personal_selected);

                PersonalPager personalPager = new PersonalPager();
                transaction.replace(R.id.frame_layout, personalPager, "VideoPager");
                transaction.commit();
                break;
        }
    }

    private Map<ImageView, Integer> getLogoMsg() {
        Map<ImageView, Integer> logoMsg = new HashMap<>();
        logoMsg.put(home_pager, R.drawable.home_unselected);
        logoMsg.put(video_pager, R.drawable.video_unselected);
        logoMsg.put(live_pager, R.drawable.live_unselected);
        logoMsg.put(personal_pager, R.drawable.personal_unselected);
        return logoMsg;
    }
}
