package com.mobdeve.s11.grp9.david.tan.arcticflight.utils;

public class GameLogicThread extends Thread
{
    private GameView gameView;
    private boolean paused;

    public GameLogicThread(GameView gameView)
    {
        this.gameView = gameView;
        this.paused = false;
    }

    public void pause()
    {
        paused = true;
    }

    public synchronized void unpause()
    {
        paused = false;
        notify();
    }

    @Override
    public void run()
    {
        long startTime;
        long elapsedTime;

        while (true)
        {
            synchronized (this)
            {
                while (paused)
                {
                    try
                    {
                        wait();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            startTime = System.currentTimeMillis();
            ///////////////////////////////////////////////////////
            ///////////////////// Logic Here //////////////////////

            gameView.logicUpdate();
            gameView.physics();

            ///////////////////////////////////////////////////////
            elapsedTime = System.currentTimeMillis() - startTime;

            long sleepTime = GameConstants.FIXED_DELTA_TIME - elapsedTime;
            if (sleepTime > 0)
            {
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
