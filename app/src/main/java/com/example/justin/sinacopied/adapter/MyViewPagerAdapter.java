package com.example.justin.sinacopied.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.justin.sinacopied.provider.ScrollTitle;
import com.example.justin.sinacopied.ui.HomeNewsFragment;
import com.example.justin.sinacopied.viewpagerfragment.PictureFragment;

/*public class MyViewPagerAdapter extends PagerAdapter {
    private Context mContext;

    public MyViewPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return ScrollTitle.SCROLL_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ScrollTitle.SCROLL_TITLE[position];
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment, container, false);
        container.addView(view);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("This is : " + (position + 1));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}*/

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private FragmentManager manager;

    public MyViewPagerAdapter(Context context, FragmentManager fm) {
        this(fm);
        mContext = context;
        manager = fm;
    }

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /**
         * 注意了!!!!!!
         * 这里的Fragment 一定是v4包的，否则一定没有补全提示
         */
        if (getPageTitle(position) == "图片") {
            // v4
            return new PictureFragment();
        } else {
            return new HomeNewsFragment().newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return ScrollTitle.SCROLL_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ScrollTitle.SCROLL_TITLE[position];
    }

}
