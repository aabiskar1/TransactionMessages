package com.example.remindtopay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class speed_contact_list extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference speedContactListRef  ;


    private speedContactAdapter adapter;
    private FloatingActionButton addSpeedContactFab;
    private ImageButton addEntryBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_contact_list);


       // addSpeedContactFab = findViewById(R.id.addSpeedContactFab);
        addEntryBtn = findViewById(R.id.speedContacts_addEntryBtn);

        mAuth = FirebaseAuth.getInstance();
        String username= mAuth.getCurrentUser().getEmail();
        speedContactListRef= db.collection("speed_contact").document(username).collection("contacts");
        setUpspeedContactRecyclerView();


        addEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), speed_contact_entry_layout.class);
                startActivity(myIntent);
            }
        });


    }

    private void setUpspeedContactRecyclerView(){
        Toast.makeText(this, speedContactListRef.getPath(), Toast.LENGTH_SHORT).show();
        Query query = speedContactListRef.orderBy("amount", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<speedContact> options = new FirestoreRecyclerOptions.Builder<speedContact>().setQuery(query,speedContact.class).build();
        adapter = new speedContactAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.speed_contact_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new speedContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                speedContact contact = documentSnapshot.toObject(speedContact.class);
               String accountName = contact.getAccountName();
               String accountNumber = contact.getAccountNumber();
               String bankName = contact.getBankName();
               String receiverEmail = contact.getReceiverEmail();
                Toast.makeText(speed_contact_list.this, "Data Loaded", Toast.LENGTH_SHORT).show();
                finish();

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                myIntent.putExtra("accountName", accountName);
                myIntent.putExtra("accountNumber", accountNumber);
                myIntent.putExtra("bankName", bankName);
                myIntent.putExtra("receiverEmail", receiverEmail);
                startActivity(myIntent);

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
        adapter.stopListening();
    }
}
