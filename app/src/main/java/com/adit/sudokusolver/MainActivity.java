package com.adit.sudokusolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.adit.sudokusolver.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clearText(View view){
        TableLayout grid = (TableLayout) findViewById(R.id.grid);
        int rows = grid.getChildCount();
        for (int i = 0; i < rows; i++){
            View child = (TableRow) grid.getChildAt(i);
            int cols = ((TableRow) child).getVirtualChildCount();
                for (int j = 0; j < cols; j++) {
                    View elem = (EditText) ((TableRow) child).getVirtualChildAt(j);
                    ((EditText) elem).getText().clear();
                }
        }
    }

    public void solvePuzzle(View view){
        TableLayout grid = (TableLayout) findViewById(R.id.grid);
        Cell[][] input = new Cell[9][9];
        int rows = grid.getChildCount();
        for (int i = 0; i < rows; i++){
            View child = (TableRow) grid.getChildAt(i);
            int cols = ((TableRow) child).getVirtualChildCount();
            for (int j = 0; j < cols; j++) {
                View elem = (EditText) ((TableRow) child).getVirtualChildAt(j);
                if ( ((EditText) elem).getText().toString().matches(""))
                    input[i][j] = new Cell(0);
                else
                    input[i][j] = new Cell(Integer.parseInt(((EditText) elem).getText().toString()));
            }
        }
        Puzzle puzz = new Puzzle(input);
        input = puzz.answer();
        for (int i = 0; i < rows; i++){
            View child = (TableRow) grid.getChildAt(i);
            int cols = ((TableRow) child).getVirtualChildCount();
            for (int j = 0; j < cols; j++) {
                View elem = (EditText) ((TableRow) child).getVirtualChildAt(j);
                ((EditText) elem).setText(""+input[i][j].getValue());
            }
        }
    }


}
