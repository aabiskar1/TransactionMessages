package com.example.remindtopay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class sent_reminder_layout extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef ;


    private sentReminderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_reminder);
        mAuth = FirebaseAuth.getInstance();
        String username= mAuth.getCurrentUser().getEmail();
        notebookRef= db.collection("reminder").document(username).collection("sentReminders");
        Toast.makeText(this, notebookRef.toString(), Toast.LENGTH_SHORT).show();
        setUpRecyclerView();
    }
    private void setUpRecyclerView(){
        RecyclerView recyclerView;
        Query query = notebookRef.orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<sentReminder> options = new FirestoreRecyclerOptions.Builder<sentReminder>().setQuery(query,sentReminder.class).build();
        adapter = new sentReminderAdapter(options);
        recyclerView = findViewById(R.id.sentReminder_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
