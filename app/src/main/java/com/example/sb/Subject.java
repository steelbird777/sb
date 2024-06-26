package com.example.sb;

public class Subject {
        private String subject_id;
        private String subject_name;
        private String user_mail;
        public Subject() {
                // Required empty public constructor for Firestore
        }
        public Subject(String subject_id, String subject_name, String user_mail) {
                this.subject_id= subject_id;
                this.subject_name = subject_name;
                this.user_mail = user_mail;
        }
        public String getSubject_id() {
                return subject_id;
        }
        public String getUser_mail() {
                return user_mail;
        }

        public void setUser_mail(String user_mail) {
                this.user_mail = user_mail;
        }

        public void setSubject_id(String subject_id) {
                this.subject_id = subject_id;
        }

        public String getSubject_name() {
                return subject_name;
        }

        public void setSubject_name(String subject_name) {
                this.subject_name = subject_name;
        }
}

