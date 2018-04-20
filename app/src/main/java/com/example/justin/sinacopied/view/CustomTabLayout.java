package com.example.justin.sinacopied.view;


import android.support.design.widget.TabLayout;

import com.example.justin.sinacopied.provider.ScrollTitle;

public class CustomTabLayout{
    private TabLayout customTabLayout;

    public CustomTabLayout(TabLayout tabLayout) {
        customTabLayout = tabLayout;
        for (int i = 0; i < ScrollTitle.SCROLL_TITLE.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(ScrollTitle.SCROLL_TITLE[i]));
        }
    }


}
