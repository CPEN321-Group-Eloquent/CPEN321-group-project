package com.example.eloquent;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriverServiceHelper {
    private final Executor mExecutor= Executors.newSingleThreadExecutor();
    private Drive mDriveService;

    public DriverServiceHelper(Drive mDriveService) {
        this.mDriveService = mDriveService;
    }

    // create a file based on the url the user enters

    public Task<String> createFile(String filePath) {
        return Tasks.call(mExecutor, () -> {

            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName("Presentation");

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("application/txt", file);

            com.google.api.services.drive.model.File myFile = null;

            try {
                myFile = mDriveService.files().create(fileMetaData, mediaContent).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (myFile == null) {
                throw new IOException("Null results");
            }

            return myFile.getId();
        });
    }

}
