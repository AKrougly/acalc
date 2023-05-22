package com.example.acalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    TextView inputDisplay, historyDisplay;
    NumPad numPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        inputDisplay = (TextView)findViewById(R.id.textView_input_display);
        historyDisplay = (TextView)findViewById(R.id.textView_history_display);
        numPad = new NumPad(this);

        Button button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
                    startActivity(intent); finish();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
            startActivity(intent); finish();
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        numPad.keyEventHandler(event);
        return super.onKeyUp(keyCode, event);
    }

    public String getInputDisplayText() {
        return inputDisplay.getText().toString();
    }
    public void setInputDisplayText(String text) {
        if (inputDisplay.getText().toString().length() < 20)
            inputDisplay.setText(text);
    }
    public void setHistoryDisplayText(String text) {
        historyDisplay.setText(text);
    }
/*
    public Button findButton(String id) {
        return (Button)findViewById(this.getResources().getIdentifier(id, "id", this.getPackageName()));
    }
    public void appendInputDisplayText(String text) {
        if (inputDisplay.getText().toString().length() < 20)
            inputDisplay.setText(getResources().getString(R.string.append_input_display, inputDisplay.getText().toString(), text));
    }
    public void rightTrimInputDisplayText() {
        if (inputDisplay.getText().toString().length() > 0)
            inputDisplay.setText(inputDisplay.getText().toString().substring(0, inputDisplay.getText().toString().length() - 1));
    }
    public void clearInputDisplayText() {
        inputDisplay.setText("");
    }
*/
}
