package com.example.eloquent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Collections;

public class EditPres extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText presTitle, presDescription;
    private Calendar calendar;
    private String todaysDate;
    private String currentTime;
    private Button preparationBtn;
    private Button presentingBtn;
    private Button liveCollabBtn;
    private DriverServiceHelper driverServiceHelper;

    Presentation presentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pres);

        presentation = (Presentation) getIntent().getSerializableExtra("Presentation");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(presentation.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presTitle = findViewById(R.id.presTitle);
        presDescription = findViewById(R.id.presDescription);
        preparationBtn = findViewById(R.id.preparationButton);
        presentingBtn = findViewById(R.id.presentingButton);
        liveCollabBtn = findViewById(R.id.liveCollabButton);

        preparationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preparationIntent = new Intent(EditPres.this, Preparation.class);
                preparationIntent.putExtra("Presentation", presentation);
                startActivity(preparationIntent);
            }
        });

        presentingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

            }
        });

        liveCollabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(EditPres.this, LiveCollaboration.class);
                startActivity(shareIntent);
            }
        });

        presTitle.setText(presentation.getTitle());

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

        //get current date and time
        calendar = Calendar.getInstance();
        todaysDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
    }

//    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        Uri uri = data.getData();
//                    }
//                }
//            }
//
//    );

    private void requestSignIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        startActivityForResult(client.getSignInIntent(),400);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 400:
                if(resultCode == RESULT_OK) {
                    handleSignInIntent(data);
                }
                break;
        }
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        GoogleAccountCredential credential = GoogleAccountCredential.
                                usingOAuth2(EditPres.this, Collections.singleton(DriveScopes.DRIVE_FILE));

                        Drive googleDriveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("Drive")
                                .build();

                        driverServiceHelper = new DriverServiceHelper(googleDriveService);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // if the hour or minute is less than 10, add 0 before it
    private String pad(int i) {
        if(i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete) {

        }
        if(item.getItemId() == R.id.save) {
            if(presentation.title != null) {
                presentation.title = presTitle.getText().toString();
                Intent changingIntent = new Intent(EditPres.this, MainActivity.class);
                changingIntent.putExtra("Presentation", presentation);
                startActivity(changingIntent);
            }
            else {
                Toast.makeText(getApplicationContext(),"Title cannot be null", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Intent presentingIntent = new Intent(EditPres.this, Presenting.class);
            presentingIntent.putExtra("Presentation", presentation);
            startActivity(presentingIntent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "We need these location permissions to run!", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Need Recording Permissions")
                        .setMessage("We need your audio recording permissions to mark automatically switch cue cards")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditPres.this, "We need these location permissions to run!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(EditPres.this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
            }
        }
    }
}