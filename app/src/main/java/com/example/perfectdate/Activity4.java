package com.example.perfectdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity4 extends AppCompatActivity {
   EditText e1,e2,mname;
   RadioGroup mRadioGroup;

   Button b1;
   private FirebaseAuth mAuth;
   private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {
                    Intent intent= new Intent(Activity4.this,Activity5.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        e1=findViewById(R.id.email2);
        e2=findViewById(R.id.pass2);
        b1=findViewById(R.id.register2);
        mname=findViewById(R.id.name);
        mRadioGroup=findViewById(R.id.radio);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton =(RadioButton)findViewById(selectId);
                if(radioButton.getText()==null){
                    return;
                }
                final String email= e1.getText().toString();
                final String password= e2.getText().toString();
                final String name= mname.getText().toString();

                           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Activity4.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(!task.isSuccessful()){
                                      Toast.makeText(Activity4.this,"sign up error",Toast.LENGTH_SHORT).show();
                                  }else{
                                      String userId= mAuth.getCurrentUser().getUid();
                                      DatabaseReference curentUserDb= FirebaseDatabase.getInstance().getReference().child("users").child(radioButton.getText().toString()).child(userId).child("name");
                                      curentUserDb.setValue(name);
                                  }
                               }
                           });

            }

        });
            }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }



}

