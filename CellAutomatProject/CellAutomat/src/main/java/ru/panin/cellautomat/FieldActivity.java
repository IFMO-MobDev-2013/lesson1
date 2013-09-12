package ru.panin.cellautomat;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cellautomat.R;

/**
 * Created by marsermd on 06.09.13.
 */
public class FieldActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CellField cf = (CellField)findViewById(R.id.CellField);
        cf.fps = (TextView)findViewById(R.id.FPS);
    }
}
