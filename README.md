# Photo Gallery Management App
## [Table of contents]()
### I. Overview
### II. Introduction to main features 
___
## [I. Overview]()
### Execution Environment:
- Supported Operating System Versions: Android 10, 11, 12, 13, 14.
- The application requires a minimum of Android 10 (API 29) and is designed to work well on Android 14 (API 33).

### SDK Versions:
- Compile SDK: Utilizes Compile SDK version 34.
- Target SDK: The application targets SDK version 33.
- Min SDK: The application requires a minimum SDK version of 29.

### Compilation Configuration:
- Java Compatibility: Java compatibility version is set to 1.8, enabling the use of features and improvements from Java 8.

### Dependencies:
1. Room Database (v2.6.0): Used for managing the internal database of the application.
2. Glide (v4.8.0): Library used for efficient loading and displaying of images.
3. Retrofit (v2.4.0): Used for making and receiving data from remote sources, facilitating interaction with remote APIs.
4. Firebase (BOM v32.4.0): Includes modules such as Authentication, Realtime Database, and Cloud Storage from Firebase, providing cloud services.
5. Facebook SDK (v15.2.0): Integrated to support Facebook-related functionalities, such as login.
6. ZXing Android Embedded (v4.3.0): Utilizes the library to generate QR codes, supporting barcode interactions.
7. ExoPlayer (v1.2.0): Integrated to support video playback within the application.
8. Firebase ML Vision (v24.0.3): Used for text recognition from images, expanding image processing capabilities.
___
## [II. Introduction to main features]()

| No. | Feature Name                                    | Description                                                            |
| --- | ----------------------------------------------- | ---------------------------------------------------------------------- |
| 1   | Sign Up/Login                                   | Utilize fingerprint, register accounts based on existing Google, Facebook accounts. |
| 2   | Create Story                                    | System randomly selects images, gifs, videos within a specific time range, automatically organizes and merges with music into a short TikTok-style video. |
| 3   | Album Management                                | Organize photos into favorite albums, albums from Facebook, Zalo. Users can create, edit, and delete custom albums. |
| 4   | Photo Sorting                                    | Sort photos based on name, capture date, tags, etc. |
| 5   | Add Photos                                        | Allow users to add photos from devices or capture using the camera. |
| 6   | Photo Editing and Filters               | Collection of photo editing tools, drawing, cropping, rotating images, applying color filters, adjusting contrast. |
| 7   | View and Display Photos                 | View photos with options for zooming, shrinking, rotating 90 degrees, and 360 degrees. |
| 8   | Upload Photos via QR Code      | Enable users to upload photos to the internet and generate QR codes. |
| 9   | Search for Photos                               | Search for photos by name, image extension type, image size. |
| 10  | Share Photos over the Internet   | Share and send photos via platforms such as Facebook, Zalo, Gmail. |
| 11  | Delete Photos                                    | Temporarily delete photos, move them to the trash, allowing restoration or permanent deletion. |
| 12  | Security and Access Rights            | Album security through password or fingerprint lock. |
| 13  | Synchronization and Backup        | Synchronize photos in the app to Cloud Storage. |
| 14  | Photo Notes                                       | Each photo can be marked as a favorite, and notes can be added to photos. |
| 15  | Convert to Text                                | Text within images will be processed and converted to text for user copying. |
| 16  | Set Photo as Lock/Home Screen | Users can select photos as their device's lock screen or home screen wallpaper. |
| 17  | Statistics                                           | Statistics feature displaying total number of photos in albums and total size of photos and videos. |
| 18  | Photo Reminders                              | Monthly reminders of memorable moments with a series of related photos. |
| 19  | Filter Duplicate Photos                 | During photo capture, suggest similar photos to users for interaction. |
| 20  | Photo Viewer                                     | Allow users to view photos in grid, list with additional information, or full-screen mode. |

___
