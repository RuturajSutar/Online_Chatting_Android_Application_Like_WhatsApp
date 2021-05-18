package com.starkindustries.smsmagicassignment2conversationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starkindustries.smsmagicassignment2conversationapp.Adapter.MessageAdapter;
import com.starkindustries.smsmagicassignment2conversationapp.Model.Chat;
import com.starkindustries.smsmagicassignment2conversationapp.Model.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerV;

    RecyclerView recyclerView;
    EditText msg_editText;
    ImageButton sendBtn;

    String userid;

    ValueEventListener seenListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        imageView = (ImageView)findViewById(R.id.imageview_profile);
        username = (TextView)findViewById(R.id.username);

        sendBtn = (ImageButton)findViewById(R.id.btn_send);
        msg_editText = (EditText)findViewById(R.id.text_send);


        recyclerV = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerV.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerV.setLayoutManager(linearLayoutManager);


//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });




        intent = getIntent();
        userid = intent.getStringExtra("userid");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/").getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if(user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher_round);

                }
                else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(imageView);
                }
                readMessage(fuser.getUid() , userid , user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = msg_editText.getText().toString();
                if(!message.equals("")){
                    sendMessage(fuser.getUid() , userid , message);
                }
                else{
                    Toast.makeText(MessageActivity.this, "Please send a non empty message", Toast.LENGTH_SHORT).show();
                }

                msg_editText.setText("");
            }
        });


        SeenMessage(userid);



    }

//    private void setSupportActionBar(Toolbar toolbar) {
//
//    }


    private void SeenMessage(final String userid){
        reference = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/")
                .getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("isseen" , true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender , String receiver , String message){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/").getReference();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender" , sender);
        hashMap.put("receiver" , receiver);
        hashMap.put("message" , message);
        hashMap.put("isseen" , false);
        reference.child("Chats").push().setValue(hashMap);


        final DatabaseReference chatRef = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/")
                .getReference("ChatList").child(fuser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void readMessage(final String myid , final String userid , final String imageurl){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/")
                .getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid)
                    && chat.getSender().equals(myid)){
                        mchat .add(chat);

                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this , mchat , imageurl);
                    recyclerV.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckStatus(String status){
        reference = FirebaseDatabase.getInstance("https://sms-magic-assignment-2-ruturaj-default-rtdb.firebaseio.com/")
                .getReference("MyUsers").child(fuser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status" , status);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        CheckStatus("Offline");
    }



}
