package com.orange1988.photoselector.manager;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.orange1988.photoselector.entity.PhotoEntity;

import java.util.List;

/**
 * Created by Mr. Orange on 15/11/27.
 */
public class PhotoLoader extends AsyncTaskLoader<List<PhotoEntity>> {

    private PhotoDomain domain;
    private String folderName;
    private List<PhotoEntity> mPhotos;

    public PhotoLoader(Context context, PhotoDomain domain) {
        super(context);
        this.domain = domain;
    }

    public void setFolderName(String name) {
        this.folderName = name;
    }

    @Override
    public List<PhotoEntity> loadInBackground() {
        if (!TextUtils.isEmpty(folderName)) {
            return domain.getPhotos(folderName);
        } else {
            return domain.getLatestPhotos();
        }
    }

    @Override
    public void onCanceled(List<PhotoEntity> photos) {
        super.onCanceled(photos);
        onReleaseResource(photos);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mPhotos != null) {
            onReleaseResource(mPhotos);
            mPhotos = null;
        }
    }

    @Override
    protected void onStartLoading() {
        if (mPhotos != null) {
            deliverResult(mPhotos);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public void deliverResult(List<PhotoEntity> photos) {
        if (isReset()) {
            if (photos != null) {
                onReleaseResource(photos);
            }
        }

        List<PhotoEntity> oldPhotos = photos;
        mPhotos = photos;
        if (isStarted()) {
            super.deliverResult(photos);
        }
        if (oldPhotos != null) {
            onReleaseResource(oldPhotos);
        }
    }

    protected void onReleaseResource(List<PhotoEntity> photos) {

    }

}
