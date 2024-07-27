package com.mobdeve.s11.grp9.david.tan.arcticflight.utils;

public class GameLogicThread extends Thread {
    private GameView gameView;
    private boolean paused;

    // Constructor to initialize the game view and set the initial paused state
    public GameLogicThread(GameView gameView) {
        this.gameView = gameView;
        this.paused = false;
    }

    // Method to pause the game logic thread
    public void pause() {
        paused = true;
    }

    // Method to unpause the game logic thread
    public synchronized void unpause() {
        paused = false;
        notify(); // Notify the thread to resume operation
    }

    @Override
    public void run() {
        long startTime;
        long elapsedTime;

        while (true) {
            synchronized (this) {
                // If the thread is paused, wait until it is unpaused
                while (paused) {
                    try {
                        wait(); // Wait for the thread to be unpaused
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Record the start time for this iteration
            startTime = System.currentTimeMillis();

            ///////////////////////////////////////////////////////
            ///////////////////// Logic Here //////////////////////

            // Update game logic and physics
            gameView.logicUpdate();
            gameView.physics();

            ///////////////////////////////////////////////////////

            // Calculate the elapsed time for this iteration
            elapsedTime = System.currentTimeMillis() - startTime;

            // Calculate the sleep time needed to maintain a consistent frame rate
            long sleepTime = GameConstants.FIXED_DELTA_TIME - elapsedTime;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime); // Sleep for the calculated time to maintain frame rate
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
