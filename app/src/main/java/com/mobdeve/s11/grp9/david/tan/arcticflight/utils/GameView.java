package com.mobdeve.s11.grp9.david.tan.arcticflight.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Main GameView for game running
 */
public abstract class GameView extends View
{
    private final ArrayList<GameObject>[] AllObjects = new ArrayList[10];
    private final GameLogicThread logicThread;
    private final GameRenderThread renderThread;
    private final GameSoundThread soundThread;
    private Handler handler;
    private Method drawMethodGO;
    private Method physicsMethodGO;
    private long lastFrameTime;
    private long frameInterval;

    /**
     * Whether the game view is started or not
     */
    public boolean IsStart;

    public GameView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        // ObjectList Initialization
        for (int i = 0; i < 10; i++)
        {
            AllObjects[i] = new ArrayList<GameObject>();
        }

        // Thread Initialization
        renderThread = new GameRenderThread(this);
        logicThread = new GameLogicThread(this);
        soundThread = new GameSoundThread(this);

        handler = new Handler(Looper.getMainLooper());
        lastFrameTime = System.currentTimeMillis();
        frameInterval = 1000 / GameConstants.MAX_FRAME_RATE;
        IsStart = false;

        // Methods Initialization
        try
        {
            drawMethodGO = GameObject.class.getDeclaredMethod("draw", Canvas.class);
            physicsMethodGO = GameObject.class.getDeclaredMethod("physics");

            drawMethodGO.setAccessible(true);
            physicsMethodGO.setAccessible(true);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Draw the sprite of all the game object of this view onto the given canvas.
     *
     * @param canvas The canvas to be drawn
     */
    @Override
    public void draw(@NonNull Canvas canvas)
    {
        super.draw(canvas);

        // Game objects
        for (int i = 9; i > -1; i--)
        {
            for (GameObject gameObject : AllObjects[i])
            {
                try
                {
                    drawMethodGO.invoke(gameObject, canvas);
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        if (elapsedTime < frameInterval)
        {
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    invalidate();
                }
            }, frameInterval - elapsedTime);
            return;
        }

        GameConstants.DELTA_TIME = elapsedTime;

        invalidate();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        onInitialization();
        onRegistration();
    }

    /**
     * Update the physics like velocity and etc. of all the game objects on this view.
     */
    public void physics()
    {
        // Game objects
        for (int i = 9; i > -1; i--)
        {
            for (GameObject gameObject : AllObjects[i])
            {
                try
                {
                    physicsMethodGO.invoke(gameObject);
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Here should initialize all the game objects and events inside this game view.
     */
    protected abstract void onInitialization();

    /**
     * Here should register all the game objects and events inside this game view.
     */
    protected abstract void onRegistration();

    /**
     * The render action should be within this method. This is once called in every frame.
     */
    public abstract void renderUpdate();

    /**
     * The logical action should be within this method. This is called every FIXED_DELTA_TIME.
     */
    public abstract void logicUpdate();

    /**
     * Play the sound with given soundID in resource file.
     *
     * @param soundID The sound ID.
     */
    public void playSound(int soundID)
    {
        soundThread.playMusic(soundID);
    }

    /**
     * Stop the sound with given soundID in resource file.
     *
     * @param soundID The sound ID.
     */
    public void stopSound(int soundID)
    {
        soundThread.stopMusic(soundID);
    }

    /**
     * Stop all the sounds currently playing.
     */
    public void stopSounds()
    {
        soundThread.stopAllMusic();
    }

    /**
     * Start running the game view.
     */
    public void start()
    {
        if (!IsStart)
        {
            renderThread.start();
            logicThread.start();
            IsStart = true;
        }
    }

    /**
     * Freeze the game view.
     */
    protected void freeze()
    {
        if (IsStart)
        {
            renderThread.pause();
            logicThread.pause();
        }
    }

    /**
     * Unfreeze the game view.
     */
    protected void unfreeze()
    {
        if (IsStart)
        {
            renderThread.unpause();
            logicThread.unpause();
        }
    }

    /**
     * Register the game object to the View.
     *
     * @param object      The game object to be registered.
     * @param renderLayer Ranging from [0 - 9]. The smaller, the more in the front.
     */
    protected void registerObject(@NonNull GameObject object, int renderLayer)
    {
        if (renderLayer >= 10)
        {
            renderLayer = 10;
        }
        else if (renderLayer < 0)
        {
            renderLayer = 0;
        }

        for (int i = 9; i > -1; i--)
        {
            for (GameObject gameObject : AllObjects[i])
            {
                if (gameObject.equals(object))
                {
                    return;
                }
            }
        }

        AllObjects[renderLayer].add(object);
    }

    protected void onDestroy() {
        super.onDetachedFromWindow();

        // Stop threads to avoid memory leaks
        if (renderThread != null) {
            renderThread.stop();
        }
        if (logicThread != null) {
            logicThread.stop();
        }
        if (soundThread != null) {
            soundThread.stopAllMusic();
            soundThread.stop();
        }

        // Clear all game objects
        for (ArrayList<GameObject> layer : AllObjects) {
            layer.clear();
        }

        // Nullify handlers and methods to allow garbage collection
        handler = null;
        drawMethodGO = null;
        physicsMethodGO = null;
    }

}
