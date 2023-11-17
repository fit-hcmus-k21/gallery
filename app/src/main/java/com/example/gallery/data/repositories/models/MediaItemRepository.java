package com.example.gallery.data.repositories.models;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.local.dao.MediaItemDao;
import com.example.gallery.data.local.db.GalleryDatabase;
import com.example.gallery.data.local.entities.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class MediaItemRepository {
    private MediaItemDao mediaItemDao;
    MutableLiveData<List<MediaItem>> allMediaItem;
    Application application;

    public MediaItemRepository(Application application) {
        this.application = application;
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        mediaItemDao = galleryDatabase.mediaItemDao();
        allMediaItem = new MutableLiveData<>();
    }
    public LiveData<List<MediaItem>> getImages() {
        return allMediaItem;
    }

    public void fetchDataFromExternalStorageAndSaveToDataBase(){
        new fetchfetchDataFromExternalStorageAndSaveToDataBaseAsyncTask(mediaItemDao, allMediaItem).execute();
    }
    public class fetchfetchDataFromExternalStorageAndSaveToDataBaseAsyncTask extends AsyncTask<Void, Void, List<MediaItem>> {
        private MediaItemDao mediaItemDao;
        private MutableLiveData<List<MediaItem>> images;

        public fetchfetchDataFromExternalStorageAndSaveToDataBaseAsyncTask(MediaItemDao mediaItemDao, MutableLiveData<List<MediaItem>> images) {
            this.mediaItemDao = mediaItemDao;
            this.images = images;
        }

        @Override
        protected List<MediaItem> doInBackground(Void... voids) {
            ArrayList<MediaItem> mediaItems = MediaItemFromExternalStorage.listMediaItems(application);
            mediaItemDao.insertAll(mediaItems);

            return mediaItemDao.getAllMediaItems();
        }


        @Override
        protected void onPostExecute(List<MediaItem> mediaItems) {
            super.onPostExecute(mediaItems);
            if(mediaItems != null){
                Log.e("MyTag", "onPostExecute: " + mediaItems.size());
                images.setValue(mediaItems);
            }
        }
    }

}
