package com.codepath.expensetracker.fragments;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.expensetracker.R;
import com.codepath.expensetracker.Receipt;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewReceipt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewReceipt extends Fragment {

    private static final String TAG = "ViewReceipt";


    public static ViewReceipt newInstance() {
        ViewReceipt viewReceipt = new ViewReceipt();
        return viewReceipt;
    }

    ImageView ivImage;
    TextView tvUser;
    TextView tvName;
    TextView tvType;
    TextView tvCost;
    TextView tvDate;


    public ViewReceipt() {
        // Required empty public constructor
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivImage = view.findViewById(R.id.ivImage);
        tvUser = view.findViewById(R.id.tvUser);
        tvName = view.findViewById(R.id.tvName);
        tvType = view.findViewById(R.id.tvType);
        tvCost = view.findViewById(R.id.tvCost);
        tvDate = view.findViewById(R.id.tvDate);

        Receipt receipt;
        receipt = Parcels.unwrap(getActivity().getIntent().getParcelableExtra("receipt"));
        ParseFile receiptImageParse = receipt.getImage();
        if (receiptImageParse != null) {
            receiptImageParse.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivImage.setImageBitmap(bmp);
                    } else {
                        ivImage.setImageResource(android.R.drawable.menuitem_background);
                    }
                }
            });
            tvUser.setText((CharSequence) receipt.getUser());
            tvName.setText((CharSequence) receipt.getStoreName());
            tvType.setText((CharSequence) receipt.getStoreType());
            tvCost.setText((CharSequence) receipt.getTransactionCost());
            tvDate.setText((CharSequence) receipt.getTransactionDate());
        }
    }
}