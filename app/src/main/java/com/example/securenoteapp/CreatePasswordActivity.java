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

public class CreatePasswordActivity extends AppCompatActivity {

    EditText editText1, editText2;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();
                String hashedPass;

                if (text1.equals("") || text2.equals("")){
                    //there is no pass
                    Toast.makeText(CreatePasswordActivity.this, "No password entered", Toast.LENGTH_SHORT).show();
                } else {
                    if (text1.equals(text2)) {
                        //both passws equal
                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        try {
                            hashedPass = hashSalted(text1);
                            editor.putString("password", hashedPass);
                            editor.apply();

                            //enter app
                            Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                            intent.putExtra("PLAINPASS", text1);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(CreatePasswordActivity.this, "Error saving password", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    } else {
                        //not equal passws
                        Toast.makeText(CreatePasswordActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }
}
