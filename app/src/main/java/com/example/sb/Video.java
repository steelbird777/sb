package com.example.sb;

import androidx.annotation.NonNull;

public class Video {
    private String video_number;
    private String video_name;
    private String video_content;
    private String video;

    public Video() {
        // Required empty public constructor for Firestore
    }

    public Video(String video_number, String video_name, String video_content, String video_link) {
        this.video_number = video_number;
        this.video_name = video_name;
        this.video_content = video_content;
        this.video = video;

    }

    public String getVideo_number() {
        return video_number;
    }

    public void setVideo_number(String video_number) {
        this.video_number = video_number;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_content() {
        return video_content;
    }

    public void setVideo_content(String video_content) {
        this.video_content = video_content;
    }

    public String getVideo() {
        return video;
    }
    public void setVideoURI(String video) {
        this.video = video;
    }



    @NonNull
    @Override
    public String toString() {
        return video_name;
    }
}
