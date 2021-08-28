package com.dsani.dsanimation;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;

import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;


import com.dsani.dsanimation.ui.expandablenavi.ExpandableListDataItems;
import com.dsani.dsanimation.ui.expandablenavi.ExpandableMenuAdapter;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
                                                                View.OnTouchListener,
                                                                PopupMenu.OnMenuItemClickListener{
    private DrawerLayout mDrawerLayout;
    private Toolbar mtoolbar;
    private ExpandableListView mExpandableListView;
    private ExpandableMenuAdapter mExpandableMenuAdapter;
    private List<String> mNaviListTitle;
    private HashMap<String, List<String>> mNaviListDataItems;
    private LinearLayout menuLL, menuQ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //full screen
        hideSystemUI();

        //setup navigation drawer menu
        mNaviListDataItems = ExpandableListDataItems.initNaviMenuData(this);
        mExpandableListView = findViewById(R.id.nav_expand_list);
        mNaviListTitle = new ArrayList<String>(mNaviListDataItems.keySet());
        mExpandableMenuAdapter = new ExpandableMenuAdapter(this, mNaviListTitle, mNaviListDataItems);
        mExpandableListView.setAdapter(mExpandableMenuAdapter);
        setupExpandableListClickListener();

        menuLL = findViewById(R.id.list);
        menuLL.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            v.setBackground(getResources().getDrawable(R.drawable.main_menu_pressed_style));
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            v.setBackground(getResources().getDrawable(R.drawable.main_menu_style));
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.list:
                showPopupMenu(v, R.menu.linearlist);
                break;
            case R.id.queue:
                showPopupMenu(v, R.menu.queue);
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPopupMenu(View v, int resId){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
        popupMenu.getMenuInflater().inflate(resId, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(MainActivity.this);
        popupMenu.setGravity(Gravity.END);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            //LL
            case R.id.ll_seqlist:
                AlgorithmActivity.startAlgsActivity(MainActivity.this,
                                                    getResources().getString(R.string.label_list_seq),
                                                    R.layout.ll_seq);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setupExpandableListClickListener(){
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                String title = mNaviListTitle.get(groupPosition);
                String item = mNaviListDataItems.get(mNaviListTitle.get(groupPosition)).get(childPosition);
                switch (title){
                    case "List":
                        switch (item){
                            case "linked list":


                            break;
                        }
                    break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * hide status and navi bar
     * @called in onWindowsFocusChanged()
     */
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