package com.example.gallery.data.repositories.models;

/**
 * Created on 28/10/2023
 */
public class PhotoEditor {
    // attributes
    private MediaItem bufferedPhoto;
    private int brightness;
    private int contrast;
    private boolean isFilterApplied;
    private boolean isRotated;
    private int rotationAngle;
    private double zoomLevel;

    // constructors


    // getters and setters


    public MediaItem getBufferedPhoto() {
        return bufferedPhoto;
    }

    public void setBufferedPhoto(MediaItem bufferedPhoto) {
        this.bufferedPhoto = bufferedPhoto;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public boolean isFilterApplied() {
        return isFilterApplied;
    }

    public void setFilterApplied(boolean filterApplied) {
        isFilterApplied = filterApplied;
    }

    public boolean isRotated() {
        return isRotated;
    }

    public void setRotated(boolean rotated) {
        isRotated = rotated;
    }

    public int getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(int rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    // methods
    public void applyBrightness(int brightness) {


        // handle here


    }

    public void applyContrast(int contrast) {


        // handle here
    }

    public void applyFilter() {


        // handle here

    }

    public void rotate(int angle) {


        // handle here


    }

    public void crop(int l, int r, int t, int b) {


        // handle here


    }

    public void zoom(double size) {

        // handle here


    }

    public void saveEditedVersion() {


        // handle here
    }


    public void addText(int x, int y, String text) {


        // handle here

    }


}

