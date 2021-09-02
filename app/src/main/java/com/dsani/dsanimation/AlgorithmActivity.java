package com.dsani.dsanimation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.dsani.dsanimation.algs.AlgorithmVisualizer;
import com.dsani.dsanimation.algs.visualizer.SeqListDrawable;



public class AlgorithmActivity extends FullScreenActivity implements View.OnClickListener {

    private String activityTitle;
    private int layoutResId;
    private Toolbar mToolbar;
    private ImageView defImgView, CDImgView, URImgView;
    private Button btnAdd, btnDel;
    private AlgorithmVisualizer<Integer> visualizerDef, visualizerCD, visualizerUR;

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

        setupAlgs(layoutResId);

    }

    private void setupAlgs(int layoutResId){
       switch (layoutResId){
           case R.layout.ll_seq:
               TextView notionText = findViewById(R.id.ds_def_notation);
               notionText.setText(Html.fromHtml(getResources().getString(R.string.ll_notion)));
               defImgView = findViewById(R.id.ds_def_img);
               CDImgView = findViewById(R.id.ds_CD_img);
               URImgView = findViewById(R.id.ds_UR_img);
               /**
                note: width is unkown until  during onCreate() executed time.
                sol_1: call in other time
                sol_2: using screen width
                */
               visualizerDef = new SeqListDrawable(AlgorithmActivity.this, getResources().getDisplayMetrics().widthPixels,
                                                defImgView);
               visualizerCD = new SeqListDrawable(AlgorithmActivity.this, getResources().getDisplayMetrics().widthPixels,
                       CDImgView);
               visualizerUR = new SeqListDrawable(AlgorithmActivity.this, getResources().getDisplayMetrics().widthPixels,
                       URImgView);
               visualizerDef.setup(AlgorithmVisualizer.OPERATION.EX);
               visualizerCD.setup(AlgorithmVisualizer.OPERATION.EX);
               visualizerUR.setup(AlgorithmVisualizer.OPERATION.EX);
               break;
           default:
               break;
       }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                visualizerCD.setup(AlgorithmVisualizer.OPERATION.DELETE);
                break;
            case R.id.btn_insert:
                visualizerCD.setup(AlgorithmVisualizer.OPERATION.INSERT);
                break;
            case R.id.btn_get:
                final EditText editText = new EditText(this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(this);
                inputDialog.setTitle("输入索引").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(AlgorithmActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                int idx = Integer.parseInt(editText.getText().toString()) ;
                                visualizerUR.setup(AlgorithmVisualizer.OPERATION.GET, idx, 0 );
                            }
                        }).show();

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




}