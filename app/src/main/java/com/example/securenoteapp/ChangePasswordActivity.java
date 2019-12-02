package com.example.securenoteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.securenoteapp.Crypto.decrypt;
import static com.example.securenoteapp.Crypto.encrypt;
import static com.example.securenoteapp.Crypto.hashSalted;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText editText0, editText1, editText2;
    Button button;
    String FILE_NAME = "example.txt";


    public void save(String newpass, String text) {
        String encryptedText;
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE); //OPEN FILE
            encryptedText = encrypt(text, newpass);       //ENCRYPT INPUT
            fos.write(encryptedText.getBytes());           //SAVE ENCRYPTED INPUT

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String load(String oldPass) {
        FileInputStream fis = null;
        String savedNote=null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            savedNote= decrypt(sb.toString(), oldPass);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return savedNote;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editText0 = (EditText) findViewById(R.id.editText0);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass1 = editText1.getText().toString();
                String newPass2 = editText2.getText().toString();
                String oldPass  = editText0.getText().toString();
                String oldPassSaved;
                String hashedOldPass=null;
                String hashedNewPass=null;

                SharedPreferences settings = getSharedPreferences("PREFS", 0);
                oldPassSaved = settings.getString("password", "");

                try {
                    hashedOldPass = hashSalted(oldPass);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (oldPassSaved.equals(hashedOldPass)) {

                    if (newPass1.equals("") || newPass2.equals("")) {
                        //there is no pass
                        Toast.makeText(ChangePasswordActivity.this, "No password entered", Toast.LENGTH_SHORT).show();
                    } else {
                        if (newPass1.equals(newPass2)) {
                            //both passws equal

                            settings = getSharedPreferences("PREFS", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            try {
                                hashedNewPass = hashSalted(newPass1);
                                editor.putString("password", hashedNewPass);
                                editor.apply();

                                String savedNote = load(oldPass);
                                save(newPass1, savedNote);

                                //enter app
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("PLAINPASS", newPass1);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(ChangePasswordActivity.this, "Error saving password", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        } else {
                            //not equal passws
                            Toast.makeText(ChangePasswordActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Old password incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
