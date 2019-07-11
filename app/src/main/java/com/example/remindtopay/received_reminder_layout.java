package com.example.remindtopay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.tfb.fbtoast.FBToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class received_reminder_layout extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef  ;

    private DocumentReference notebookRefReceived;
    private DocumentReference notebookTargetReminder;
    private FirebaseFirestore dbReceivedReminder;
    private FirebaseFirestore dbTargetReminder;
    private receivedReminderAdapter adapter;
    public static final String KEY_STATUS = "status";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_reminder);
        mAuth = FirebaseAuth.getInstance();
        String username= mAuth.getCurrentUser().getEmail();
        notebookRef= db.collection("reminder").document(username).collection("receivedReminders");
        FBToast.infoToast(this, notebookRef.getPath(), Toast.LENGTH_SHORT);

        dbReceivedReminder = FirebaseFirestore.getInstance();
        dbTargetReminder = FirebaseFirestore.getInstance();
        setUpRecyclerView();
    }
    private void setUpRecyclerView(){

        Query query = notebookRef.orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<receivedReminder> options = new FirestoreRecyclerOptions.Builder<receivedReminder>()
                .setQuery(query, receivedReminder.class)
                .build();
        adapter = new  receivedReminderAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.receivedReminder_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnBtnClickListener(new receivedReminderAdapter.OnConfirmBtnClickListener() {
            @Override
            public void onBtnClick(DocumentSnapshot documentSnapshot, int position) {
                receivedReminder reminder = documentSnapshot.toObject(receivedReminder.class);
                String id = documentSnapshot.getId();
                String email = mAuth.getCurrentUser().getEmail();
                String targetEmail= reminder.getReceiverEmail().toString();
                DocumentReference ref = documentSnapshot.getReference();
                notebookTargetReminder = dbTargetReminder.collection("reminder").document(targetEmail).collection("sentReminders").document(id);
                dbReceivedReminder = FirebaseFirestore.getInstance();
                dbTargetReminder = FirebaseFirestore.getInstance();
                Map<String ,Object> note = new HashMap<>();
                note.put(KEY_STATUS,"Transaction Done");
                notebookTargetReminder.set(note, SetOptions.merge());
                ref.set(note, SetOptions.merge());
//                String yourEmail = notebookRefReceived = dbReceivedReminder.collection("reminder").document(email).collection("receivedReminders").document(id);

                FBToast.infoToast(received_reminder_layout.this, "Email:"+ref+"---------"+"Target Email:"+targetEmail, Toast.LENGTH_SHORT);
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }


}
