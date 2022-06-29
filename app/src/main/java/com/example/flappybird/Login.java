package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;
    Database DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnLogin = (Button) findViewById(R.id.btnSignIn1);
        DB = new Database(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals("")){
                    Toast.makeText(Login.this, "Empty Fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkUsernamePassword = DB.checkUsernamePassword(user,pass);
                    if (checkUsernamePassword==true){
                        Toast.makeText(Login.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),Modes.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(Login.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}