package com.dsani.dsanimation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.dsani.dsanimation.algs.visualizer.SeqListDrawable;



public class AlgorithmActivity extends AppCompatActivity {
    private String activityTitle;
    private int layoutResId;
    private Toolbar mToolbar;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        activityTitle = intent.getStringExtra("label");
        layoutResId = intent.getIntExtra("layout", R.layout.ll_seq);

        setContentView(layoutResId);
        setTitle(activityTitle);
        mToolbar = findViewById(R.id.algstoolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        hideSystemUI();

        setDefination(layoutResId);

    }

    private void setDefination(int layoutResId){
       switch (layoutResId){
           case R.layout.ll_seq:
               TextView notionText = findViewById(R.id.ds_def_notation);
               notionText.setText(Html.fromHtml(getResources().getString(R.string.ll_notion)));
               ImageView defImgView = findViewById(R.id.ds_def_img);
               /**
                note: width is unkown until  during onCreate() executed time.
                */
               SeqListDrawable seqListDrawable = new SeqListDrawable(AlgorithmActivity.this, getResources().getDisplayMetrics().widthPixels);
               defImgView.setImageDrawable(seqListDrawable);

               break;
           default:
               break;
       }
    }


    public static void startAlgsActivity(Context context, String title, int resId){
        Intent intent = new Intent(context, AlgorithmActivity.class);
        intent.putExtra("label", title);
        intent.putExtra("layout", resId);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideSystemUI(){
        //support display cutout
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(params);
        }

        // status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set navigationbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}