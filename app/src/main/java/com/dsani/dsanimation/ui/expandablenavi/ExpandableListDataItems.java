package com.dsani.dsanimation.ui.expandablenavi;

import android.content.Context;

import com.dsani.dsanimation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> initNaviMenuData(Context context) {
        HashMap<String, List<String>> ListDataItems = new HashMap<String, List<String>>();

        List<String> list = new ArrayList<String>();
        list.add(context.getString(R.string.list_linkedlist));
        list.add(context.getString(R.string.list_stack));
        list.add(context.getString(R.string.list_queue));

        List<String> tree = new ArrayList<String>();
        tree.add(context.getString(R.string.tree_bt));
        tree.add(context.getString(R.string.tree_pre_trav));
        tree.add(context.getString(R.string.tree_in_trav));
        tree.add(context.getString(R.string.tree_post_trav));
        tree.add(context.getString(R.string.tree_huffman));
        tree.add(context.getString(R.string.tree_bst));

        List<String> graph = new ArrayList<String>();
        graph.add(context.getString(R.string.graph_bfs));
        graph.add(context.getString(R.string.graph_dfs));

        ListDataItems.put(context.getString(R.string.menu_list), list);
        ListDataItems.put(context.getString(R.string.menu_tree), tree);
        ListDataItems.put(context.getString(R.string.menu_graph), graph);

        return ListDataItems;
    }

}
