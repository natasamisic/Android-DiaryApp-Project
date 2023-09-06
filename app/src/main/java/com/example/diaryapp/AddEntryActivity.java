package com.example.diaryapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diaryapp.database.EntryRepository;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;

public class AddEntryActivity extends AppCompatActivity {

    EditText titleInput;
    EditText textInput;
    Button saveBtn;
    Button btnGallery;
    ImageView img;
    EntryRepository entryRepo;
    Bitmap selectedImage;
    Button mapbtn;
    String location;
    TextView locationview;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        entryRepo = new EntryRepository(getApplicationContext());
        titleInput = findViewById(R.id.titleinput);
        textInput = findViewById(R.id.textinput);
        saveBtn = findViewById(R.id.update_btn);
        img = findViewById(R.id.uploaded_img);
        btnGallery = findViewById(R.id.imagebtn);
        mapbtn = findViewById(R.id.mapbtn);
        locationview = findViewById(R.id.entered_location);

        latLng = new LatLng(0,0);
        location = "";

        btnGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityIntent.launch(intent);
        });

        mapbtn.setOnClickListener(v -> {
            Intent intent = new Intent(AddEntryActivity.this, MapActivity.class);
            intent.putExtra("activity", "AddEntry");
            intent.putExtra("title", titleInput.getText().toString());
            intent.putExtra("text", textInput.getText().toString());
            startActivity(intent);
            finish();
        });

        saveBtn.setOnClickListener(v -> {
            if(titleInput.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Entry title can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }else if(textInput.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Entry text can't be empty!", Toast.LENGTH_SHORT).show();
                finish();
            }else if(selectedImage == null){
                Toast.makeText(this, "You must insert an image!", Toast.LENGTH_SHORT).show();
                finish();
            }
            final Entry entry = new Entry();
            entry.setTitle(titleInput.getText().toString());
            entry.setText(textInput.getText().toString());
            entry.setCreatedTime(System.currentTimeMillis());
            entry.setImage(selectedImage);
            entry.setLocation(latLng.toString());

            entryRepo.insertEntry(entry);
            new Handler().postDelayed(() -> {
                Toast.makeText(this, "Entry saved!", Toast.LENGTH_SHORT).show();
                finish();
            }, 2000);

        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            try {
                                final Uri imageUri = result.getData().getData();
                                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                selectedImage = BitmapFactory.decodeStream(imageStream);
                                img.setImageURI(imageUri);
                                img.setImageBitmap(selectedImage);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                }
            });

    @Override
    protected void onResume() {
        super.onResume();
        getContent();
    }

    private void getContent(){
        Intent data = getIntent();
        if(data.getStringExtra("activity") != null){
            location = data.getStringExtra("location");
            latLng = data.getExtras().getParcelable("latlng");
            locationview.setText(location);
            titleInput.setText(data.getStringExtra("title"));
            textInput.setText(data.getStringExtra("text"));
        }
    }
}