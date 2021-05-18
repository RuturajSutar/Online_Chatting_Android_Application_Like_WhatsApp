package com.starkindustries.smsmagicassignment2conversationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText userET,passET,emailET,mobileET;
    Button registerBTN;

    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        userET = (EditText)findViewById(R.id.nameEditText);
        passET = (EditText)findViewById(R.id.passEditText);
        emailET = (EditText)findViewById(R.id.emailEditText);
        mobileET = (EditText)findViewById(R.id.mobileEditText);
        registerBTN = (Button)findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text = userET.getText().toString();
                String mobile_text = mobileET.getText().toString();
                String email_text = emailET.getText().toString();
                String password_text = passET.getText().toString();

                if(TextUtils.isEmpty(username_text) || TextUtils.isEmpty(mobile_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)){
                    Toast.makeText(RegisterActivity.this, "Please fill all the fields in form", Toast.LENGTH_SHORT).show();
                }
                else if (password_text.length() < 6){
                    passET.setError("Password length must be greater than 6");
                    passET.requestFocus();
                }
                else{
                    RegisterNow(username_text,mobile_text,email_text,password_text);
                }
            }
        });

    }

    private void RegisterNow(final String username, final String mobile, final String email, final String password){

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid().toString();

                            myRef = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/").getReference().child("MyUsers").child(userid);

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id" , userid);
                            hashMap.put("username" , username);
                            hashMap.put("mobile" , mobile);
                            hashMap.put("imageURL" , "default");
                            hashMap.put("status" , "offline");
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this , MainActivity.class);
                                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Email already exist. Try with other email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
