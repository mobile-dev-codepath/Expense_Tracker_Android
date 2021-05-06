package com.codepath.expensetracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.expensetracker.Receipt;
import com.codepath.expensetracker.ReceiptsAdapter;
import com.codepath.expensetracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsFragment extends Fragment {

    public static final String TAG = "ReceiptsFragment";
    private RecyclerView rvReceipts;
    protected ReceiptsAdapter adapter;
    protected List<Receipt> allReceipts;
    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton fabAddReceipt;
    public FragmentManager fragmentManager;

    public ReceiptsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvReceipts = view.findViewById(R.id.rvReceipts);

        allReceipts = new ArrayList<>();
        adapter = new ReceiptsAdapter(getContext(), allReceipts);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data!");
                queryReceipts();
            }
        });

        // Steps to use the recycler view:
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvReceipts.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryReceipts();

        fabAddReceipt = view.findViewById(R.id.fabAddReceipt);
        fabAddReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ComposeFragment();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });
        fragmentManager.beginTransaction().replace(R.id.flContainer, (new ReceiptsFragment())).commit();
    }

    protected void queryReceipts() {
        ParseQuery<Receipt> query = ParseQuery.getQuery(Receipt.class);
        query.include(Receipt.KEY_USER);
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
                    Log.i(TAG, "Store name: " + receipt.getStoreName() + ", store type: " + receipt.getStoreType() + ", transaction cost: " + receipt.getTransactionCost() + ", transaction date: " + receipt.getTransactionDate() + ", username: " + receipt.getUser().getUsername());
                }
                adapter.clear();
                adapter.addAll(receipts);
                swipeContainer.setRefreshing(false);
            }
        });
    }
}