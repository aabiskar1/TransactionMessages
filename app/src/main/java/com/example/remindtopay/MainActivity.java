package com.example.remindtopay;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tfb.fbtoast.FBToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView txtReceiverEmail, txtBankname, txtAccountname, txtAccountNumber, txtAmount,txtNotes;
    TextView txtReminderSentCount, txtProfileName,txtReminderReceivedCount;
    Button remindButton,saveDataButton;
    FloatingActionButton fab_speed_contact,fab_settings;
    DatabaseReference databaseReference;


    //Fields related to database
    private FirebaseFirestore dbSentReminder;
    private FirebaseFirestore dbCheckDocExists;
    private FirebaseFirestore dbReceivedReminder;
    private FirebaseFirestore dbEmailHistory;
    private FirebaseFirestore dbBankNameHistory;
    private FirebaseFirestore dbAccountNumberHisotry;
    private FirebaseFirestore dbAccountNameHistory;
    private FirebaseFirestore dbUserDetails;


    private static final String TAG = "MainActivity";
    public static final String KEY_RECEIVEREMAIL = "receiverEmail";
    public static final String KEY_BANKNAME = "bankName";
    public static final String KEY_ACCOUNTNAME = "accountName";
    public static final String KEY_ACCOUNTNUMBER = "accountNumber";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_NOTES = "notes";


    private FirebaseAuth mAuth;
    private CollectionReference notebookRefSent;
    private CollectionReference notebookRefReceived;
    private DocumentReference checkDocExistsRef;
    private DocumentReference userDetailsRef;
    private CollectionReference historyEmailRef,historyBankNameRef,historyAccountNumberRef,historyAccountNameRef;

    LinearLayout sentReminderLayoutBtn;
    LinearLayout receivedReminderLayoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //Firestore instance for database
        dbSentReminder = FirebaseFirestore.getInstance();
        dbReceivedReminder = FirebaseFirestore.getInstance();
        dbAccountNameHistory= FirebaseFirestore.getInstance();
        dbAccountNumberHisotry= FirebaseFirestore.getInstance();
        dbBankNameHistory= FirebaseFirestore.getInstance();
        dbEmailHistory= FirebaseFirestore.getInstance();
        dbCheckDocExists = FirebaseFirestore.getInstance();
        dbUserDetails = FirebaseFirestore.getInstance();



        fab_speed_contact = findViewById(R.id.fabitem_speedContact);
        fab_settings = findViewById(R.id.fabItem_settings);

        txtReceiverEmail = findViewById(R.id.mainpage_receiverEmail);
        txtBankname = findViewById(R.id.mainpage_bankname);
        txtAccountname = findViewById(R.id.mainpage_accountname);
        txtAccountNumber = findViewById(R.id.mainpage_accountnumber);
        txtAmount = findViewById(R.id.mainpage_amount);
        txtNotes = findViewById(R.id.mainpage_notes);
        remindButton = findViewById(R.id.btnRemind);
        saveDataButton = findViewById(R.id.btnSaveData);
        sentReminderLayoutBtn = findViewById(R.id.profile_reminderSent_layout);
        receivedReminderLayoutBtn = findViewById(R.id.profile_reminderReceived_layout);
        txtProfileName = findViewById(R.id.profile_username);
        txtReminderSentCount = findViewById(R.id.profile_reminderSentCount);
        txtReminderReceivedCount = findViewById(R.id.profile_reminderReceivedCount);

       // txtProfileName.setText(mAuth.getCurrentUser().toString());
        String username = mAuth.getCurrentUser().getEmail();

        //database references
        checkDocExistsRef =  dbCheckDocExists.collection("details").document(username);
        notebookRefSent = dbSentReminder.collection("reminder").document(username).collection("sentReminders");
        notebookRefReceived = dbReceivedReminder.collection("reminder").document(username).collection("receivedReminders");
        historyEmailRef =     dbEmailHistory.collection("history").document(username).collection("sent_email_history");
        historyBankNameRef = dbBankNameHistory.collection("history").document(username).collection("sent_bankName_history");
        historyAccountNumberRef = dbAccountNumberHisotry.collection("history").document(username).collection("sent_accountNumber_history");
        historyAccountNameRef= dbAccountNameHistory.collection("history").document(username).collection("sent_accountName_history");
        userDetailsRef =  dbUserDetails.collection("details").document(username);

        //getting loaded details from loading card
        Bundle extras = getIntent().getExtras();
        if (extras != null) {                                                   //if the bundle is not empty retireving data
            String accountName = extras.getString("accountName");
            String accountNumber = extras.getString("accountNumber");
            String bankName = extras.getString("bankName");
            String receiverEmail = extras.getString("receiverEmail");


            // Using picasso library to show the images

            txtReceiverEmail.setText(receiverEmail);
            txtBankname.setText(bankName);
            txtAccountname.setText(accountName);
            txtAccountNumber.setText(accountNumber);
            //The key argument here must match that used in the other activity
        }




        //setting up history for email
  historyEmailRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // String[] historyEmail;
                List<String> historyEmail = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    sentReminder SentReminder = documentSnapshot.toObject(sentReminder.class);
                    String tempEmail = SentReminder.getReceiverEmail();
                    historyEmail.add(tempEmail);
                }

                txtReceiverEmail.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,historyEmail));
            }
        });

        historyAccountNameRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // String[] historyEmail;
                List<String> history = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    sentReminder SentReminder = documentSnapshot.toObject(sentReminder.class);
                    String tempAccName = SentReminder.getAccountName();
                    history.add(tempAccName);
                }

                txtAccountname.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,history));
            }
        });

        historyAccountNumberRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // String[] historyEmail;
                List<String> history = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    sentReminder SentReminder = documentSnapshot.toObject(sentReminder.class);
                    String tempAccNum = SentReminder.getAccountNumber();
                    history.add(tempAccNum);
                }
                txtAccountNumber.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,history));
            }
        });
        historyBankNameRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // String[] historyEmail;
                List<String> history = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    sentReminder SentReminder = documentSnapshot.toObject(sentReminder.class);
                    String tempBankName = SentReminder.getBankName();
                    history.add(tempBankName);
                }

                txtBankname.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,history));
            }
        });






