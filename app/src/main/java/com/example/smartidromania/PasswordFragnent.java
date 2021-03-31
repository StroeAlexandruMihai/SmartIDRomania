package com.example.smartidromania;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordFragnent extends Fragment {



    public PasswordFragnent() {
        // Required empty public constructor
    }

    public static PasswordFragnent newInstance() {
        PasswordFragnent fragment = new PasswordFragnent();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_password_fragnent, container, false);

        //cod change pass
        FirebaseAuth auth;
        Button changepassword;
        final EditText email = view.findViewById(R.id.edit_text_email);
        final EditText password = view.findViewById(R.id.edit_text_password);
        changepass = view.findViewById(R.id.edit_text_pass_change);

        changepassword = (Button) view.findViewById(R.id.btn_mod_pass);


        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               changepass(password.getText().toString(), email.getText().toString());
           }
        });

        return view;

    }
    EditText changepass;

    private void changepass(String password,final String email) {

        {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Get auth credentials from the user for re-authentication
            AuthCredential credential = EmailAuthProvider.getCredential(email, password); // Current Login Credentials

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Log.d("value", "User re-authenticated.");

                    // Now change your pass address \\
                    //----------------Code for Changing pass Address----------\\
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(changepass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Parola schimbata!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(),"Eroare!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}