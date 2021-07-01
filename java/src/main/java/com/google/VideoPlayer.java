package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video currentlyPlaying = null;
  private Boolean paused = true; // true when not playing anything
  private Random randGen = new Random();
  private List<VideoPlaylist> allPlaylists = new ArrayList<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");

    List<String> allVideos = videoLibrary.getVideos().stream()
            .map(video -> video.getTitle() + " (" + video.getVideoId() + ") " +
                    video.getTags().toString().replace(",", ""))
            .sorted()
            .collect(Collectors.toList());
    allVideos.forEach(video -> System.out.printf("%s%n", video));
  }

  public void playVideo(String videoId) {
    List<Video> videos = videoLibrary.getVideos();
    Video videoRequested = null;

    for(Video v: videos) {
      if(v.getVideoId().equals(videoId)) {
        videoRequested = v;
      }
    }

    if(videoRequested != null) { // If requested video exists
      if(currentlyPlaying != null) {
        System.out.printf("Stopping video: %s%n", currentlyPlaying.getTitle());
      }
      currentlyPlaying = videoRequested;
      paused = false;
      System.out.printf("Playing video: %s%n", currentlyPlaying.getTitle());

    } else {
      System.out.println("Cannot play video: Video does not exist");
    }
  }

  public void stopVideo() {
    if(currentlyPlaying != null) {
      System.out.printf("Stopping video: %s%n", currentlyPlaying.getTitle());
      currentlyPlaying = null;
    } else {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    List<Video> videos = videoLibrary.getVideos();
    if(videos.size() != 0) {
      Video videoRequested = videos.get(randGen.nextInt(videos.size()));
      playVideo(videoRequested.getVideoId());
    } else {
      System.out.println("No videos available");
    }
  }

  public void pauseVideo() {
    if(!paused) {
      System.out.printf("Pausing video: %s%n", currentlyPlaying.getTitle());
      paused = true;
    } else if(currentlyPlaying == null) {
      System.out.println("Cannot pause video: No video is currently playing");
    } else {
      System.out.printf("Video already paused: %s%n", currentlyPlaying.getTitle());
    }
  }

  public void continueVideo() {
    if(currentlyPlaying == null) {
      System.out.println("Cannot continue video: No video is currently playing");
    } else if(paused) {
      paused = false;
      System.out.printf("Continuing video: %s%n", currentlyPlaying.getTitle());
    } else {
      System.out.println("Cannot continue video: Video is not paused");
    }
  }

  public void showPlaying() {
    if(currentlyPlaying == null) {
      System.out.println("No video is currently playing");
    } else if(!paused) {
      System.out.printf("Currently playing: %s (%s) %s%n",
              currentlyPlaying.getTitle(), currentlyPlaying.getVideoId(),
              currentlyPlaying.getTags().toString().replace(",", ""));
    } else {
      System.out.printf("Currently playing: %s (%s) %s - PAUSED%n",
              currentlyPlaying.getTitle(), currentlyPlaying.getVideoId(),
              currentlyPlaying.getTags().toString().replace(",", ""));
    }
  }

  public void createPlaylist(String playlistName) {
    if(allPlaylists.size() != 0 && allPlaylists.stream().map(p -> p.getName().toLowerCase()).collect(Collectors.toList())
            .contains(playlistName.toLowerCase())) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    } else {
      allPlaylists.add(new VideoPlaylist(playlistName));
      System.out.printf("Successfully created new playlist: %s%n", playlistName);
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    List<Video> videos = videoLibrary.getVideos();
    Video videoRequested = null;
    VideoPlaylist playlistRequested = null;
    boolean canAdd = true;

    for (Video v : videos) {
      if (v.getVideoId().equals(videoId)) {
        videoRequested = v;
      }
    }
    for (VideoPlaylist vp : allPlaylists) {
      if (vp.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistRequested = vp;
      }
    }

    if (playlistRequested == null) {
      System.out.printf("Cannot add video to %s: Playlist does not exist%n", playlistName);
      canAdd = false;
    } else if(videoRequested == null) {
      System.out.printf("Cannot add video to %s: Video does not exist%n", playlistName);
      canAdd = false;
    } else {
      for(Video v: playlistRequested.getVideos()) {
        if(v == videoRequested) {
          System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
          canAdd = false;
        }
      }
    }

    if(canAdd) {
      playlistRequested.addVideo(videoRequested);
      System.out.printf("Added video to %s: %s%n", playlistName, videoRequested.getTitle() );
    }
  }

  public void showAllPlaylists() {
    if(allPlaylists.size() == 0) {
      System.out.println("No playlists exist yet");
    } else {
      List<String> orderedPlaylists = allPlaylists.stream().map(p -> p.getName()).collect(Collectors.toList());
      orderedPlaylists.sort(String.CASE_INSENSITIVE_ORDER);
      System.out.println("Showing all playlists:");
      orderedPlaylists.forEach(System.out::println);
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist playlistRequested = null;
    for(VideoPlaylist p: allPlaylists) {
      if(p.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistRequested = p;
      }
    }
    if(playlistRequested == null) {
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n", playlistName);
    } else {
      System.out.printf("Showing playlist: %s%n", playlistName);
      if(playlistRequested.getVideos().size() == 0) {
        System.out.println("No videos here yet");
      } else {
        playlistRequested.getVideos().stream()
                .map(p -> p.getTitle() + " (" + p.getVideoId() + ") " + p.getTags().toString()
                        .replace(",", ""))
                .forEach(System.out::println);
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    List<Video> videos = videoLibrary.getVideos();
    VideoPlaylist playlistRequested = null;
    Video videoRequested = null;

    for(VideoPlaylist p: allPlaylists) {
      if(p.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistRequested = p;
      }
    }
    for(Video v: videos) {
      if(v.getVideoId().equals(videoId)) {
        videoRequested = v;
      }
    }

    if(playlistRequested == null) {
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n", playlistName);
    } else if(videoRequested == null) {
      System.out.printf("Cannot remove video from %s: Video does not exist%n", playlistName);
    } else if(playlistRequested.getVideos().stream().map(v -> v.getVideoId()).collect(Collectors.toList()).contains(videoId)){
      System.out.printf("Removed video from %s: %s%n", playlistName, videoRequested.getTitle());
      playlistRequested.removeVideo(videoRequested);
    } else {
      System.out.printf("Cannot remove video from %s: Video is not in playlist%n", playlistName);
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlistRequested = null;
    for(VideoPlaylist p: allPlaylists) {
      if(p.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistRequested = p;
      }
    }
    if(playlistRequested == null) {
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n", playlistName);
    } else {
      playlistRequested.clearVideos();
      System.out.printf("Successfully removed all videos from %s%n", playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlistRequested = null;
    for(VideoPlaylist p: allPlaylists) {
      if(p.getName().toLowerCase().equals(playlistName.toLowerCase())) {
        playlistRequested = p;
      }
    }
    if(playlistRequested == null) {
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n", playlistName);
    } else {
      allPlaylists.remove(playlistRequested);
      System.out.printf("Deleted playlist: %s%n", playlistName);
    }
  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getVideos();
    Map<String, Video> nameVideo = new HashMap<>();
    String keyFound = null;
    for(Video v: videos) {
      nameVideo.put(v.getTitle().toLowerCase(), v);
    }
    for(String name: nameVideo.keySet()) {
      if(name.contains(searchTerm)) {
        keyFound = name;
      }
    }
    if(keyFound == null) {
      System.out.printf("No search results for %s%n", searchTerm);
    }
  }

  public void searchVideosWithTag(String videoTag) {
    System.out.println("searchVideosWithTag needs implementation");
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}