package com.example.contact.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.DataModel.Contact;
import com.example.contact.R;
import com.example.contact.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    private static final String TAG = "ContactRecyclerAdapter";

    private final Context mContext;
    List<Contact>mContactList;
    List<Contact> mBackUpContactList;
    Consumer<Integer> mOnClickListener;

    public ContactRecyclerAdapter(Context context, List<Contact> mContactList, Consumer<Integer>onClickListener) {
        this.mContactList = mContactList;
        mContext = context;
        mOnClickListener = onClickListener;
        mBackUpContactList = new ArrayList<>(mContactList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_list,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mContactList.get(position).getName());
        Log.d(TAG, "onBindViewHolder: name: "+ mContactList.get(position).getName());
        Log.d(TAG, "onBindViewHolder: ImagePath: "+ mContactList.get(position).getImageUrl());
        if (mContactList.get(position).getImageUrl() != null) {
            ImageLoader.loadImageFromSdCard(mContext, holder.imageView, Uri.parse(mContactList.get(position).getImageUrl()));
        }
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public void filter(String name) {
        mContactList = mBackUpContactList.stream().filter(contact -> {
            Log.d(TAG, "filter: " + contact.getName().toLowerCase().contains(name.toLowerCase()));
            return contact.getName().toLowerCase().contains(name.toLowerCase());
        }).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.contact_image);
            textView = itemView.findViewById(R.id.contact_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.accept(getAdapterPosition());
        }
    }
}
