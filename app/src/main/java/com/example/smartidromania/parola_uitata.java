package com.example.smartidromania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class parola_uitata extends AppCompatActivity {

    private EditText edit_text_email_recover_password;
    private Button buton_recover;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parola_uitata);

        edit_text_email_recover_password = (EditText) findViewById(R.id.edit_text_email_recover_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buton_recover = (Button) findViewById(R.id.buton_recover);

        mAuth = FirebaseAuth.getInstance();

        buton_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = edit_text_email_recover_password.getText().toString().trim();

        if(email.isEmpty()){
            edit_text_email_recover_password.setError("Introduceti un email valid !");
            edit_text_email_recover_password.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edit_text_email_recover_password.setError("Introduceti un email valid !");
            edit_text_email_recover_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(parola_uitata.this, "Un mail pentru resetarea parolei a fost trimis ! Verifcati emailul !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(parola_uitata.this, "Eroare! Incearca din nou !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}