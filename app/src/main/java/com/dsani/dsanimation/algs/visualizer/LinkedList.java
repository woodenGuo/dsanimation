package com.dsani.dsanimation.algs.visualizer;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsani.dsanimation.R;

import java.util.Random;

public class LinkedList extends AppCompatActivity {


   private Context context;
   public LinkedList(Context context) {
      this.context = context;
   }

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.nav_header_main);
   }

   private void init(){
      TextView nodes[] = new TextView[new Random().nextInt(4)+4];
   }

}
