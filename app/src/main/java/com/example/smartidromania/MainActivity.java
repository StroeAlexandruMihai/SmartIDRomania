package com.example.smartidromania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button register;
    private EditText edit_text_password;
    private EditText edit_text_email;
    private Button buton_login;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView textview_forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        textview_forgot = (TextView) findViewById(R.id.textview_forgot);
        textview_forgot.setOnClickListener(this);

        edit_text_password = (EditText)findViewById(R.id.edit_text_password);
        edit_text_email =(EditText) findViewById(R.id.edit_text_email);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buton_login = (Button) findViewById(R.id.buton_login);
        buton_login.setOnClickListener(this);

        register = findViewById(R.id.buton_register);
        register.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buton_register:
                startActivity(new Intent(this, inregistrare_admin.class));
                break;
            case R.id.buton_login:
                userLogin();
                break;
            case R.id.textview_forgot:
                startActivity(new Intent(this, parola_uitata.class));
                break;

        }
    }

    private void userLogin() {
        String email = edit_text_email.getText().toString().trim();
        String password = edit_text_password.getText().toString().trim();

        if(password.isEmpty()){
            edit_text_password.setError("Introduceti o parola formata din minim 6 caractere ! ");
            edit_text_password.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edit_text_email.setError("Introduceti un email valid !");
            edit_text_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edit_text_email.setError("Introduceti un email valid !");
            edit_text_email.requestFocus();
            return;
        }

        if(password.length() < 6){
            edit_text_password.setError("Introduceti o parola formata din minim 6 caractere !");
            edit_text_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user.isEmailVerified()){
                    startActivity(new Intent(MainActivity.this, MenuActivity.class) );
                } else{
                    user.sendEmailVerification();
                    Toast.makeText(MainActivity.this, "Verificati emailul ! Verificati email pentru a confirma contul !", Toast.LENGTH_LONG).show();
                }


            }else{
                Toast.makeText(MainActivity.this, "LOGARE ESUATA! Verifcati emailul si parola !", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Sunteti sigur ca doriti sa iesiti din aplicatie?")
                .setCancelable(false)
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Nu", null)
                .show();
    }
}