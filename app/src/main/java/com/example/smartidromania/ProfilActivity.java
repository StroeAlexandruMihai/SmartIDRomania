package com.example.smartidromania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity {
    private Button btn_modifica;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Administrators");
        userID = user.getUid();

        final TextView emailTextView = (TextView) findViewById(R.id.tv_email);
        final TextView nameTextView = (TextView) findViewById(R.id.tv_fullname);

        btn_modifica = findViewById(R.id.btn_modifica);
        btn_modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilActivity.this, ModificaActivity.class));
            }
        });

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.Email;

                    nameTextView.setText(fullName);
                    emailTextView.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilActivity.this,"Eroare !",Toast.LENGTH_LONG).show();
            }
        });

    }
}