package com.gimeno.enric.infobilbao;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbarPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Load toolbar
        toolbarPreference = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarPreference);
        toolbarPreference.setTitle(R.string.configuracion);

        setSupportActionBar(toolbarPreference);
        // Arrow Button show
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // Action added to the arrow button
            case android.R.id.home:
                this.finish();
        }
        return  true;
    }
}