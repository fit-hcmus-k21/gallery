package com.example.gallery.data.models.api;

public class User {
        private String fullName;
        private String email;
        private String id;


        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String fullName, String email, String displayName) {
            this.fullName = fullName;
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }




}
