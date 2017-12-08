package com.example.tahasaber.chatwithme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by TahaSaber on 7/10/2017.
 */

public class ChatHeadsAdapter extends ArrayAdapter<UserDataClass> {

    private Context context;
    public static ArrayList<UserDataClass> friendsList;


    public ChatHeadsAdapter(@NonNull Context context, ArrayList<UserDataClass> friendList) {
        super(context, -1, friendList);

        this.context = context;
        this.friendsList = friendList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.item_chat_heads, parent, false);

        TextView friendName = (TextView) rootView.findViewById(R.id.user_name);
        TextView description = (TextView) rootView.findViewById(R.id.last_message);
        ImageView userImag = (ImageView) rootView.findViewById(R.id.chat_head_image);

        friendName.setText(friendsList.get(position).getName());
        description.setText(friendsList.get(position).getDescription());
        String photoUrl = friendsList.get(position).getPhotoUrl();
        userImag.setVisibility(View.VISIBLE);
        Glide.with(userImag.getContext())
                .load(photoUrl)
                .into(userImag);


        return rootView;
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
