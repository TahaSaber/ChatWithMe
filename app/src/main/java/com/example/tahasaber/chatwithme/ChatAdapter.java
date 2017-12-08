package com.example.tahasaber.chatwithme;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.tahasaber.chatwithme.R.id.photoImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    ArrayList<MessageDataClass> messages;
    Context mContext;

    public ChatAdapter() {
    }


    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        ChatAdapter.ChatViewHolder chatAdapter = new ChatAdapter.ChatViewHolder(v);
        //  setHasStableIds(true);

        return chatAdapter;
    }

    public ChatAdapter(ArrayList<MessageDataClass> messages, Context mContext) {
        this.messages = messages;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, final int position) {

        String messageBodyText = messages.get(position).getMsgBody();
        String messageSenderID = messages.get(position).getMsgPublisherId();
        MessageDataClass message = messages.get(position);
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            if (messageSenderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                int colorValue = Color.parseColor("#ff0000");
                holder.cardView.setCardBackgroundColor(colorValue);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.cardView.setLayoutParams(params);
            } else {
                int colorValue = Color.parseColor("#E0E0E0");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.cardView.setLayoutParams(params);
                holder.cardView.setCardBackgroundColor(colorValue);
            }
            holder.photoImageView.setMaxWidth(200);
            //  holder.messageBody.setVisibility(View.GONE);

            // Picasso.with(mContext).load(message.getPhotoUrl()).into(holder.photoImageView);

            holder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(holder.photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(holder.photoImageView);
        } else {
            if (messageSenderID.equals("app")) {
                int colorValue = Color.parseColor("#ff0000");
                int textColor = Color.parseColor("#ffffff");
                holder.cardView.setCardBackgroundColor(colorValue);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.cardView.setLayoutParams(params);
                holder.messageBody.setTextColor(textColor);

            } else if (messageSenderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                int colorValue = Color.parseColor("#ff0000");
                int textColor = Color.parseColor("#ffffff");
                holder.cardView.setCardBackgroundColor(colorValue);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.cardView.setLayoutParams(params);
                holder.messageBody.setTextColor(textColor);


            } else {
                int colorValue = Color.parseColor("#E0E0E0");
                int textColor = Color.parseColor("#000000");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.cardView.setLayoutParams(params);
                holder.cardView.setCardBackgroundColor(colorValue);
                holder.messageBody.setTextColor(textColor);
            }
            holder.messageBody.setText(messageBodyText);
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView messageBody;
        private ImageView photoImageView;
        CardView cardView;

        public ChatViewHolder(View itemView) {
            super(itemView);

            messageBody = (TextView) itemView.findViewById(R.id.messageTextView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_message);
            photoImageView = (ImageView) itemView.findViewById(R.id.photoImageView);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}