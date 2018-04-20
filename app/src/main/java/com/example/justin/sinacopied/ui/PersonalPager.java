package com.example.justin.sinacopied.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.transition.TransitionAnimation;

import java.security.PermissionCollection;

public class PersonalPager extends Fragment implements View.OnClickListener {
    private ImageView personal_login;
    private ImageView personal_collection;
    private ImageView personal_history;
    private ImageView personal_comment;
    private ImageView personal_message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_fragment, container, false);

        personal_login = (ImageView) view.findViewById(R.id.personal_login);
        personal_login.setOnClickListener(this);
        personal_collection = (ImageView) view.findViewById(R.id.personal_collection);
        personal_collection.setOnClickListener(this);
        personal_history = (ImageView) view.findViewById(R.id.personal_history);
        personal_history.setOnClickListener(this);
        personal_comment = (ImageView) view.findViewById(R.id.personal_comment);
        personal_comment.setOnClickListener(this);
        personal_message = (ImageView) view.findViewById(R.id.personal_message);
        personal_message.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_login:
                Intent login = new Intent(getActivity(), PersonalLoginActivity.class);
                startActivity(login);
                break;
            case R.id.personal_collection:
                Intent collection = new Intent(getActivity(), PermissionCollection.class);
                startActivity(collection);
                break;
            case R.id.personal_history:
                Intent history = new Intent(getActivity(), PersonalHistoryActivity.class);
                startActivity(history);
                break;
            case R.id.personal_comment:
                Intent comment = new Intent(getActivity(), PersonalCommentActivity.class);
                startActivity(comment);
                break;
            case R.id.personal_message:
                Intent message = new Intent(getActivity(), PersonalMessageActivity.class);
                startActivity(message);
                break;
        }
    }
}
