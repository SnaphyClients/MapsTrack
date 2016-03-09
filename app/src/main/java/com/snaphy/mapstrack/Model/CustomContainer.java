package com.snaphy.mapstrack.Model;

import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

/**
 * Created by Ravi-Gupta on 2/16/2016.
 */
public class CustomContainer extends Container {
    /**
     * Upload a new file
     * @param file Content of the file.
     * @param callback The callback to be executed when finished.
     */

    public void UploadAmazon(java.io.File file, ObjectCallback<com.snaphy.mapstrack.Model.ImageModel> callback) {

        getFileRepository().uploadAmazon(file, callback);
    }

    public CustomFileRepository getFileRepository() {
        RestAdapter adapter = ((RestAdapter)getRepository().getAdapter());
        CustomFileRepository repo = adapter.createRepository(CustomFileRepository.class);
        repo.setContainer(this);
        return repo;
    }
}
