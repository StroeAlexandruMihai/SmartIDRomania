package com.example.smartidromania;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_add_user, btn_cauta, btn_logout, btn_profil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btn_add_user =(Button) findViewById(R.id.btn_add_user);
        btn_add_user.setOnClickListener(this);
        btn_cauta = (Button)findViewById(R.id.btn_cauta);
        btn_cauta.setOnClickListener(this);
        btn_logout =(Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_profil = (Button)findViewById(R.id.btn_profil);
        btn_profil.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_user:
                startActivity(new Intent(this, MainActivity2.class));
                break;
            case R.id.btn_cauta:
                startActivity(new Intent(this, CautaActivity.class));
                break;
            case R.id.btn_profil:
                startActivity(new Intent(this, ProfilActivity.class));
                break;
            case R.id.btn_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("LOGOUT");
                alertDialogBuilder.setMessage("Pentru delogare apasati butonul... Da");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Da", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        logout();
                    }
                });

                alertDialogBuilder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MenuActivity.this,"Ati apasat... Nu",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNeutralButton("Anuleaza", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Ati apasat... Anulare",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                break;

        }
        
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("IESIRE")
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