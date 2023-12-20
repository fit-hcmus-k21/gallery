package com.example.gallery.data.models.db;


import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Optional;


/**
 * Created on 27/10/2023
 */

@Entity(tableName = "media_items",
        foreignKeys = {
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "id",
                    childColumns = "userID",
                    onDelete = ForeignKey.CASCADE
            )
//                ,
//            @ForeignKey( // error will be incurred if s.o comment this
//                        entity = Album.class,
//                        parentColumns = "name",
//                        childColumns = "albumName",
//                        onDelete = ForeignKey.NO_ACTION // If delete album, mediaItem still exist
//                )
        }
//        primaryKeys = {
//            "id", "userID"
//        }
)
public class MediaItem implements Serializable {
    // This is used to represent for MediaItem in entites with the atribute typeDisplay which uses for recognize the type to display in recyclerview
    public static final int TYPE_GRID = 1;
    public static final int TYPE_LIST = 2;
    public static final int TYPE_STAGGERED = 3;


//    @ColumnInfo(name = "id", index = true)

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo( name = "userID", index = true)
    @NonNull
    private String userID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "tag")
    private String tag;

    @ColumnInfo(name = "description")
    private String description;

    // path in local
    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "width")
    private int width;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "fileSize")
    private long fileSize;

    @ColumnInfo(name = "fileExtension")
    private String fileExtension;

    @ColumnInfo(name = "creationDate") // covert from String to Long, that specify the number of milliseconds since January 1, 1970, 00:00:00 GMT
    private Long creationDate;

    @ColumnInfo(name = "location")
    private String location;


//    @ColumnInfo(name = "albumName", index = true)
    @ColumnInfo(name = "albumName")
    private String albumName;

    // url when sync to cloud storage
    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "favorite")
    private boolean favorite; // 0 is not favorite, 1 is favorite

    @ColumnInfo(name = "parentPath")
    private String parentPath;

    @ColumnInfo(name = "lastModified") // covert from String to Long, that specify the number of milliseconds since January 1, 1970, 00:00:00 GMT
    private Long lastModified;

    @ColumnInfo(name = "deletedTs") // Deleted timestamp
    private long deletedTs;

    // if download through a link, origin is the link
    @ColumnInfo(name = "origin")
    private String origin;
    @ColumnInfo(name = "previousAlbum")
    private String previousAlbum;

    // Add ignore attribute to display some work

    @Ignore
    private int typeDisplay;

    public int getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(int typeDisplay) {
        this.typeDisplay = typeDisplay;
    }


    // Constructor
    public MediaItem() {
//        if (this.previousAlbum == null)
//            this.previousAlbum = this.albumName;

    }


    public MediaItem(int id, String userID, String name, String tag, String description, String path,
                     int width, int height, long fileSize, String fileExtension, Long creationDate, String location,
                     String albumName, String url, boolean favorite, String parentPath, Long lastModified, long deletedTs) {
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.path = path;
        this.width = width;
        this.height = height;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
        this.creationDate = creationDate;
        this.location = location;
        this.albumName = albumName;
        this.url = url;
        this.favorite = favorite;
        this.parentPath = parentPath;
        this.lastModified = lastModified;
        this.deletedTs = deletedTs;
//        if (this.previousAlbum == null)
//            this.previousAlbum = "";
    }

    public String getPreviousAlbum(){return  this.previousAlbum;}
    public void setPreviousAlbum(String name){this.previousAlbum = name;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {

        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag == null ? "" : tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {

        return description == null ?  "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        if (width == 0) {
            setInfo();
        }
        return width;
    }

    public void setWidth(int width) {

        this.width = width;
    }

    public int getHeight() {
        if (height == 0) {
            setInfo();
        }
        return height;
    }

    public void setHeight(int height) {


        this.height = height;

    }

    public long getFileSize() {

        if (fileSize == 0){
            setInfo();
        }
        return fileSize;
    }

    public void setFileSize(long fileSize) {

        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        if (fileExtension == null){
            setInfo();
        }

        return fileExtension;
    }



    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocation() {
        if (location == null || location.equals("")){
            setLocation();
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocation() {
        if (getPath() == null || getPath().equals("")){
            //  System.out.println("MediaItem : setLocation | Please input image path");
            return;
        }

        InputStream inputStream = null;
        String location = "";

            try {
                inputStream = getImageInputStream(getPath());

                // Bạn có thể sử dụng inputStream ở đây để đọc dữ liệu từ ảnh
                // Ví dụ: Đọc byte từ inputStream

                // Đóng inputStream khi đã sử dụng xong
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

            // See whether it has GPS data
            Iterable<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                // Try to read out the location, making sure it's non-zero
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {
                    location = geoLocation.toString();
                    //  System.out.println("Location: " + location);
                }
            }
        } catch (ImageProcessingException | IOException err ) {
            // Handle exception case
        }

        this.location = location;
    }

    private static InputStream getImageInputStream(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        return new FileInputStream(imageFile);
    }



    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public long getDeletedTs() {
        return deletedTs;
    }

    public void setDeletedTs(long deletedTs) {
        this.deletedTs = deletedTs;
    }

    public void setOrigin(String url) {
        this.origin = url;

    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        MediaItem mediaItem = (MediaItem) obj;
        return mediaItem.getPath() == this.getPath();
    }


//    ------------------
//    set info media item
    public void setInfo() {
        // set width, height, filesize, fileExtension, location

        // Đường dẫn đến tập tin hình ảnh
        String imagePath = getPath();
        if (imagePath == null) {
            //  System.out.println("MediaItem : setInfo | Please input image path");
            return;
        }

        // Tạo đối tượng File từ đường dẫn
        File imageFile = new File(imagePath);

        fileExtension = getFileExtension(imageFile);
        setFileExtension(fileExtension);

        // Lấy thông tin file size
        long fileSize = imageFile.length(); // Kích thước file trong bytes
        setFileSize(fileSize);

        // Hiển thị thông tin
        //  System.out.println("File Extension: " + fileExtension);
        //  System.out.println("File Size (Bytes): " + fileSize);

        // Đọc ảnh để lấy thông tin chi tiết hơn
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        setHeight(imageHeight);
        setWidth(imageWidth);

        //  System.out.println("Image Width: " + imageWidth);
        //  System.out.println("Image Height: " + imageHeight);

//        // Lấy thông tin vị trí
        setLocation();


    }

    // Hàm để lấy file extension từ một đối tượng File
    public String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return ""; // Trả về chuỗi trống nếu không tìm thấy đuôi file
    }
}

