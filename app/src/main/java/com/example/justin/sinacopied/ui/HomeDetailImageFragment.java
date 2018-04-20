package com.example.justin.sinacopied.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.justin.sinacopied.R;

public class HomeDetailImageFragment extends Fragment {
    private String mImageViewUrls;
    private ProgressBar progressBar;
    private ImageView imageView;

    private static final String IMAGE_DATA_EXTRA = "extra_image_data";

    public static HomeDetailImageFragment newInstance(String url) {

        Bundle args = new Bundle();

        HomeDetailImageFragment fragment = new HomeDetailImageFragment();
        args.putString(IMAGE_DATA_EXTRA, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageViewUrls = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_image_detail, container, false);

        imageView = (ImageView) view.findViewById(R.id.detail_image);
        progressBar = (ProgressBar) view.findViewById(R.id.ready_progressbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Glide.with(getActivity())
                .load(mImageViewUrls)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
