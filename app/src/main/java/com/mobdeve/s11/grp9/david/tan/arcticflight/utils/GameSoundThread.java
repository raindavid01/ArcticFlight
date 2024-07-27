package com.mobdeve.s11.grp9.david.tan.arcticflight.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GameSoundThread extends Thread {
    private static final int MAX_SYNC_PLAYERS = 5; // Maximum number of simultaneous media players for a single sound
    private final Context context; // Context from the GameView
    private final Map<Integer, MediaPlayer[]> idSoundPair = new HashMap<>(); // Map to store MediaPlayer arrays for each sound

    // Constructor to initialize context and load all raw sound files
    public GameSoundThread(GameView gameView) {
        this.context = gameView.getContext();

        // Load Raw Sound Files
        loadAllRawSound();
    }

    // Method to play a sound by its ID
    public synchronized void playMusic(int musicID) {
        if (idSoundPair.containsKey(musicID) && idSoundPair.get(musicID) != null) {
            MediaPlayer[] mpArray = idSoundPair.get(musicID);

            if (mpArray != null) {
                for (MediaPlayer mediaPlayer : mpArray) {
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        break;
                    }
                }
            }
        }
    }

    // Method to stop a sound by its ID
    public synchronized void stopMusic(int musicID) {
        if (idSoundPair.containsKey(musicID) && idSoundPair.get(musicID) != null) {
            MediaPlayer[] mpArray = idSoundPair.get(musicID);

            if (mpArray != null) {
                for (MediaPlayer mediaPlayer : mpArray) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
            }
        }
    }

    // Method to stop all currently playing sounds
    public synchronized void stopAllMusic() {
        for (MediaPlayer[] mpArray : idSoundPair.values()) {
            if (mpArray != null) {
                for (MediaPlayer mediaPlayer : mpArray) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
            }
        }
    }

    // Method to load all raw sound files into MediaPlayer arrays
    private void loadAllRawSound() {
        idSoundPair.clear();

        // Get Resource IDs for all raw sound files
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            if (field.getName().endsWith("_sound")) {
                int rawResourceId;

                try {
                    rawResourceId = field.getInt(null); // Get the resource ID for the sound file
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }

                MediaPlayer[] mpArray = new MediaPlayer[MAX_SYNC_PLAYERS];

                // Create multiple MediaPlayer instances for the same sound file
                for (int i = 0; i < MAX_SYNC_PLAYERS; i++) {
                    mpArray[i] = MediaPlayer.create(context, rawResourceId);
                }

                // Store the MediaPlayer array in the map with the sound resource ID as the key
                idSoundPair.put(rawResourceId, mpArray);
            }
        }
    }
}
