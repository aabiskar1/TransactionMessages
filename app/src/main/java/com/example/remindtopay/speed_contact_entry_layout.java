package com.example.remindtopay;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfb.fbtoast.FBToast;

import java.util.HashMap;
import java.util.Map;

// AMOUNT IN THIS CLASS MEmaN
public class speed_contact_entry_layout extends AppCompatActivity {
    public static final String SPEED_KEY_RECEIVEREMAIL = "receiverEmail";
    public static final String SPEED_KEY_BANKNAME = "bankName";
    public static final String SPEED_KEY_ACCOUNTNAME = "accountName";
    public static final String SPEED_KEY_ACCOUNTNUMBER = "accountNumber";
    public static final String SPEED_KEY_AMOUNT = "amount";
    private LinearLayout speedContactEntryProgressbarLayout;

    private FirebaseFirestore dbSpeedContact;
    private FirebaseAuth mAuth;
    private CollectionReference notebookRefSpeedContact;


    EditText txtReceiverEmail, txtBankname, txtAccountname, txtAccountNumber, txtAmount;

    ImageButton addContactBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_contact_entry);
        dbSpeedContact = FirebaseFirestore.getInstance();

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        speedContactEntryProgressbarLayout = findViewById(R.id.speedContactEntry_dotsLayout);

        txtReceiverEmail =(EditText) findViewById(R.id.speedContact_receiverEmail);
        txtBankname = (EditText) findViewById(R.id.speedContact_bankname);
        txtAccountname = (EditText) findViewById(R.id.speedContact_accountname);
        txtAccountNumber = (EditText) findViewById(R.id.speedContact_accountnumber);
        txtAmount = (EditText) findViewById(R.id.speedContact_amount);
        addContactBtn = findViewById(R.id.speedContact_btnSaveContact);


        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpeedContact(v);
            }
        });
    }
    private void addSpeedContact(View v){


        String receiverEmail,bankName,accountName,accountNumber,amount;
        receiverEmail = txtReceiverEmail.getText().toString().trim();
        accountName = txtAccountname.getText().toString().trim();
        bankName = txtBankname.getText().toString().trim();
        accountNumber = txtAccountNumber.getText().toString().trim();
        amount = txtAmount.getText().toString().trim();

        String userLocation = mAuth.getCurrentUser().getEmail();


        if (!validateInputs(receiverEmail, bankName, accountName, accountNumber, amount)) {
            speedContactEntryProgressbarLayout.setVisibility(View.VISIBLE);
            Map<String, Object> speedContact = new HashMap<>();

            speedContact.put(SPEED_KEY_ACCOUNTNAME,accountName);
            speedContact.put(SPEED_KEY_ACCOUNTNUMBER,accountNumber);
            speedContact.put(SPEED_KEY_BANKNAME,bankName);
            speedContact.put(SPEED_KEY_RECEIVEREMAIL,receiverEmail);
            speedContact.put(SPEED_KEY_AMOUNT,amount);

            dbSpeedContact.collection("speed_contact").document(userLocation).collection("contacts").document().set(speedContact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FBToast.successToast(getApplicationContext(), "ENTRY SAVED", Toast.LENGTH_SHORT);
                            speedContactEntryProgressbarLayout.setVisibility(View.GONE);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FBToast.errorToast(speed_contact_entry_layout.this, "SOME ERROR OCCUR", Toast.LENGTH_SHORT);
                            speedContactEntryProgressbarLayout.setVisibility(View.GONE);
                        }
                    });
        }

    }



    private boolean validateInputs(String receiverEmail, String bankName, String
            accountName, String accountNumber, String amount) {
        if (receiverEmail.isEmpty()) {
            txtReceiverEmail.setError("Email required");
            txtReceiverEmail.requestFocus();
            return true;
        }

        if (bankName.isEmpty()) {
            txtBankname.setError("Bank Name required");
            txtBankname.requestFocus();
            return true;
        }

        if (accountName.isEmpty()) {
            txtAccountname.setError("Account Name required");
            txtAccountname.requestFocus();
            return true;
        }

        if (accountNumber.isEmpty()) {
            txtAccountNumber.setError("Account Number required");
            txtAccountNumber.requestFocus();
            return true;
        }

        if (amount.isEmpty()) {
            txtAmount.setError("Title required");
            txtAmount.requestFocus();
            return true;
        }

        return false;
    }

}
