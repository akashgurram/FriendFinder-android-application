package com.example.friendfinderakash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextEmail, editTextPassword, editTextName;
    private TextView textViewLogin;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin) ;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //This is the code to make the Login textview Bluish
        String text = "Have an account? ";
        String text2 = "Login";
        String text3 = text + text2;
        Spannable spannable = new SpannableString(text3);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), text.length(),(text + text2).length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewLogin.setText(spannable, TextView.BufferType.SPANNABLE);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;

        }

        if(password.length()<6){
            editTextPassword.setError("Minimum password length 6");
            editTextPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    final String name = editTextName.getText().toString();

                    //For Display Name
                    UserProfileChangeRequest req = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    mAuth.getCurrentUser().updateProfile(req);
                    Intent i = new Intent(getApplicationContext(), LoggedInActivity.class);
                    i.putExtra("Name",name);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"User Registration Successful", Toast.LENGTH_SHORT).show();
                }

                //Case where user already exists
                else {
                    if(task.getException() instanceof  FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User already registered", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.buttonSignUp:

                registerUser();

                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, MainActivity.class));
        }
    }
}
