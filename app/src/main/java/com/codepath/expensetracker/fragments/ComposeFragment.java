package com.codepath.expensetracker.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.expensetracker.Receipt;
import com.codepath.expensetracker.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int IMAGE_WIDTH = 200;
    private EditText etStoreName;
    private Spinner spnrStoreType;
    private EditText etTransactionCost;
    private EditText etTransactionDate;
    private Button btnCaptureImage;
    private ImageView ivReceiptImage;
    private Button btnSubmit;
    private File photoFile;
    private String photoFileName = "receipt.jpg";

    public ComposeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etStoreName = view.findViewById(R.id.etStoreName);
        spnrStoreType = view.findViewById(R.id.spnrStoreType);
        etTransactionCost = view.findViewById(R.id.etTransactionCost);
        etTransactionDate = view.findViewById(R.id.etTransactionDate);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivReceiptImage = view.findViewById(R.id.ivReceiptImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeName = etStoreName.getText().toString();
                String storeType = spnrStoreType.getSelectedItem().toString();
                String transactionCost = etTransactionCost.getText().toString();
                String transactionDate = etTransactionDate.getText().toString();
                if (storeName.isEmpty()) {
                    Toast.makeText(getContext(), "Store name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (storeType.isEmpty()) {
                    Toast.makeText(getContext(), "Store type cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (transactionCost.isEmpty()) {
                    Toast.makeText(getContext(), "Transaction cost cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (transactionDate.isEmpty()) {
                    Toast.makeText(getContext(), "Transaction date cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivReceiptImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveReceipt(storeName, storeType, transactionCost, transactionDate, currentUser, photoFile);

            }
        });
    }



    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // See code above
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, IMAGE_WIDTH);
                // Load the taken image into a preview
                ivReceiptImage.setImageBitmap(resizedBitmap);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void saveReceipt(String storeName, String storeType, String transactionCost, String transactionDate, ParseUser currentUser, File photoFile) {
        Receipt receipt = new Receipt();
        receipt.setStoreName(storeName);
        receipt.setStoreType(storeType);
        receipt.setTransactionCost(transactionCost);
        receipt.setTransactionDate(transactionDate);
        receipt.setImage(new ParseFile(photoFile));
        receipt.setUser(currentUser);
        receipt.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Please enter all required data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Receipt save was successful!");
                etStoreName.setText("");
                
                ivReceiptImage.setImageResource(0);
            }
        });
    }
}