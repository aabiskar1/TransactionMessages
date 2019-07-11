package com.example.remindtopay;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class speedContactAdapter extends FirestoreRecyclerAdapter<speedContact,speedContactAdapter.speedContactHolder> {

    private OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public speedContactAdapter(@NonNull FirestoreRecyclerOptions<speedContact> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull speedContactHolder holder, int position, @NonNull speedContact model) {

        holder.txt_receiverEmail.setText(model.getReceiverEmail());
        holder.txt_bankname.setText(model.getBankName());
        holder.txt_accountName.setText(model.getAccountName());
        holder.txt_accountNumber.setText(model.getAccountNumber());
        holder.txt_amount.setText(model.getAmount());
    }

    @NonNull
    @Override
    public speedContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_speed_contact_card,
                viewGroup, false);
        return new speedContactHolder(v);
    }

    class speedContactHolder extends RecyclerView.ViewHolder{

        TextView txt_receiverEmail;
        TextView txt_accountName;
        TextView txt_accountNumber;
        TextView txt_amount;
        TextView txt_bankname;
        Button loadBtn;


        public speedContactHolder(@NonNull View itemView) {
            super(itemView);
            txt_receiverEmail = itemView.findViewById(R.id.speedCard_toRemind);
            txt_accountName = itemView.findViewById(R.id.speedCard_accountName);
            txt_accountNumber = itemView.findViewById(R.id.speedCard_accountNumber);
            txt_amount = itemView.findViewById(R.id.speedCard_Title);
            txt_bankname = itemView.findViewById(R.id.speedCard_bankName);
            loadBtn = itemView.findViewById(R.id.speedCard_loadBtn);

            loadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener !=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });



        }

    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
