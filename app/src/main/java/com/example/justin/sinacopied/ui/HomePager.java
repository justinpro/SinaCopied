package com.example.justin.sinacopied.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.adapter.MyViewPagerAdapter;
import com.example.justin.sinacopied.view.CustomTabLayout;

public class HomePager extends Fragment {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    //private CustomTabLayout customTabLayout;

    private static final String SELETED_KEY = "selected_key";
    private TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        //初始化tabLayout
        //记住！
        //布局文件里面一定要设置成  app:tabMode="scrollable" 这个才能显示
        mTabLayout = (TabLayout) view.findViewById(R.id.title_tab);
        //customTabLayout = new CustomTabLayout(mTabLayout);

        myViewPagerAdapter = new MyViewPagerAdapter(getActivity(), getFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //注意这个setupWithViewPager一定要放在ViewPager初始化后的下面
        //否则将无法运行
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(myViewPagerAdapter);
        return view;
    }
}
