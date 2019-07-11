package com.example.remindtopay;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfb.fbtoast.FBToast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseAuth mAuth;
    private TextView dateofbirthTxt;
    private ImageButton registerBtn;
    private EditText emailTxt, passwordTxt, firstnameTxt, lastnameTxt;
    private FirebaseFirestore dbUserDetails;
    private DocumentReference userDetailsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        dateofbirthTxt = (TextView) findViewById(R.id.register_txtDateOfBirth);


        emailTxt = findViewById(R.id.register_txtEmail);
        passwordTxt = findViewById(R.id.register_txtPassword);
        firstnameTxt = findViewById(R.id.register_txtFirstName);
        lastnameTxt = findViewById(R.id.register_txtLastName);
        registerBtn = findViewById(R.id.register_imgBtnRegister);


        dateofbirthTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale aLocale;
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONDAY);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dateofbirthTxt.setText(day + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }


    public void registerUser() {


        final String email, password, firstName, lastName, dateofbirth;
        email = emailTxt.getText().toString().trim().toLowerCase();
        password = passwordTxt.getText().toString().trim().toLowerCase();
        firstName = firstnameTxt.getText().toString().trim();
        lastName = lastnameTxt.getText().toString().trim();
        dateofbirth = dateofbirthTxt.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FBToast.successToast(getApplicationContext(), "User Registered ", Toast.LENGTH_SHORT);

                            dbUserDetails = FirebaseFirestore.getInstance();
                            userDetailsRef = dbUserDetails.collection("details").document(email);
                            String registered_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            Map<String, Object> detailsList = new HashMap<>();
                            detailsList.put("first_name", firstName);
                            detailsList.put("last_name", lastName);
                            detailsList.put("dateOfBirth", dateofbirth);
                            detailsList.put("email", email);
                            detailsList.put("registered_date", registered_date);

                            userDetailsRef.set(detailsList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                }
                            })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        FBToast.errorToast(RegisterActivity.this, "SOME ERROR OCCUR....Details not saved ",Toast.LENGTH_SHORT);
                                    }
                                });


                        } else {
                            // If sign in fails, display a message to the user.
                            FBToast.errorToast(RegisterActivity.this, "Authentication failed: " + task.getException(),
                                    Toast.LENGTH_SHORT);

                        }

                        // ...
                    }
                });


    }
}
