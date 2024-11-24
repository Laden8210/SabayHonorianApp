package com.example.sabayhonorianapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sabayhonorianapp.R;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private final List<String> payments;

    public PaymentAdapter(List<String> payments) {
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_detail, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        String paymentDetail = payments.get(position);
        String[] details = paymentDetail.split(" - ");
        holder.tvCustomerName.setText(details[0]);
        holder.tvPaymentAmount.setText(details[1]);
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public void updatePayments(List<String> payments) {
        this.payments.clear();
        this.payments.addAll(payments);
        notifyDataSetChanged();
    }


    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvPaymentAmount;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvPaymentAmount = itemView.findViewById(R.id.tvPaymentAmount);
        }
    }
}
