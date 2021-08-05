package com.dsani.dsanimation.ui.expandablenavi;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.dsani.dsanimation.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableMenuAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<String> TitleList;
    private final HashMap<String, List<String>> ListDataItems;
    private float MENU_TITLE_SIZE = 15.0f;
    private float MENU_ITEM_SIZE = 13.0f;

    public ExpandableMenuAdapter(Context context, List<String> TitleList,
                                           HashMap<String, List<String>> ListDataItems) {
        this.context = context;
        this.TitleList = TitleList;
        this.ListDataItems = ListDataItems;
    }
    /**
     * must implement methods of Interface ExpandableListAdapter
     * @note nothing to do now 2021/8/3
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     *  callback func, draw expandable title
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
         String title = TitleList.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navi_menu, null);
        }

       // ImageView imgTitle = (ImageView) convertView.findViewById(R.id.navi_menu_icon);
        TextView textTitle = (TextView) convertView.findViewById(R.id.navi_menu_text);
        textTitle.setText(title);
        textTitle.setTextSize(MENU_TITLE_SIZE);
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String item = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navi_sub_menu, null);
        }

        TextView textSubTitle = (TextView) convertView
                .findViewById(R.id.navi_submenu);

        textSubTitle.setText(item);
        textSubTitle.setTextSize(MENU_ITEM_SIZE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * get child data associated with group
     * @param groupPosition group pos
     * @param childPosition child pos
     * @return child data
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.ListDataItems.get(this.TitleList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.ListDataItems.get(this.TitleList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.ListDataItems.get(this.TitleList.get(groupPosition));
    }

    @Override
    public int getGroupCount() {
        return this.TitleList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

}
