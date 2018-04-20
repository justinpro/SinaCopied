package com.example.justin.sinacopied.transition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

public class TransitionAnimation {
    private Activity context;
    private Intent intent;

    public TransitionAnimation(Activity context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    public void setTransition(Pair<View,String>... pairs){
        ActivityOptionsCompat activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context, pairs);

        ActivityCompat.startActivity(context, intent
                , activityOptionsCompat.toBundle());
    }
}
