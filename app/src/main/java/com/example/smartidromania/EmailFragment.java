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
//email fragment

public class EmailFragment extends Fragment {



    public EmailFragment() {
        // Required empty public constructor
    }



    public static EmailFragment newInstance() {
        EmailFragment fragment = new EmailFragment();
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
        View view =  inflater.inflate(R.layout.fragment_email, container, false);

        //cod change pass
        FirebaseAuth auth;
        Button changeemail;
        final EditText email = view.findViewById(R.id.edit_text_email);
        final EditText password = view.findViewById(R.id.edit_text_password);
        email_change = view.findViewById(R.id.edit_text_email_change);

        changeemail = (Button) view.findViewById(R.id.btn_mod_email);


        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMail(password.getText().toString(), email.getText().toString());
            }
        });

        return view;

    }
    EditText email_change;

    private void changeMail(String email,final String password) {

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
                    user.updateEmail(email_change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Email schimbat!", Toast.LENGTH_SHORT).show();
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