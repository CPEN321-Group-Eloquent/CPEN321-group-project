package com.example.eloquent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class AddPres extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText presTitle, presDescription;
    private Button importButton;
    private Calendar calendar;
    private String todaysDate;
    private String currentTime;
    private String importText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pres);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Presentation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presTitle = findViewById(R.id.presTitle);
        presDescription = findViewById(R.id.presDescription);

        // it detects when the presentation title is edited, it will change the title on the app

        presTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        importButton = findViewById(R.id.importButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.getInstance(AddPres.this).importPresentation(
                        User.getInstance().getData().getUserID(),
                        getString(R.string.sample_import_input)
                );
            }
        });

        //get current date and time
        calendar = Calendar.getInstance();
        todaysDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
    }

    // this will start a import activity and is invoked by pressing the import button
    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        byte[] bytes = getBytesFromUrl(getApplicationContext(), uri);
                        importText = new String((bytes));
                    }
                }
            }

    );

    // if the hour or minute is less than 10, add 0 before it
    private String pad(int i) {
        if(i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    // return the menu with the add and save button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    // when the save or delete button is clicked, it will create or delete the presentation

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Delete btt is clicked", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.save) {
            if(presTitle.getText().toString()!= null) {
                Presentation presentation = new Presentation(presTitle.getText().toString());

                // Create new presentation in backend
                Router router = Router.getInstance(this);
                router.createEmptyPresentation(User.getInstance().getData().getUserID(), presTitle.getText().toString(), presentation);

                // Send new presentation to main activity
                Intent savingIntent = new Intent(AddPres.this, MainActivity.class);
                savingIntent.putExtra("Presentation", presentation);
                startActivity(savingIntent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Please enter a title", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // open the choose file window and let user choose the data
    public void openFileDialog(View view) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        data.setType("*/*");
        String[] mimeTypes = {"text/*"};
        data.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        data = Intent.createChooser(data, "choose a file");
        sActivityResultLauncher.launch(data);
    }

    byte[] getBytesFromUrl (Context context, Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}