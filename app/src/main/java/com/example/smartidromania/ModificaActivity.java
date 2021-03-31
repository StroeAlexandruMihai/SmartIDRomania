package com.example.smartidromania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ModificaActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button chanegemail;
    Button changepassword;
    private Button fragment_parola, fragment_email;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);
        fragmentContainer =(FrameLayout) findViewById(R.id.fragment_container);
        fragment_email = (Button) findViewById(R.id.btn_fragment_email);
        fragment_parola = (Button) findViewById(R.id.btn_fragment_parola);

        fragment_parola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentPass();
            }
        });
        fragment_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentEmail();
            }
        });

   }

    private void openFragmentEmail() {
        EmailFragment eFragment = EmailFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, eFragment, "BLANK").commit();
    }

    private void openFragmentPass() {
        PasswordFragnent pFragment = PasswordFragnent.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, pFragment, "BLANK").commit();
    }

}