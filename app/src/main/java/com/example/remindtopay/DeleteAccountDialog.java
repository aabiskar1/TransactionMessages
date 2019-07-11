package com.example.remindtopay;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tfb.fbtoast.FBToast;

public class DeleteAccountDialog extends AppCompatDialogFragment {

    private DeleteAccountListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
               .setTitle("DELETE ACCOUNT")
               .setMessage("ONCE THE ACCOUNT IS DELETED ALL THE DATA IS ERASED PERMANENTLY")
               .setPositiveButton("DELETE ACCOUNT", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       listener.OnYesClicked();
                   }
               })


               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       listener.OnNoClicked();
                   }
               })
               ;

            return builder.create();

    }

    public interface DeleteAccountListener{
        void OnYesClicked();
        void OnNoClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteAccountListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement ExampleDialogListener");
        }

    }
}
