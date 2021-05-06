package com.codepath.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.expensetracker.fragments.BitmapScaler;
import com.codepath.expensetracker.fragments.ViewReceipt;
import com.parse.ParseFile;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import static java.security.AccessController.getContext;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder> {

    public static final int IMAGE_WIDTH = 200;
    private Context context;
    private List<Receipt> receipts;

    public ReceiptsAdapter(Context context, List<Receipt> receipts) {
        this.context = context;
        this.receipts = receipts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);
        holder.bind(receipt);
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        receipts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Receipt> receiptList) {
        receipts.addAll(receiptList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout receiptContainer;
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvStoreName;
        private TextView tvStoreType;
        private TextView tvTransactionCost;
        private TextView tvTransactionDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvStoreType = itemView.findViewById(R.id.tvStoreType);
            tvTransactionCost = itemView.findViewById(R.id.tvTransactionCost);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            receiptContainer = itemView.findViewById(R.id.receiptContainer);
        }

        public void bind(Receipt receipt) {

            tvStoreName.setText(receipt.getStoreName());
            tvStoreType.setText(receipt.getStoreName());
            tvTransactionCost.setText(receipt.getTransactionCost());
            tvTransactionDate.setText(receipt.getTransactionDate());
            tvUsername.setText(receipt.getUser().getUsername());
            ParseFile image = receipt.getImage();
            if (image != null) {
                Glide.with(context).load(receipt.getImage().getUrl()).into(ivImage);
            }

             receiptContainer.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent i = new Intent(context, ViewReceipt.class);
                     i.putExtra("receipt", Parcels.wrap(receipt));
                     context.startActivity(i);
                 }
             });


        }
    }

}
