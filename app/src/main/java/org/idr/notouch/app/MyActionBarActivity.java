package org.idr.notouch.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by ismail ARILIK on 4.05.2014.
 */
public class MyActionBarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // action bar settings
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6699cc")));
    }
}
