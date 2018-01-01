package com.adit.sudokusolver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.adit.sudokusolver.MESSAGE";
    //public final TableLayout grid = (TableLayout) findViewById(R.id.tableLayout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

//    public void clearText(View view){
//        int count = grid.getChildCount();
//        for (int i = 0; i < count; i++){
//            View child = (EditText) grid.getChildAt(i);
//            ((EditText) child).getText().clear();
//        }
//    }


}
