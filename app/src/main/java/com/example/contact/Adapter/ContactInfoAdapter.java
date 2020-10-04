package com.example.contact.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.MainActivity;
import com.example.contact.R;
import com.example.contact.utils.Permissions;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ContactInfoAdapter extends RecyclerView.Adapter<ContactInfoAdapter.ViewHolder> {

    private final List<String> mContactInfo;
    private final Context mContext;

    public ContactInfoAdapter(Context context, List<String> contactInfo) {
        mContext = context;
        mContactInfo = contactInfo;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_info, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mContactInfo.get(position).contains("@")) {
            holder.leftImageView.setImageResource(R.drawable.email_ic);
            holder.centerTextView.setText(mContactInfo.get(position));
            holder.leftImageView.setOnClickListener(view -> {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String [] {mContactInfo.get(position)});
                emailIntent.setType("plain/text");

                mContext.startActivity(emailIntent);
            });
        } else {
            holder.leftImageView.setImageResource(R.drawable.ic_call);
            holder.rightImageView.setImageResource(R.drawable.ic_message);
            holder.centerTextView.setText(mContactInfo.get(position));
            holder.leftImageView.setOnClickListener(view -> {
                if (((MainActivity)mContext).checkPermission(Permissions.PHONE_PERMISSION)) {
                    Log.d(TAG, "onBindViewHolder: initiating phone call...");
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", mContactInfo.get(position), null));
                    mContext.startActivity(callIntent);
                } else {
                    ((MainActivity)mContext).requestPermission(Permissions.PHONE_PERMISSION);
                }
            });

            holder.rightImageView.setOnClickListener(view -> {
                Log.d(TAG, "onBindViewHolder: initiating text message...");
                Intent messageIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts("sms", mContactInfo.get(position), null));
                mContext.startActivity(messageIntent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContactInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView leftImageView;
        TextView centerTextView;
        ImageView rightImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftImageView = itemView.findViewById(R.id.userInfo_left_image);
            centerTextView = itemView.findViewById(R.id.userInfo_middle_text);
            rightImageView = itemView.findViewById(R.id.userInfo_right_image);
        }
    }
}
