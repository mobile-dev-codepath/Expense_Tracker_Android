package com.codepath.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Receipt> query = ParseQuery.getQuery(Receipt.class);
        query.findInBackground(new FindCallback<Receipt>() {
            @Override
            public void done(List<Receipt> Receipts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Unable to get Post");
                    return;
                }
                for (Receipt receipt:Receipts){
                    Log.i(TAG, "Receipt Info:" + receipt.getDescription());
                }
            }
        });
    }
}