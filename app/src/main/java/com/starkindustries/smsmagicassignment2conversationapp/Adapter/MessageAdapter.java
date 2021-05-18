package com.starkindustries.smsmagicassignment2conversationapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.starkindustries.smsmagicassignment2conversationapp.MessageActivity;
import com.starkindustries.smsmagicassignment2conversationapp.Model.Chat;
import com.starkindustries.smsmagicassignment2conversationapp.Model.Users;
import com.starkindustries.smsmagicassignment2conversationapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Chat> mChat;
    private String imgURL;

    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_REGHT = 1;

    public MessageAdapter(Context context, List<Chat> mChat , String imgURL) {
        this.context = context;
        this.mChat = mChat;
        this.imgURL = imgURL;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_REGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right , parent , false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left , parent , false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
//        final Chat  = mChat.get(position);
//        holder.username.setText(users.getUsername());
//        if(users.getImageURL().equals("default")){
//            holder.imageView.setImageResource(R.mipmap.ic_launcher_round);
//        }
//        else{
//            Glide.with(context).load(users.getImageURL()).into(holder.imageView);
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, MessageActivity.class);
//                i.putExtra("userid" , users.getId());
//                context.startActivity(i);
//            }
//        });

        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imgURL.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
        }
        else{
            Glide.with(context).load(imgURL).into(holder.profile_image);
        }


        if (position == mChat.size()-1){
            if(chat.isIsseen()){
                holder.txt_seen.setText("Seen");
                holder.txt_seen.setVisibility(View.VISIBLE);
            }
            else{
                holder.txt_seen.setText("Delivered");
                holder.txt_seen.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.txt_seen.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen_status);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_REGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}

