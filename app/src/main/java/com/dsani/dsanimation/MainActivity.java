package com.dsani.dsanimation;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ExpandableListView;

import com.dsani.dsanimation.ui.expandablenavi.ExpandableListDataItems;
import com.dsani.dsanimation.ui.expandablenavi.ExpandableMenuAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mExpandableListView;
    private AppBarConfiguration mAppBarConfiguration;
    private ExpandableMenuAdapter mExpandableMenuAdapter;
    private List<String> mNaviListTitle;
    private HashMap<String, List<String>> mNaviListDataItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(mDrawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mNaviListDataItems = ExpandableListDataItems.initNaviMenuData(this);
        mExpandableListView = findViewById(R.id.nav_expand_list);
        mNaviListTitle = new ArrayList<String>(mNaviListDataItems.keySet());
        mExpandableMenuAdapter = new ExpandableMenuAdapter(this, mNaviListTitle, mNaviListDataItems);
        mExpandableListView.setAdapter(mExpandableMenuAdapter);
        setupExpandableListClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            hideSystemUI();
        }
    }


    /**
     * hide status and navi bar
     * @called in onWindowsFocusChanged()
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideSystemUI(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_FULLSCREEN|
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        );
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setupExpandableListClickListener(){
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String title = mNaviListTitle.get(groupPosition);
                String item = mNaviListDataItems.get(mNaviListTitle.get(groupPosition)).get(childPosition);
                switch (title){
                    case "List":
                        switch (item){
                            case "linked_list":

                            break;
                        }
                    break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
}