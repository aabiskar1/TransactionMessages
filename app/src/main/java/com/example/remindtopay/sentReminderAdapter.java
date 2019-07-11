package com.example.remindtopay;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class sentReminderAdapter extends FirestoreRecyclerAdapter<sentReminder, sentReminderAdapter.itemHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public sentReminderAdapter(@NonNull FirestoreRecyclerOptions<sentReminder> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull itemHolder holder, int position, @NonNull sentReminder model) {
        holder.txt_receiverEmail.setText(model.getReceiverEmail());
        holder.txt_bankname.setText(model.getBankName());
        holder.txt_accountName.setText(model.getAccountName());
        holder.txt_accountNumber.setText(model.getAccountNumber());
        holder.txt_amount.setText(model.getAmount());
        holder.txt_status.setText(model.getStatus().toUpperCase());
        holder.txt_date.setText(model.getDate());
        holder.txt_notes.setText(model.getNotes());
    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card,
                viewGroup, false);
        return new itemHolder(v);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class itemHolder extends RecyclerView.ViewHolder {
        TextView txt_receiverEmail;
        TextView txt_accountName;
        TextView txt_accountNumber;
        TextView txt_amount;
        TextView txt_bankname;
        TextView txt_status;
        TextView txt_date;
        TextView txt_notes;



        public
        itemHolder(@NonNull View itemView) {
            super(itemView);
            txt_receiverEmail = itemView.findViewById(R.id.card_toRemind);
            txt_accountName = itemView.findViewById(R.id.card_accountName);
            txt_accountNumber = itemView.findViewById(R.id.card_accountNumber);
            txt_amount = itemView.findViewById(R.id.card_amount);
            txt_bankname = itemView.findViewById(R.id.card_bankName);
            txt_status = itemView.findViewById(R.id.card_status);
            txt_date = itemView.findViewById(R.id.card_date);
            txt_notes = itemView.findViewById(R.id.card_notes);
        }
    }
}
