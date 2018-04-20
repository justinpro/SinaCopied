package com.example.justin.sinacopied.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.justin.sinacopied.R;

public class PersonalCollectionActivity extends Activity {
    public static final String PERSONAL_HISTORY = "history";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_history_activity);
    }
}
