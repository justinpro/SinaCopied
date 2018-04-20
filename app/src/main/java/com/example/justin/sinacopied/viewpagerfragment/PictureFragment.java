package com.example.justin.sinacopied.viewpagerfragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.justin.sinacopied.R;

public class PictureFragment extends Fragment {
    private TextView pictureTitle;
    private ImageView pictureImage;

    private ImageView shareButton;
    private ImageView commentButton;

    public static PictureFragment newInstance() {

        Bundle args = new Bundle();

        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_item_picture, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        pictureTitle = (TextView) view.findViewById(R.id.picture_title);
        pictureImage = (ImageView) view.findViewById(R.id.picture_face);

        shareButton = (ImageView) view.findViewById(R.id.picture_share);
        commentButton = (ImageView) view.findViewById(R.id.picture_comment);
    }
}
