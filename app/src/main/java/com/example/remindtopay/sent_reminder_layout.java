package com.example.remindtopay;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class sent_reminder_layout extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef  ;


    private sentReminderAdapter adapter;
    private int holderPosition;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_reminder);
        mAuth = FirebaseAuth.getInstance();
        String username= mAuth.getCurrentUser().getEmail();
        notebookRef= db.collection("reminder").document(username).collection("sentReminders");
        setUpRecyclerView();
    }
    private void setUpRecyclerView(){

        Query query = notebookRef.orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<sentReminder> options = new FirestoreRecyclerOptions.Builder<sentReminder>().setQuery(query,sentReminder.class).build();
        adapter = new sentReminderAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.sentReminder_recyclerView);
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
                holderPosition = viewHolder.getAdapterPosition();
                openDialog();
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                //adapter.deleteItem(viewHolder.getAdapterPosition());
            }

        }
        ).attachToRecyclerView(recyclerView);
    }


    public void openDialog(){
        DeleteDialog dialog = new DeleteDialog();
        dialog.show(getSupportFragmentManager(),"Delete Dialog");
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

    @Override
    public void onYesClicked() {
        adapter.deleteItem(holderPosition);
    }

    @Override
    public void noNoClicked() {
        Toast.makeText(this, "Action Canceled", Toast.LENGTH_SHORT).show();
    }
}
