package com.example.contacts2;

import android.content.Context;
import android.content.Intent;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private Context mContext;
    private ArrayList<Contact> mContactList;
    private DatabaseHelper myDb;
    private ActionMode mActionMode;
    private boolean isMultiSelect = false;
    private ArrayList<Contact> mMultiList = new ArrayList<>();

    public ContactAdapter(Context ct, ArrayList<Contact> list) {
        mContext = ct;
        mContactList = list;

    }


    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        myDb = new DatabaseHelper(mContext);
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
        final Contact contact = mContactList.get(position);

        holder.mTextName.setText(contact.getName());
        holder.mTextNumber.setText(contact.getNumber());

        holder.mItemLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));

        if (contact.getImageUrl().equals("") || contact.getImageUrl() == null) {
            if (contact.isMale()) {
                holder.mImageView.setImageResource(R.mipmap.male);
            } else {
                holder.mImageView.setImageResource(R.mipmap.female);
            }
        } else {
            Picasso.get().load(contact.getImageUrl()).placeholder(R.mipmap.male).into(holder.mImageView);
        }

        holder.mItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isMultiSelect = true;
                if(mActionMode == null){
                    mActionMode = ((AppCompatActivity)v.getContext()).startActionMode(actionModeCallback);
                }


                if (mMultiList.contains(mContactList.get(position))) {
                    mMultiList.remove(mContactList.get(position));
                    v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                } else {
                    v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                    mMultiList.add(mContactList.get(position));
                }
                if (mMultiList.size() == 0) {
                    mActionMode.finish();
                } else {
                    actionModeCallback.onPrepareActionMode(mActionMode,null);
                }
                return true;
            }
        });


        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMultiSelect) {
                    Intent intent = new Intent(mContext, ContactInfo.class);
                    intent.putExtra("name", contact.getName());
                    intent.putExtra("number", contact.getNumber());
                    intent.putExtra("imageUrl", contact.getImageUrl());
                    intent.putExtra("sex", contact.isMale());
                    intent.putExtra("id", contact.getId());
                    mContext.startActivity(intent);
                } else {
                    if (mMultiList.contains(mContactList.get(position))) {
                        mMultiList.remove(mContactList.get(position));
                        v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                    } else {
                        v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
                        mMultiList.add(mContactList.get(position));
                    }

                    if (mMultiList.size() == 0) {
                        mActionMode.finish();
                    } else {
                        actionModeCallback.onPrepareActionMode(mActionMode,null);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        public CircleImageView mImageView;
        public TextView mTextName, mTextNumber;
        public RelativeLayout mItemLayout;


        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView_ItemImage);
            mTextName = itemView.findViewById(R.id.textView_ItemName);
            mTextNumber = itemView.findViewById(R.id.textView_ItemNumber);
            mItemLayout = itemView.findViewById(R.id.relativeLayout_ItemLayout);


        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add("Delete");
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.delete_menu,menu);
            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(mMultiList.size() + " selected");
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId() == R.id.delete_contact){
                myDb.deleteList(mMultiList);
                for(Contact contact : mMultiList){
                    mContactList.remove(contact);
                }
                Toast.makeText(mContext,"Items will be deleted",Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isMultiSelect = false;
            mMultiList.clear();
            notifyDataSetChanged();
            mActionMode = null;
            Toast.makeText(mContext,"Destroyed",Toast.LENGTH_SHORT).show();
        }
    };




}
