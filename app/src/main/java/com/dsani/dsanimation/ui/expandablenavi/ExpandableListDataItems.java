package com.dsani.dsanimation.ui.expandablenavi;

import android.content.Context;

import com.dsani.dsanimation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> initNaviMenuData(Context context) {
        LinkedHashMap<String, List<String>> ListDataItems = new LinkedHashMap<String, List<String>>();

        List<String>  linearList= new ArrayList<String>();
        linearList.add(context.getString(R.string.list_seq));
        linearList.add(context.getString(R.string.list_single));
        linearList.add(context.getString(R.string.list_double));
        ListDataItems.put(context.getString(R.string.menu_linear_list), linearList);

        List<String> queue = new ArrayList<String>();
        queue.add(context.getString(R.string.queue_sequence));
        queue.add(context.getString(R.string.queue_linkedlist));
        ListDataItems.put(context.getString(R.string.menu_queue), queue);

        List<String> stack = new ArrayList<String>();
        stack.add(context.getString(R.string.stack_sequence));
        stack.add(context.getString(R.string.stack_linkedlist));
        ListDataItems.put(context.getString(R.string.menu_stack), stack);

        List<String> tree = new ArrayList<String>();
        tree.add(context.getString(R.string.tree_bt));
        tree.add(context.getString(R.string.tree_pre_trav));
        tree.add(context.getString(R.string.tree_in_trav));
        tree.add(context.getString(R.string.tree_post_trav));
        tree.add(context.getString(R.string.tree_huffman));
        tree.add(context.getString(R.string.tree_bst));
        ListDataItems.put(context.getString(R.string.menu_tree), tree);

        List<String> graph = new ArrayList<String>();
        graph.add(context.getString(R.string.graph_bfs));
        graph.add(context.getString(R.string.graph_dfs));
        ListDataItems.put(context.getString(R.string.menu_graph), graph);



        return ListDataItems;
    }

}
