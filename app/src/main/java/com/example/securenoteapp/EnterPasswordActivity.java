package com.example.securenoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.securenoteapp.Crypto.hashSalted;

public class EnterPasswordActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    String password;

    public void openChangePass (View view) {
        Intent intent = new Intent (getApplicationContext(), ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        //Load Password
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                String hashedText = null;
                try {
                    hashedText = hashSalted(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (password.equals(hashedText)) {
                    //open app
                    Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                    intent.putExtra("PLAINPASS", text);
                    startActivity(intent);
                    finish();
                } else {
                    //not equal passws
                    Toast.makeText(EnterPasswordActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

}