//test code
        String[] testarray = new String[]{"test1", "test2", "test3", "test3", "test3", "test3", "test3", "test3", "test3", "test3", "test3", "test3"};

        //txtReceiverEmail.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,testarray));
       // txtAmount.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,testarray));



        //counting number of sent reminder items
        sentReminderCounterUpdate();
//        notebookRefSent.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int count = 0;
//                            for (DocumentSnapshot document : task.getResult()) {
//                                count=count+1;
//                                txtReminderSentCount.setText(count+"");
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
        receivedReminderCounterUpdate();

        FirebaseApp.initializeApp(this);
        // Write a message to the database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        // item touch listener
        txtAccountNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }

        });

        txtAccountNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }
        });

        txtBankname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }
        });

        txtAccountname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }

        });



        remindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sentReminderCounterUpdate();
                receivedReminderCounterUpdate();
                String receiverEmail = txtReceiverEmail.getText().toString().toLowerCase().trim();
                dbCheckDocExists.collection("details").document(receiverEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Boolean checkFlag = document.exists();


                            if (document.exists()) {
                                addRemind();
                                FBToast.successToast(MainActivity.this," Reminder Added" , FBToast.LENGTH_SHORT);
                            } else {
                                FBToast.errorToast(MainActivity.this," User does not exists", FBToast.LENGTH_SHORT);
                            }
                        } else {
                            FBToast.errorToast(MainActivity.this,"Error: "+task.getException(),FBToast.LENGTH_SHORT);
                        }
                    }
                });

            }
        });

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                sentReminderCounterUpdate();
                receivedReminderCounterUpdate();
            }
        });

        sentReminderLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentReminderCounterUpdate();
                Intent myIntent = new Intent(getApplicationContext(), sent_reminder_layout.class);
                startActivity(myIntent);
            }
        });

        receivedReminderLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FBToast.infoToast(MainActivity.this, "Received Reminder layout clicked", Toast.LENGTH_SHORT);
                receivedReminderCounterUpdate();
                Intent myIntent = new Intent(getApplicationContext(), received_reminder_layout.class);
                startActivity(myIntent);
            }
        });

        fab_speed_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), speed_contact_list.class);
                startActivity(myIntent);


            }
        });

        fab_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(myIntent);
            }
        });


    }

    public void onItemClick(AdapterView<?> adapterViewIn, View viewIn, int indexSelected, long arg3) {
        InputMethodManager imm = (InputMethodManager) getSystemService(viewIn.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewIn.getApplicationWindowToken(), 0);
        // whatever else should be done
    }

    public void addRemind() {
        String userLocation = mAuth.getCurrentUser().getEmail();



        String reminded_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String receiverEmail = txtReceiverEmail.getText().toString().toLowerCase().trim();
        String bankName = txtBankname.getText().toString().toLowerCase().trim();
        String accountName = txtAccountname.getText().toString().toLowerCase().trim();
        String accountNumber = txtAccountNumber.getText().toString().toLowerCase().trim();
        String amount = txtAmount.getText().toString().toLowerCase().trim();
        String notes = txtNotes.getText().toString().toLowerCase().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        FBToast.warningToast(this, "Button Clicked", Toast.LENGTH_SHORT);


        if (!validateInputs(receiverEmail, bankName, accountName, accountNumber, amount, reminded_date)) {
            Date currentTime = Calendar.getInstance().getTime();
            String currentDateAndTime = date +" "+ currentTime.toString();
         // Storing the reminder
            Map<String, Object> reminder = new HashMap<>();

            reminder.put(KEY_RECEIVEREMAIL, receiverEmail);
            reminder.put(KEY_BANKNAME, bankName);
            reminder.put(KEY_ACCOUNTNAME, accountName);
            reminder.put(KEY_ACCOUNTNUMBER, accountNumber);
            reminder.put(KEY_AMOUNT, amount);
            reminder.put(KEY_DATE, date);
            reminder.put(KEY_NOTES, notes);
            reminder.put(KEY_STATUS, "in progress");

            String currentUser =  mAuth.getCurrentUser().getEmail();
            Map<String, Object> reminded_reminder = new HashMap<>();

            reminded_reminder.put(KEY_RECEIVEREMAIL,currentUser);
            reminded_reminder.put(KEY_BANKNAME, bankName);
            reminded_reminder.put(KEY_ACCOUNTNAME, accountName);
            reminded_reminder.put(KEY_ACCOUNTNUMBER, accountNumber);
            reminded_reminder.put(KEY_AMOUNT, amount);
            reminded_reminder.put(KEY_DATE, date);
            reminded_reminder.put(KEY_NOTES, notes);
            reminded_reminder.put(KEY_STATUS, "in progress");



            dbSentReminder.collection("reminder").document(userLocation).collection("sentReminders").document(currentDateAndTime).set(reminder)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FBToast.successToast(MainActivity.this, "Reminder Sent", Toast.LENGTH_SHORT);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FBToast.errorToast(MainActivity.this, "Error!!", Toast.LENGTH_SHORT);
                            Log.d(TAG, e.toString());
                        }
                    });

            dbReceivedReminder.collection("reminder").document(receiverEmail).collection("receivedReminders").document(currentDateAndTime).set(reminded_reminder)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FBToast.successToast(MainActivity.this, "Reminder Received", Toast.LENGTH_SHORT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FBToast.errorToast(MainActivity.this, "Reminder not received", Toast.LENGTH_SHORT);
                }
            });
            //Storing the history

            Map<String, Object> historyReceiverEmail = new HashMap<>();
            historyReceiverEmail.put(KEY_RECEIVEREMAIL, receiverEmail);
            dbEmailHistory.collection("history").document(userLocation).collection("sent_email_history").document(receiverEmail).set(historyReceiverEmail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "email history saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });


            Map<String, Object> historyBankName = new HashMap<>();
            historyBankName.put(KEY_BANKNAME, bankName);
            dbBankNameHistory.collection("history").document(userLocation).collection("sent_bankName_history").document(bankName).set(historyBankName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "bank name history saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });





            Map<String, Object> historyAccountNumber = new HashMap<>();
            historyAccountNumber.put(KEY_ACCOUNTNUMBER,accountNumber);

            dbAccountNumberHisotry.collection("history").document(userLocation).collection("sent_accountNumber_history").document(accountNumber).set(historyAccountNumber)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FBToast.successToast(MainActivity.this," acc num", FBToast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FBToast.successToast(MainActivity.this," Error!!!", FBToast.LENGTH_SHORT);
                            Log.d(TAG, e.toString());
                        }
                    });



            Map<String, Object> historyAccountName = new HashMap<>();
            historyAccountName.put(KEY_ACCOUNTNAME,accountName);
            dbAccountNameHistory.collection("history").document(userLocation).collection("sent_accountName_history").document(accountName).set(historyAccountName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FBToast.successToast(MainActivity.this," acc name History saved", FBToast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() != null){
            loadName();
        }
    }

    public void saveData() {
        String userLocation = mAuth.getCurrentUser().getEmail();
        Toast.makeText(this, "worked first part", Toast.LENGTH_SHORT).show();

        String reminded_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String receiverEmail = txtReceiverEmail.getText().toString().toLowerCase().trim();
        String bankName = txtBankname.getText().toString().toLowerCase().trim();
        String accountName = txtAccountname.getText().toString().toLowerCase().trim();
        String accountNumber = txtAccountNumber.getText().toString().toLowerCase().trim();
        String amount = txtAmount.getText().toString().toLowerCase().trim();
        String notes = txtNotes.getText().toString().toLowerCase().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();


        if (!validateInputsSaveData(bankName, accountName, accountNumber, amount, reminded_date)) {
            Date currentTime = Calendar.getInstance().getTime();
            String currentDateAndTime = date +" "+ currentTime.toString();
            // Storing the reminder
            Map<String, Object> reminder = new HashMap<>();

            reminder.put(KEY_RECEIVEREMAIL, receiverEmail);
            reminder.put(KEY_BANKNAME, bankName);
            reminder.put(KEY_ACCOUNTNAME, accountName);
            reminder.put(KEY_ACCOUNTNUMBER, accountNumber);
            reminder.put(KEY_AMOUNT, amount);
            reminder.put(KEY_DATE, date);
            reminder.put(KEY_NOTES, notes);
            reminder.put(KEY_STATUS, "in progress");

            dbSentReminder.collection("reminder").document(userLocation).collection("sentReminders").document(currentDateAndTime).set(reminder)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Reminder Sent", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });

            //Storing the history



            Map<String, Object> historyBankName = new HashMap<>();
            historyBankName.put(KEY_BANKNAME, bankName);
            dbBankNameHistory.collection("history").document(userLocation).collection("sent_bankName_history").document(bankName).set(historyBankName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "bank name history saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });





            Map<String, Object> historyAccountNumber = new HashMap<>();
            historyAccountNumber.put(KEY_ACCOUNTNUMBER,accountNumber);

            dbAccountNumberHisotry.collection("history").document(userLocation).collection("sent_accountNumber_history").document(accountNumber).set(historyAccountNumber)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, " acc num history saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });



            Map<String, Object> historyAccountName = new HashMap<>();
            historyAccountName.put(KEY_ACCOUNTNAME,accountName);
            dbAccountNameHistory.collection("history").document(userLocation).collection("sent_accountName_history").document(accountName).set(historyAccountName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "acc name history saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });


        }
    }

    private boolean validateInputs(String receiverEmail, String bankName, String
            accountName, String accountNumber, String amount, String date) {
        if (receiverEmail.isEmpty()) {
            txtReceiverEmail.setError("Name required");
            txtReceiverEmail.requestFocus();
            return true;
        }

        if (bankName.isEmpty()) {
            txtBankname.setError("Bank Name required");
            txtBankname.requestFocus();
            return true;
        }

        if (accountName.isEmpty()) {
            txtAccountname.setError("Description required");
            txtAccountname.requestFocus();
            return true;
        }

        if (accountNumber.isEmpty()) {
            txtAccountNumber.setError("Price required");
            txtAccountNumber.requestFocus();
            return true;
        }

        if (amount.isEmpty()) {
            txtAmount.setError("Amount required");
            txtAmount.requestFocus();
            return true;
        }

        return false;
    }


    private boolean validateInputsSaveData(String bankName, String
            accountName, String accountNumber, String amount, String date) {
        if (bankName.isEmpty()) {
            txtBankname.setError("Bank Name required");
            txtBankname.requestFocus();
            return true;
        }

        if (accountName.isEmpty()) {
            txtAccountname.setError("Description required");
            txtAccountname.requestFocus();
            return true;
        }

        if (accountNumber.isEmpty()) {
            txtAccountNumber.setError("Price required");
            txtAccountNumber.requestFocus();
            return true;
        }

        if (amount.isEmpty()) {
            txtAmount.setError("Amount required");
            txtAmount.requestFocus();
            return true;
        }

        return false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        sentReminderCounterUpdate();
        receivedReminderCounterUpdate();
    }

    public void sentReminderCounterUpdate(){
        notebookRefSent.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count=count+1;
                                txtReminderSentCount.setText(count+"");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void receivedReminderCounterUpdate(){
        notebookRefReceived.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count=count+1;
                                txtReminderReceivedCount.setText(count+"");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public void loadName(){
        userDetailsRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                    String first_name = userDetails.getFirst_name();
                    txtProfileName.setText(first_name.toString());
                }
            }
        });
    }

}
