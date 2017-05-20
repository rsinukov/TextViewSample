package com.example.animatedsquaretextview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText field = (EditText) findViewById(R.id.activity_main_edit_text);
        final TextView textView = (TextView) findViewById(R.id.activity_main_square_text_view);

        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // no op
            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s);
            }
        });
    }
}
