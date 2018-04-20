package com.example.justin.sinacopied.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.adapter.HomeNewsRecycler;
import com.example.justin.sinacopied.provider.AssetProvider;
import com.example.justin.sinacopied.xrecyclerview.ProgressStyle;
import com.example.justin.sinacopied.xrecyclerview.XRecyclerView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Home 那页的内容
 */
public class HomeNewsFragment extends Fragment {

    private AssetProvider assetProvider;

    private HomeNewsRecycler recyclerAdapter;
    private HomeNewsRecycler refreshAdapter;

    private LinearLayoutManager linearLayoutManager;

    private XRecyclerView recyclerView;
    private static final String SELETED_KEY = "selected_key";

    private boolean isUsed = false;
    private static final String SAVEDSTATE = "savedstate";

    private int titlePositon;

    private String formatDate = null;
    private List<String> getList;
    private List<String> refreshList;
    private List<String> savedList;


    public HomeNewsFragment newInstance(int position) {
        titlePositon = position;

        Bundle args = new Bundle();
        args.putInt(SELETED_KEY, position);
        HomeNewsFragment fragment = new HomeNewsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * !!!!!!!!!!!! 注意了 !!!!!!!!!!!
         * 之所以要把这个文件名数组放在onCreate 里面，是因为onCreate 在生命周期里面只加载 1 次
         * 如果放在onCreateView里面就会每次加载fragment的时候都加载一次，导致所获得新闻每次都不一样
         * 放在onCreate里面，每次都可以使用同一个新闻名数组了
         */
        //获得新闻数组了，准备把assetProvide和新闻数据传入，传入RecyclerFragment显示
        assetProvider = new AssetProvider(getResources().getAssets());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        formatDate = simpleDateFormat.format(new Date());
        getList = assetProvider.getAssetFileName(formatDate);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment, container, false);
        int scrollPosition = 0;

        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);

        //没有 LinearLayout 是不会显示出数据的
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        //设置分割线
        Drawable dividerDrawable =
                ContextCompat.getDrawable(getActivity(), R.drawable.xrecyclerview_divider);
        recyclerView.addItemDecoration(recyclerView.new DividerItemDecoration(dividerDrawable));

        //设置上拉和下拉的方式
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置箭头
        recyclerView.setArrowImageView(R.drawable.refresh_arrow);

        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        recyclerView.scrollToPosition(scrollPosition);

        //设置加载监听器
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onPullRefresh(assetProvider.getAssetFileName(formatDate));
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoadingRefresh(getList, formatDate);
                    }
                }, 1000);
            }
        });


        recyclerAdapter = new HomeNewsRecycler(getActivity(),assetProvider, getList, formatDate);

        recyclerView.setAdapter(recyclerAdapter);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        //设置点击RecyclerView的监听器
        recyclerAdapterSetListener(recyclerAdapter);
        recyclerView.refresh();
    }


    /**
     * 给适配器设置监听器,
     * qian之前需要点击两次才进入的原因是，设置了两次监听器
     */
    public void recyclerAdapterSetListener(HomeNewsRecycler adapter) {
        adapter.getItemClickListener(new HomeNewsRecycler.SetOnItemClickListener() {
            @Override
            public void getHold(View view, String filename) {
                // 从fragment开始跳转，只能用getActivity() ，
                // 不能和activity开始跳转一样，用类名+this
                // 而且fragment只能跳转到activity上
                Intent contextIntent = new Intent(getActivity(), HomeDetailContext.class);

                //传一个标题过去activity，好让activity获取一个对应的信箱系内容
                contextIntent.putExtra(HomeDetailContext.DETAIL_ID, filename);
                contextIntent.putExtra(HomeDetailContext.DETAIL_DATE, formatDate);


                /**
                 * 在这里只要点击一个recyclerView，马上把文件名和时间穿过去给数据库
                 * 时间就取当时点击的那一分钟
                 */
                startActivity(contextIntent);
            }
        });
    }

    /**
     * SwipedRefreshLayout
     * 异步获取新的数据，并返回给OnPostExecute()，并执行刷新界面操作
     */
    /*private class CustomFreshTask extends AsyncTask<Void, Void, ArrayList<String>> {

        public CustomFreshTask() {
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return assetProvider.getAssetFileName(formatDate);
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            onRefreshComplete(strings);
        }
    }*/

    /**
     * 刷新界面的方法
     *
     * @param result 异步类获取的全新的数据
     */
    private void onPullRefresh(List<String> result) {
        //重新获取数据
        if (refreshAdapter == null) {
            refreshAdapter = new HomeNewsRecycler(getActivity(),assetProvider, result, formatDate);
        }
        isUsed = true;
        refreshList = result;

        recyclerAdapter.notifyDataSetChanged();
        //两个方式都可以用
        //recyclerView.setAdapter(refreshAdapter);


        /**
         * 注意!!!!!
         * 需要再使用一次监听器
         * 因为 recyclerAdapter 适配器已经获得了一个新的对象
         * 点击 itemView 会导致 ViewHold 空指针异常
         */
        recyclerAdapterSetListener(refreshAdapter);
        //停止刷新
        recyclerView.refreshComplete();
    }

    /**
     * 在原始数据getList集合添加数据，用notify更新数据，因为不是用的新的适配器，所以不用重新设置监听器
     *
     * @param loadingList 原始数据
     */
    private void onLoadingRefresh(List<String> loadingList, String date) {
        assetProvider.getLoadingMoreFileName(loadingList, date);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.refreshComplete();
    }
}
