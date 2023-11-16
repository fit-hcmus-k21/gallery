package com.example.gallery.data.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 16/11/2023
 */
public class LoginRequest {
    private LoginRequest() {
        // This class is not publicly instantiable
    }

    public static class FacebookLoginRequest {

        @Expose
        @SerializedName("fb_access_token")
        private String fbAccessToken;

        @Expose
        @SerializedName("fb_user_id")
        private String fbUserId;

        public FacebookLoginRequest(String fbUserId, String fbAccessToken) {
            this.fbUserId = fbUserId;
            this.fbAccessToken = fbAccessToken;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            FacebookLoginRequest that = (FacebookLoginRequest) object;

            if (fbUserId != null ? !fbUserId.equals(that.fbUserId) : that.fbUserId != null) {
                return false;
            }
            return fbAccessToken != null ? fbAccessToken.equals(that.fbAccessToken)
                    : that.fbAccessToken == null;
        }

        @Override
        public int hashCode() {
            int result = fbUserId != null ? fbUserId.hashCode() : 0;
            result = 31 * result + (fbAccessToken != null ? fbAccessToken.hashCode() : 0);
            return result;
        }

        public String getFbAccessToken() {
            return fbAccessToken;
        }

        public String getFbUserId() {
            return fbUserId;
        }
    }

    public static class GoogleLoginRequest {

        @Expose
        @SerializedName("google_user_id")
        private String googleUserId;

        @Expose
        @SerializedName("google_id_token")
        private String idToken;

        public GoogleLoginRequest(String googleUserId, String idToken) {
            this.googleUserId = googleUserId;
            this.idToken = idToken;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            GoogleLoginRequest that = (GoogleLoginRequest) object;

            if (googleUserId != null ? !googleUserId.equals(that.googleUserId)
                    : that.googleUserId != null) {
                return false;
            }
            return idToken != null ? idToken.equals(that.idToken) : that.idToken == null;

        }

        @Override
        public int hashCode() {
            int result = googleUserId != null ? googleUserId.hashCode() : 0;
            result = 31 * result + (idToken != null ? idToken.hashCode() : 0);
            return result;
        }

        public String getGoogleUserId() {
            return googleUserId;
        }

        public String getIdToken() {
            return idToken;
        }
    }

    public static class ServerLoginRequest {

        @Expose
        @SerializedName("email")
        private String email;

        @Expose
        @SerializedName("password")
        private String password;

        public ServerLoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            ServerLoginRequest that = (ServerLoginRequest) object;

            if (email != null ? !email.equals(that.email) : that.email != null) {
                return false;
            }
            return password != null ? password.equals(that.password) : that.password == null;
        }

        @Override
        public int hashCode() {
            int result = email != null ? email.hashCode() : 0;
            result = 31 * result + (password != null ? password.hashCode() : 0);
            return result;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
