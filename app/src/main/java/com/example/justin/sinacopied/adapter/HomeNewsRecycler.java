package com.example.justin.sinacopied.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.provider.AssetProvider;
import com.example.justin.sinacopied.provider.ChildContext;
import com.example.justin.sinacopied.provider.ScrollTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeNewsRecycler extends RecyclerView.Adapter<HomeNewsRecycler.ViewHold> {
    public static String Img_Path="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec" +
            "=1512061043652&di=5201aa7c0b3af094a30b63b9ec6146ee&imgtype=0&src=http%3A%2F%2Fimg5" +
            ".duitang.com%2Fuploads%2Fitem%2F201601%2F15%2F20160115135358_iQzhn.jpeg";

    private HomeNewsRecycler homeNewsRecycler;
    private SetOnItemClickListener setOnItemClickListener;

    private AssetProvider assetProvider;
    private List<String> newsArray;
    private String date;
    private Context context;

    public HomeNewsRecycler(Context context, AssetProvider assetProvider, List<String> newsArray, String date) {
        this.context = context;
        this.date = date;
        this.assetProvider = assetProvider;
        this.newsArray = newsArray;
    }

    /**
     * 这样子做主要是为了优化adapter，避免findViewById()这个树查找操作(数据一多就非常耗时)
     */
    public class ViewHold extends RecyclerView.ViewHolder {
        TextView title_recycler;
        TextView child_context_recycler;
        ImageView imageView;

        public ViewHold(View itemView
                , TextView title_recycler, TextView child_context_recycler, ImageView imageView) {
            super(itemView);
            this.title_recycler = title_recycler;
            this.child_context_recycler = child_context_recycler;
            this.imageView = imageView;
        }

        public TextView getTitle_recycler() {
            return title_recycler;
        }

        public TextView getChild_context_recycler() {
            return child_context_recycler;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }


    /**
     * 这个方法其实就是把需要用到的控件保存到ViewHolder
     *
     * @return 返回给ViewHold保存
     */
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_recycler, parent, false);

        TextView title_recycler = (TextView) view.findViewById(R.id.title_recycler);
        TextView child_context_recycler = (TextView) view.findViewById(R.id.child_context_recycler);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_recycler);

        return new ViewHold(view, title_recycler, child_context_recycler, imageView);
    }

    @Override
    public void onBindViewHolder(final ViewHold holder, int position) {

        /**
         * 要放个判断，因为如果没有判断，就无法绑定底部的ViewHolder了
         */
        //获得随机的文件名了,这个文件名是个定了时间的了
        //这样子就可以保证每次都获得同一个时间的asset文件夹,随机的新闻文件名
        final String filename = assetProvider.getRandomNews(newsArray);
        //访问服务器获取文件名对应的标题
        /**
         * 这步先放住，因为无法用webView 的webChromeClient获得标题，因为网页加载是在详情页的，没进入详情页
         * 是无法拿到标题的，
         * 所以我先用文件名代替 (filename)
         */

        holder.getTitle_recycler().setText(" Position : " + position + " " + filename);
        holder.getChild_context_recycler().setText(ChildContext.CHILD_CONTEXT);
        //holder.getImageView().setImageResource(R.drawable.icon);
        Glide.with(context)
                .load(Img_Path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImageView());

        //给RecyclerView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnItemClickListener.getHold(v, filename);
            }
        });
    }


    @Override
    public int getItemCount() {
        //扩大条目显示的数量
        //newArray.size() = 3
        return newsArray.size() * 5;
    }

    public interface SetOnItemClickListener {
        void getHold(View view, String filename);
    }

    public void getItemClickListener(SetOnItemClickListener listener) {
        setOnItemClickListener = listener;
    }
}
