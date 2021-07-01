package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private final String name;
    private List<Video> videos;


    VideoPlaylist(String name) {
        this.name = name;
        videos = new ArrayList<>();
    }

    public String getName() { return name; }

    public List<Video> getVideos() { return videos; }

    public void addVideo(Video vid) { videos.add(vid); }

    public void removeVideo(Video vid) { videos.remove(vid); }

    public void clearVideos() { videos.clear(); };
}
