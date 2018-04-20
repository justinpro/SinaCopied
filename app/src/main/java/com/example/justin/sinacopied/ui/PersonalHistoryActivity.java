package com.example.justin.sinacopied.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.adapter.PersonalHistoryRecycler;
import com.example.justin.sinacopied.dao.DaoManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalHistoryActivity extends ActionBarActivity {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private PersonalHistoryRecycler personalHistoryRecycler;

    private String formatDate = null;

    public static final String PERSONAL_HISTORY = "history";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.personal_history_activity);


        //不能用getActionBar() 一定要用getSupportBar() 才能不会爆出空指针异常
        ActionBar actionBar = getSupportActionBar();
        /**
         * 注意!!!!!!!!!!
         * 一定要弄个断言，不然activity会一直认为获取不了ActionBar
         */
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("阅读历史");
        actionBar.show();


        recyclerView = (RecyclerView) this.findViewById(R.id.personal_history_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 注意!!!!!!
        // 这个分割线一定要放在LayoutManager的下面才会生效，因为没有生成布局管理器是不能画图的，
        // 没有画图就不能画分割线
        /*Drawable mDrawable = ContextCompat
                .getDrawable(PersonalHistoryActivity.this, R.drawable.xrecyclerview_divider);
        recyclerView.addItemDecoration(recyclerView.new DividerItemDecoration(mDrawable));*/

        personalHistoryRecycler = new PersonalHistoryRecycler(this);

        recyclerView.setAdapter(personalHistoryRecycler);

        recyclerAdapterSetListener(personalHistoryRecycler);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        formatDate = simpleDateFormat.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //recyclerAdapterSetListener(personalHistoryRecycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //只要finish() 就相当于把activity终结了，相当于关闭activity
                finish();
                return true;
            case R.id.history_edit:
                historyEditor();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 对历史记录进行编辑
     */
    public void historyEditor() {
        final DaoManager daoManager = new DaoManager();
        daoManager.setupDataBase(this);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage("是否要清空历史记录?");
        alertDialog.setTitle("注意");
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (daoManager != null) {
                    daoManager.removeAll();
                }
                personalHistoryRecycler.removeHistory();
                //退出对话框
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭对话框
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * 给适配器设置监听器,
     * 之前需要点击两次才进入的原因是，设置了两次监听器
     */
    public void recyclerAdapterSetListener(PersonalHistoryRecycler adapter) {
        adapter.getItemClickListener(new PersonalHistoryRecycler.SetOnItemClickListener() {
            @Override
            public void getHold(View view, String filename) {
                // 从fragment开始跳转，只能用getActivity() ，
                // 不能和activity开始跳转一样，用类名+this
                // 而且fragment只能跳转到activity上
                Intent contextIntent = new Intent(PersonalHistoryActivity.this, HistoryDetailContext.class);

                //传一个标题过去activity，好让activity获取一个对应的信箱系内容
                contextIntent.putExtra(HistoryDetailContext.DETAIL_ID, filename);
                contextIntent.putExtra(HistoryDetailContext.DETAIL_DATE, formatDate);


                /**
                 * 在这里只要点击一个recyclerView，马上把文件名和时间穿过去给数据库
                 * 时间就取当时点击的那一分钟
                 */
                startActivity(contextIntent);
            }
        });
    }
}
