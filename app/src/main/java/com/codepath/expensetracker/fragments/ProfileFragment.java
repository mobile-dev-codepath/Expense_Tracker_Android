package com.codepath.expensetracker.fragments;

import android.util.Log;

import com.codepath.expensetracker.Receipt;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends ReceiptsFragment {
    @Override
    protected void queryReceipts() {
        ParseQuery<Receipt> query = ParseQuery.getQuery(Receipt.class);
        query.include(Receipt.KEY_USER);
        query.whereEqualTo(Receipt.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Receipt.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Receipt>() {
            @Override
            public void done(List<Receipt> receipts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting receipts", e);
                    return;
                }
                for (Receipt receipt : receipts) {
                    Log.i(TAG, "Receipt: " + receipt.getDescription() + ", username: " + receipt.getUser().getUsername());
                }
                allReceipts.addAll(receipts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
