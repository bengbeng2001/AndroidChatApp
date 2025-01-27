package com.example.webchatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModelClass> messagesAdapterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<MessageModelClass> messagesAdapterArrayList) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModelClass messages = messagesAdapterArrayList.get(position);

        // Long click listener for delete
        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this message?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        // Implement message deletion logic here
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
            return true;
        });

        // Set message text based on view type
        if (holder instanceof senderViewHolder) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
        } else if (holder instanceof receiverViewHolder) {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModelClass messages = messagesAdapterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    // Sender layout view holder
    class senderViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    // Receiver layout view holder
    class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView msgtxt;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}