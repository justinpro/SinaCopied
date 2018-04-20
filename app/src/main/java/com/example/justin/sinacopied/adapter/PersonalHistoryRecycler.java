package com.example.justin.sinacopied.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.dao.DaoManager;
import com.example.justin.sinacopied.provider.AssetProvider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PersonalHistoryRecycler extends RecyclerView.Adapter<PersonalHistoryRecycler.ViewHold> {

    private TextView history_title;
    private TextView history_date;

    private DaoManager daoManager;
    private Map<Long, String> queryMaps;
    private Iterator<Map.Entry<Long, String>> iterator;
    private Context context;

    private PersonalHistoryRecycler.SetOnItemClickListener setOnItemClickListener;

    public PersonalHistoryRecycler(Context context) {
        this.context = context;

        daoManager = new DaoManager();
        daoManager.setupDataBase(context);
        queryMaps = daoManager.query();
        iterator = queryMaps.entrySet().iterator();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        final TextView history_title;
        final TextView history_date;

        public ViewHold(View itemView, TextView history_title, TextView history_date) {
            super(itemView);
            this.history_title = history_title;
            this.history_date = history_date;
        }
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_history_recycler, parent, false);

        history_title = (TextView) view.findViewById(R.id.personal_history_title);
        history_date = (TextView) view.findViewById(R.id.personal_history_date);

        return new ViewHold(view, history_title, history_date);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {

        Long id;
        String getResult;
        final String title;
        String date ;

        Map.Entry<Long, String> getNext = iterator.next();
        id = getNext.getKey();
        getResult = getNext.getValue();
        if (getResult != null) {
            title = getResult.split("\\,")[0];
            date = getResult.split("\\,")[1];

            holder.history_title.setText(title);
            holder.history_date.setText(date);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //把标题传给方法就好了
                    setOnItemClickListener.getHold(v, title);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return queryMaps.size();
    }

    public void removeHistory() {
        if (queryMaps != null) {
            queryMaps.clear();
        }
        //注意!!!!
        //要清空数据的源头才会调用notifyDataSetChanged()
        notifyDataSetChanged();
    }

    public interface SetOnItemClickListener {
        void getHold(View view, String filename);
    }

    public void getItemClickListener(PersonalHistoryRecycler.SetOnItemClickListener listener) {
        setOnItemClickListener = listener;
    }
}
