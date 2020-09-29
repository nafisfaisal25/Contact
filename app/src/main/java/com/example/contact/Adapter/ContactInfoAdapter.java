package com.example.contact.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.R;

import java.util.List;

public class ContactInfoAdapter extends RecyclerView.Adapter<ContactInfoAdapter.ViewHolder> {

    private final List<String> mContactInfo;

    public ContactInfoAdapter(List<String> contactInfo) {
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
        } else {
            holder.leftImageView.setImageResource(R.drawable.ic_call);
            holder.rightImageView.setImageResource(R.drawable.ic_message);
            holder.centerTextView.setText(mContactInfo.get(position));
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
