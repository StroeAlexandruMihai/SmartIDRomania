package com.example.smartidromania;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class inregistrare_admin extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText edit_text_email_register, edit_text_password_register, edit_text_fullname_register;
    Button buton_inregistrare;
    private ImageView logo_img_register;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inregistrare_admin);

        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        edit_text_email_register = (EditText) findViewById(R.id.edit_text_email_register);
        edit_text_password_register = (EditText) findViewById(R.id.edit_text_password_register);
        edit_text_fullname_register = (EditText) findViewById(R.id.edit_text_fullname_register);

        logo_img_register = (ImageView)findViewById(R.id.logo_img_register);
        logo_img_register.setOnClickListener(this);

        buton_inregistrare = (Button) findViewById(R.id.buton_inregistrare);
        buton_inregistrare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buton_inregistrare:
                register();
                break;
            case R.id.logo_img_register:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    private void register() {
        final String email, password, fullName;
        email = edit_text_email_register.getText().toString().trim();
        password = edit_text_password_register.getText().toString().trim();
        fullName = edit_text_fullname_register.getText().toString().trim();

        if(fullName.isEmpty()){
            edit_text_fullname_register.setError("Introduceti numele intreg !");
            edit_text_fullname_register.requestFocus();
            return;
        }


        if(password.isEmpty()){
            edit_text_password_register.setError("Introduceti o parola formata din minim 6 caractere ! ");
            edit_text_password_register.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edit_text_email_register.setError("Introduceti un email valid !");
            edit_text_email_register.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edit_text_email_register.setError("Introduceti un email valid !");
            edit_text_email_register.requestFocus();
            return;
        }

        if(password.length() < 6){
            edit_text_password_register.setError("Introduceti o parola formata din minim 6 caractere !");
            edit_text_password_register.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            User user = new User(fullName,
                                    email
                                    );
                            FirebaseDatabase.getInstance().getReference("Administrators")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override

                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(inregistrare_admin.this, "Succes!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        // Redirect
                                    }else{
                                        Toast.makeText(inregistrare_admin.this, "Incearca din nou!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }
                        else{
                            Toast.makeText(inregistrare_admin.this, "Incearca din nou!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}