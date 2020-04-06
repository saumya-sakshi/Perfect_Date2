package com.example.perfectdate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;



import java.util.ArrayList;
import java.util.List;


public class Activity5 extends AppCompatActivity {
   private Cards cards_datta[];
    private arrayAdapter arrayAdapter;

    private int i;
    private FirebaseAuth mAuth;
    private String currentUId;
    private DatabaseReference usersDb;
    ListView listView;
    List<Cards> rowItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
usersDb=FirebaseDatabase.getInstance().getReference().child("users");
mAuth=FirebaseAuth.getInstance();



checkUserSex();
        rowItems = new ArrayList<Cards>();




        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );




        SwipeFlingAdapterView flingContainer=(SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override

            public void removeFirstObjectInAdapter() {

                // this is the simplest way to delete an object from the Adapter (/AdapterView)

                Log.d("LIST", "removed object!");

                rowItems.remove(0);

                arrayAdapter.notifyDataSetChanged();

            }



            @Override

            public void onLeftCardExit(Object dataObject) {
                Cards obj =(Cards)dataObject;
                String userId=obj.getUserId();
                usersDb.child(oppositeuserSex).child(userId).child("connections").child("nope").child(currentUId).setValue(true);

                //Do something on the left!

                //You also have access to the original object.

                //If you want to use it just cast it (String) dataObject

                Toast.makeText(Activity5.this, "Left!", Toast.LENGTH_SHORT).show();

            }



            @Override

            public void onRightCardExit(Object dataObject) {
                Cards obj =(Cards) dataObject;
                String userId=obj.getUserId();
                usersDb.child(oppositeuserSex).child(userId).child("connections").child("yeah").child(currentUId).setValue(true);
                Toast.makeText(Activity5.this, "Right!", Toast.LENGTH_SHORT).show();

            }



            @Override

            public void onAdapterAboutToEmpty(int itemsInAdapter) {



            }



            @Override

            public void onScroll(float scrollProgressPercent) {



            }

        });





        // Optionally add an OnItemClickListener

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {

            @Override

            public void onItemClicked(int itemPosition, Object dataObject) {

               Toast.makeText(Activity5.this,"clicked",Toast.LENGTH_SHORT).show();

            }

        });



    }
    private String userSex,oppositeuserSex;
public void checkUserSex(){
  final  FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference femaleDb= FirebaseDatabase.getInstance().getReference().child("users").child("FEMALE");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.getKey().equals(user.getUid())){
                    userSex="FEMALE";
                    oppositeuserSex="MALE";
                    getoppositesexuser();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    DatabaseReference maleDb= FirebaseDatabase.getInstance().getReference().child("users").child("MALE");
    maleDb.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if(dataSnapshot.getKey().equals(user.getUid())){
                userSex="MALE";
                oppositeuserSex="FEMALE";
                getoppositesexuser();

            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
public void getoppositesexuser(){
    DatabaseReference oppositesexDb= FirebaseDatabase.getInstance().getReference().child("users").child(oppositeuserSex);
    oppositesexDb.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

          if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId)&& !dataSnapshot.child("connections").child("yeah").hasChild(currentUId)){
              Cards Items = new Cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString());
              rowItems.add(Items);
              arrayAdapter.notifyDataSetChanged();
          }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}
    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent =new Intent(Activity5.this,Activity2.class);
        startActivity(intent);
        finish();
        return;
    }
}