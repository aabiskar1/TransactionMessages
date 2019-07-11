package com.example.remindtopay;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class receivedReminderAdapter extends FirestoreRecyclerAdapter<receivedReminder, receivedReminderAdapter.ItemHolder> {
        private OnConfirmBtnClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public receivedReminderAdapter(@NonNull FirestoreRecyclerOptions<receivedReminder> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull receivedReminder model) {
        holder.received_txt_receiverEmail.setText(model.getReceiverEmail());
        holder.received_txt_bankname.setText(model.getBankName());
        holder.received_txt_accountName.setText(model.getAccountName());
        holder.received_txt_accountNumber.setText(model.getAccountNumber());
        holder.received_txt_amount.setText(model.getAmount());
        holder.received_txt_status.setText(model.getStatus().toUpperCase());
        holder.received_txt_date.setText(model.getDate());
        holder.received_txt_notes.setText(model.getNotes());
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_received,
                viewGroup, false);
        return new ItemHolder(v) ;
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class ItemHolder extends RecyclerView.ViewHolder{
        TextView received_txt_receiverEmail;
        TextView received_txt_accountName;
        TextView received_txt_accountNumber;
        TextView received_txt_amount;
        TextView received_txt_bankname;
        TextView received_txt_status;
        TextView received_txt_date;
        TextView received_txt_notes;
        Button confirmBtn;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            received_txt_receiverEmail = itemView.findViewById(R.id.cardReceived_toRemind);
            received_txt_accountName = itemView.findViewById(R.id.cardReceived_accountName);
            received_txt_accountNumber = itemView.findViewById(R.id.cardReceived_accountNumber);
            received_txt_amount = itemView.findViewById(R.id.cardReceived_amount);
            received_txt_bankname = itemView.findViewById(R.id.cardReceived_bankName);
            received_txt_status = itemView.findViewById(R.id.cardReceived_status);
            received_txt_date = itemView.findViewById(R.id.cardReceived_date);
            received_txt_notes = itemView.findViewById(R.id.cardReceived_notes);
            confirmBtn = itemView.findViewById(R.id.receiveReminder_confirmBtn);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onBtnClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnConfirmBtnClickListener{
        void onBtnClick(DocumentSnapshot documentSnapshot,int position);


    }
    public void setOnBtnClickListener(OnConfirmBtnClickListener listener){
        this.listener=listener;

    }

}
