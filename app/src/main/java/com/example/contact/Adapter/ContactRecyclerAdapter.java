package com.example.contact.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contact.DataModel.Contact;
import com.example.contact.R;

import java.util.List;
import java.util.function.Consumer;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    List<Contact>mContactList;
    Consumer<Integer> mOnClickListener;

    public ContactRecyclerAdapter(Context context, List<Contact> mContactList, Consumer<Integer>onClickListener) {
        this.mContactList = mContactList;
        mContext = context;
        mOnClickListener = onClickListener;
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
        Glide.with(mContext)
                .asBitmap()
                .load(mContactList.get(position).getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
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
