package com.example.gallery.data.repositories.models;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gallery.data.local.dao.UserDao;
import com.example.gallery.data.local.entities.Album;
import com.example.gallery.data.local.entities.User;
import com.example.gallery.data.local.dao.AlbumDao;
import com.example.gallery.data.local.db.GalleryDatabase;

import java.util.List;

public class AlbumRepository {
    private AlbumDao albumDao;
    private MutableLiveData<List<Album>> allAlbums;
    Application application;
    public AlbumRepository(Application application){
        this.application = application;
        GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(application);
        albumDao = galleryDatabase.albumDao();
        allAlbums = new MutableLiveData<>();
    }

    public LiveData<List<Album>> getAlbums(){
        return allAlbums;
    }

    public void fetchDataFromExternalStorageAndSaveToDataBase(){
        new fetchDataFromExternalStorageAndSaveToDataBaseAsyncTask(albumDao, allAlbums).execute();
    }
    public class fetchDataFromExternalStorageAndSaveToDataBaseAsyncTask extends android.os.AsyncTask<Void, Void, List<Album>> {
        private AlbumDao albumDao;
        private MutableLiveData<List<com.example.gallery.data.local.entities.Album>> albums;

        public fetchDataFromExternalStorageAndSaveToDataBaseAsyncTask(AlbumDao albumDao, MutableLiveData<List<Album>> albums) {
            this.albumDao = albumDao;
            this.albums = albums;
        }

        @Override
        protected List<com.example.gallery.data.local.entities.Album> doInBackground(Void... voids) {
            List<Album> albums = AlbumFromExternalStorage.listAlbums(application);
            albumDao.insertAll(albums);

            return albumDao.loadAllAlbums();
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            super.onPostExecute(albums);
            if(albums != null){
                Log.d("MyTag", "onPostExecute Album Repository: " + albums.size());
                new contentResolverAsyncTask().execute();
                this.albums.setValue(albums);
            }
        }
    }
    public class contentResolverAsyncTask extends android.os.AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            String authority = "com.example.gallery.data.local.db.GalleryDatabase";
            String databaseName = "GalleryDatabase";
            String tableName = "albums";

            Uri uriDatabase = Uri.parse("content://" + authority + "/" + databaseName);
            Uri uriTable = Uri.withAppendedPath(uriDatabase, tableName);

            Cursor cursor = application.getContentResolver().query(uriTable, null, null, null);
            if(cursor != null){
                cursor.moveToFirst();
                do{
                    Log.d("MyTag123", "contentResolverAsyncTask: " + cursor.getString(cursor.getColumnIndexOrThrow("name")));
                }while(cursor.moveToNext());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }


}
