    package com.example.webchatapp;

    import android.os.Bundle;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.cardview.widget.CardView;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.Date;

    public class ChatWindow extends AppCompatActivity {
        private String receiverUid;
        private String receiverName;
        private String senderUid;
        private TextView receiverNameTextView;
        private CardView sendButton;
        private EditText messageEditText;
        private RecyclerView messageRecyclerView;

        private FirebaseDatabase database;
        private FirebaseAuth firebaseAuth;

        private ArrayList<MessageModelClass> messagesArrayList;
        private MessageAdapter messagesAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chatwindow);

            // Initialize Firebase and UI components
            initializeFirebase();
            extractIntentExtras();
            initializeViews();
            setupRecyclerView();
            listenToMessages();
            setupSendMessageListener();
        }

        private void initializeFirebase() {
            database = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            senderUid = firebaseAuth.getUid();
        }

        private void extractIntentExtras() {
            receiverName = getIntent().getStringExtra("name");
            receiverUid = getIntent().getStringExtra("uid");
        }

        private void initializeViews() {
            receiverNameTextView = findViewById(R.id.rName);
            sendButton = findViewById(R.id.sendBtn);
            messageEditText = findViewById(R.id.textMsg);
            messageRecyclerView = findViewById(R.id.msgadpter);

            receiverNameTextView.setText(receiverName);
            messagesArrayList = new ArrayList<>();
        }

        private void setupRecyclerView() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            messageRecyclerView.setLayoutManager(linearLayoutManager);

            messagesAdapter = new MessageAdapter(this, messagesArrayList);
            messageRecyclerView.setAdapter(messagesAdapter);
        }

        private void listenToMessages() {
            String senderRoom = senderUid + receiverUid;
            DatabaseReference chatReference = database.getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages");

            chatReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messagesArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MessageModelClass message = dataSnapshot.getValue(MessageModelClass.class);
                        if (message != null) {
                            messagesArrayList.add(message);
                        }
                    }
                    messagesAdapter.notifyDataSetChanged();
                    scrollToLatestMessage();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ChatWindow.this,
                            "Failed to load messages",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void setupSendMessageListener() {
            sendButton.setOnClickListener(view -> {
                String message = messageEditText.getText().toString().trim();

                if (message.isEmpty()) {
                    Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessage(message);
            });
        }

        private void sendMessage(String message) {
            messageEditText.setText(""); // Clear input

            Date date = new Date();
            MessageModelClass messageModel = new MessageModelClass(message, senderUid, date.getTime());

            String senderRoom = senderUid + receiverUid;
            String receiverRoom = receiverUid + senderUid;

            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .push()
                    .setValue(messageModel)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(messageModel);
                        } else {
                            Toast.makeText(this,
                                    "Failed to send message",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void scrollToLatestMessage() {
            if (!messagesArrayList.isEmpty()) {
                messageRecyclerView.scrollToPosition(messagesArrayList.size() - 1);
            }
        }
    }