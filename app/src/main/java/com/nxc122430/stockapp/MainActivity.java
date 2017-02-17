package com.nxc122430.stockapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // create button and edit text
    Button display;
    EditText value;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get value by id
        display = (Button)findViewById(R.id.button);
        value = (EditText)findViewById(R.id.editText);

        // after diplay button is clicked open stock activity
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user input
                input = value.getText().toString();
                // calling to the stock avtivity
                Intent i = new Intent(MainActivity.this, StockActivity.class);
                // sending user input value
                i.putExtra("symbol", input.toUpperCase());
                startActivity(i);
            }
        });
    }
}
