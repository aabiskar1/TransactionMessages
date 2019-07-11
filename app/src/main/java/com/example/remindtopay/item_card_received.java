package com.example.remindtopay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class item_card_received extends AppCompatActivity {
    private DocumentReference notebookRefReceived;
    private DocumentReference notebookTargetReminder;
    private FirebaseFirestore dbReceivedReminder;
    private FirebaseFirestore dbTargetReminder;

    Button confirm;
    TextView idTxtView;
    TextView targetTxtView;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_card_received);


//        idTxtView = findViewById(R.id.cardReceived_date);
//        String id = idTxtView.getText().toString();
//        targetTxtView = findViewById(R.id.cardReceived_toRemind);
//        String targetEmail = targetTxtView.getText().toString();
//        notebookRefReceived = dbReceivedReminder.collection("reminder").document(username).collection("receivedReminders").document(id);
//        notebookTargetReminder = dbTargetReminder.collection("reminder").document(targetEmail).collection("sentReminders").document(id);


    }


}
