package com.mobdeve.s11.grp9.david.tan.arcticflight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.DatabaseHelper;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameConstants;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameView;
import com.mobdeve.s11.grp9.david.tan.arcticflight.objects.BackGround;
import com.mobdeve.s11.grp9.david.tan.arcticflight.objects.BaseGround;
import com.mobdeve.s11.grp9.david.tan.arcticflight.objects.Bird;
import com.mobdeve.s11.grp9.david.tan.arcticflight.objects.Coin;
import com.mobdeve.s11.grp9.david.tan.arcticflight.objects.Pipe;
import com.mobdeve.s11.grp9.david.tan.arcticflight.objects.Timer;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class GameScene extends GameView
{
    // States
    public int score;
    private Random random;
    private boolean isGameOver;
    private boolean canControl;
    private boolean isDebug;
    private long startTime;
    private long lastFrameShowTime;
    private int coinCount;
    private int timeCount;

    // Scene Game Objects
    private Bird bird;
    private Coin coin;
    private Timer timer;
    private Timer frameRateShower;
    private final ArrayList<BackGround> backGrounds = new ArrayList<BackGround>();
    private final ArrayList<BaseGround> baseGrounds = new ArrayList<BaseGround>();
    private final ArrayList<Pipe> pipes = new ArrayList<Pipe>();
    private static final float fixed_speed = 0.7f;


    public GameScene(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        random = new Random();
        isGameOver = false;
        canControl = false;
        isDebug = false;
        coinCount = 0;
        timeCount = 0;
    }

    @Override
    protected void onInitialization()
    {
        ///////////////////////// Objects /////////////////////////
        // Bird
        bird = new Bird(Vector2.Zero, Vector2.multiply(Vector2.One, 0.3f));
        bird.setPosition(new Vector2(-bird.getRawRect().width(), GameConstants.SCREEN_HEIGHT * 0.3f));

        // BackGround
        BackGround backGround1 = new BackGround(Vector2.Zero, Vector2.One);
        backGround1.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / backGround1.getRect().width(), (float) GameConstants.SCREEN_HEIGHT / backGround1.getRect().height()));

        BackGround backGround2 = new BackGround(Vector2.Zero, Vector2.One);
        backGround2.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / backGround2.getRect().width(), (float) GameConstants.SCREEN_HEIGHT / backGround2.getRect().height()));
        backGround2.setPosition(new Vector2(GameConstants.SCREEN_WIDTH, 0));

        backGrounds.add(backGround1);
        backGrounds.add(backGround2);

        // BaseGround
        BaseGround baseGround1 = new BaseGround(Vector2.Zero, Vector2.One);
        baseGround1.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / baseGround1.getRect().width(), 0.3f));
        baseGround1.setPosition(new Vector2(0, GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height()));

        BaseGround baseGround2 = new BaseGround(Vector2.Zero, Vector2.One);
        baseGround2.setSize(new Vector2((float) GameConstants.SCREEN_WIDTH / baseGround2.getRect().width(), 0.3f));
        baseGround2.setPosition(new Vector2(baseGround2.getRect().width(), GameConstants.SCREEN_HEIGHT - baseGround2.getRect().height()));

        baseGrounds.add(baseGround1);
        baseGrounds.add(baseGround2);

        // Pipes
        Pipe pipe1U = new Pipe(Vector2.Zero, Vector2.multiply(Vector2.One, 0.4f));
        pipe1U.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + 1 * Pipe.INTERVAL, 0));
        pipe1U.boundYRange(Math.max(GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - 2 * pipe1U.getRect().height() - Pipe.GAP, (float) GameConstants.SCREEN_HEIGHT / 10 - pipe1U.getRect().height()), Math.min(0, (float) GameConstants.SCREEN_HEIGHT * 9 / 10 - baseGround1.getRect().height() - Pipe.GAP - pipe1U.getRect().height()));
        pipe1U.randomizeY();
        Pipe pipe1D = new Pipe(Vector2.multiply(Vector2.Up, pipe1U.getRect().height() + Pipe.GAP), Vector2.One);
        pipe1D.flip();
        pipe1D.setParent(pipe1U);

        Pipe pipe2U = new Pipe(Vector2.Zero, Vector2.multiply(Vector2.One, 0.4f));
        pipe2U.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + 2 * Pipe.INTERVAL, 0));
        pipe2U.boundYRange(Math.max(GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - 2 * pipe1U.getRect().height() - Pipe.GAP, (float) GameConstants.SCREEN_HEIGHT / 10 - pipe1U.getRect().height()), Math.min(0, (float) GameConstants.SCREEN_HEIGHT * 9 / 10 - baseGround1.getRect().height() - Pipe.GAP - pipe1U.getRect().height()));
        pipe2U.randomizeY();
        Pipe pipe2D = new Pipe(Vector2.multiply(Vector2.Up, pipe2U.getRect().height() + Pipe.GAP), Vector2.One);
        pipe2D.flip();
        pipe2D.setParent(pipe2U);

        Pipe pipe3U = new Pipe(Vector2.Zero, Vector2.multiply(Vector2.One, 0.4f));
        pipe3U.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + 3 * Pipe.INTERVAL, 0));
        pipe3U.boundYRange(Math.max(GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - 2 * pipe1U.getRect().height() - Pipe.GAP, (float) GameConstants.SCREEN_HEIGHT / 10 - pipe1U.getRect().height()), Math.min(0, (float) GameConstants.SCREEN_HEIGHT * 9 / 10 - baseGround1.getRect().height() - Pipe.GAP - pipe1U.getRect().height()));
        pipe3U.randomizeY();
        Pipe pipe3D = new Pipe(Vector2.multiply(Vector2.Up, pipe3U.getRect().height() + Pipe.GAP), Vector2.One);
        pipe3D.flip();
        pipe3D.setParent(pipe3U);

        pipes.add(pipe1U);
        pipes.add(pipe1D);
        pipes.add(pipe2U);
        pipes.add(pipe2D);
        pipes.add(pipe3U);
        pipes.add(pipe3D);

        // Coin
        int ranIndex = random.nextInt(3) * 2;
        coin = new Coin(Vector2.Zero, Vector2.multiply(Vector2.One, 1.1f));
        coin.setPosition(new Vector2(pipes.get(ranIndex).getPosition().x + (float) pipe1U.getRect().width() / 2 + Pipe.INTERVAL / 2 - (float) coin.getRect().width() / 2, 0));
        coin.boundYRange((float) GameConstants.SCREEN_HEIGHT / 10, GameConstants.SCREEN_HEIGHT - baseGround1.getRect().height() - (float) GameConstants.SCREEN_HEIGHT / 10);
        coin.randomizeY();



        // Timer and FrameRateShower
        timer = new Timer(Vector2.Zero, Vector2.multiply(Vector2.One, 1.5f));
        timer.CanGlow = true;
        timer.setAlignTopCenter(new Vector2((float) GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT * 0.1f));

        frameRateShower = new Timer(Vector2.Zero, Vector2.multiply(Vector2.One, 0.8f));
        frameRateShower.setDebugGreen();
        frameRateShower.setAlignTopRight(new Vector2(GameConstants.SCREEN_WIDTH, 0));

        // Load Object Speed
        reloadSpeed();

    }

    @Override
    protected void onRegistration()
    {
        ///////////////////////// Objects /////////////////////////
        // Bird
        registerObject(bird, 1);

        // BackGrounds
        for (BackGround backGround : backGrounds)
        {
            registerObject(backGround, 5);
        }

        // BaseGrounds
        for (BaseGround baseGround : baseGrounds)
        {
            registerObject(baseGround, 3);
        }

        // Pipes
        for (Pipe pipe : pipes)
        {
            registerObject(pipe, 4);
        }

        // Coin
        registerObject(coin, 2);



        // Timer and FrameRateShower
        registerObject(timer, 0);
        registerObject(frameRateShower, 0);
    }

    @Override
    public void renderUpdate()
    {
        bird.applyAnimation(16);
        coin.applyAnimation(24);

    }

    @Override
    public void logicUpdate()
    {
        // Bird
        if (!canControl && bird.getPosition().x >= (float) (100 * GameConstants.SCREEN_WIDTH) / 1080)
        {
            bird.setVelocity(new Vector2(0, bird.getVelocity().y));
            bird.setPosition(new Vector2((float) (100 * GameConstants.SCREEN_WIDTH) / 1080, bird.getPosition().y));
            timer.timerUpdate(0);
            startTime = System.currentTimeMillis();

            canControl = true;
        }
        else if (!canControl && bird.getPosition().y > GameConstants.SCREEN_HEIGHT * 0.45f) {
            playSound(R.raw.flap_sound);
            bird.setVelocity(new Vector2(bird.getVelocity().x, 1.75f));
        }

        // Pipes
        for (Pipe pipe : pipes)
        {
            pipe.logicUpdate();
        }

        // Timer
        int elapsedTime = (int)(System.currentTimeMillis() - startTime) / 1000;
        float elapsedFrameTime = (float)(System.currentTimeMillis() - lastFrameShowTime) / 1000.0f;

        if (canControl && elapsedTime >= 1)
        {
            timeCount += (int) ((System.currentTimeMillis() - startTime) / 1000);
            startTime = System.currentTimeMillis();
            lastFrameShowTime = System.currentTimeMillis();
            timer.timerUpdate(timeCount);
        }

        //Frame Rate Shower
        if (isDebug && GameConstants.DELTA_TIME != 0 && elapsedFrameTime >= 0.5f)
        {
            frameRateShower.timerUpdate((int) (1000 / GameConstants.DELTA_TIME));
            lastFrameShowTime = System.currentTimeMillis();
        }


        // State Checks
        cycleCheck();

        if (!bird.IsDead)
        {
            checkCollision();
        }
        else
        {
            // GameOver Event
            if (!isGameOver)
            {
                onGameOver();
            }

            // Stop GameScene Threads
            if (bird.getPosition().y > GameConstants.SCREEN_HEIGHT)
            {
                freeze();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // If dead, cannot control, is game over, get rid of this touch action
        if (!canControl || isGameOver || bird == null || bird.IsDead)
        {
            return true;
        }

        // Handle Touch Screen
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
                playSound(R.raw.flap_sound);
                bird.flap();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void start()
    {
        reloadSpeed();
        super.start();
    }

    public void reset()
    {
        ///////////////////////// Objects /////////////////////////
        // Bird
        bird.setPosition(new Vector2(-bird.getRawRect().width(), GameConstants.SCREEN_HEIGHT * 0.3f));
        bird.IsDead = false;

        // BackGround
        backGrounds.get(0).setPosition(Vector2.Zero);
        backGrounds.get(1).setPosition(new Vector2(GameConstants.SCREEN_WIDTH, 0));

        // BaseGround
        baseGrounds.get(0).setPosition(new Vector2(0, GameConstants.SCREEN_HEIGHT - baseGrounds.get(0).getRect().height()));
        baseGrounds.get(1).setPosition(new Vector2(baseGrounds.get(1).getRect().width(), GameConstants.SCREEN_HEIGHT - baseGrounds.get(1).getRect().height()));

        // Pipes
        for (int i = 0; i < pipes.size() / 2; i++)
        {
            pipes.get(2 * i).setPosition(new Vector2(GameConstants.SCREEN_WIDTH + (i + 1) * Pipe.INTERVAL, 0));
            pipes.get(2 * i).randomizeY();
            pipes.get(2 * i).toggleMovePipe(false);
            pipes.get(2 * i + 1).logicUpdate();
        }

        // Coin
        int ranIndex = random.nextInt(3) * 2;
        coin.setPosition(new Vector2(pipes.get(ranIndex).getPosition().x + (float) pipes.get(0).getRect().width() / 2 + Pipe.INTERVAL / 2 - (float) coin.getRect().width() / 2, 0));
        coin.randomizeY();


        // Timer and FrameRateShower
        timer.setSpriteColor(Color.TRANSPARENT);
        frameRateShower.setSpriteColor(Color.TRANSPARENT);


        ///////////////////////// States /////////////////////////
        isGameOver = false;
        canControl = false;
        coinCount = 0;
        timeCount = 0;
        reloadSpeed();
        invalidate();
    }

    public void restart()
    {
        if (isGameOver)
        {
            reset();
        }

        score = 0;
        reloadSpeed();
        unfreeze();
    }

    public void toggleDebugFrame()
    {
        isDebug = !isDebug;
    }

    private void cycleCheck() {
        // BackGround
        for (BackGround backGround : backGrounds) {
            if (backGround.getPosition().x <= -GameConstants.SCREEN_WIDTH) {
                float offset = backGround.getPosition().x + GameConstants.SCREEN_WIDTH;
                backGround.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + offset, backGround.getPosition().y));
            }
        }

        // BaseGround
        for (BaseGround baseGround : baseGrounds) {
            if (baseGround.getPosition().x <= -GameConstants.SCREEN_WIDTH) {
                float offset = baseGround.getPosition().x + GameConstants.SCREEN_WIDTH;
                baseGround.setPosition(new Vector2(GameConstants.SCREEN_WIDTH + offset, baseGround.getPosition().y));
            }
        }

        // Pipes
        float pipeX = 0;
        for (int i = 0; i < pipes.size() / 2; i++) {
            int upperPipeIndex = 2 * i;
            int lowerPipeIndex = upperPipeIndex + 1;

            // Ensure that the indices are valid
            if (upperPipeIndex < pipes.size() && lowerPipeIndex < pipes.size()) {
                float offset = pipes.get(upperPipeIndex).getPosition().x + pipes.get(upperPipeIndex).getRect().width();

                if (offset < 0) {
                    pipes.get(upperPipeIndex).setPosition(new Vector2(3 * Pipe.INTERVAL - pipes.get(upperPipeIndex).getRect().width() + offset, 0f));
                    pipes.get(upperPipeIndex).randomizeY();
                    pipes.get(upperPipeIndex).randomizeToggleMove();
                }

                pipeX = Math.max(pipes.get(upperPipeIndex).getPosition().x, pipeX);
            }
        }

        // Coin
        if (coin.getPosition().x < -coin.getRect().width()) {
            float ranRange = random.nextInt(2);
            coin.setPosition(new Vector2(pipeX + ranRange * Pipe.INTERVAL - (Pipe.INTERVAL - pipes.get(0).getRect().width()) / 2 - (float) coin.getRect().width() / 2, 0));
            coin.randomizeY();
        }
    }


    private void checkCollision()
    {
        // Check Bird and Ground
        for (BaseGround baseGround : baseGrounds)
        {
            if (bird.collisionCheck(baseGround))
            {
                playSound(R.raw.damage_sound);
                bird.IsDead = true;

                bird.setVelocity(new Vector2(bird.getVelocity().x, 3.8f));
            }
        }

        // Check Bird and Pipes
        for (Pipe pipe : pipes)
        {
            if (bird.collisionCheck(pipe))
            {
                playSound(R.raw.damage_sound);
                bird.IsDead = true;

            }

        }

        // Check Bird and Coin
        if (bird.collisionCheck(coin))
        {
            playSound(R.raw.coin_sound);
            coin.setPosition(new Vector2(-coin.getRect().width(), 0));
            coinCount++;

        }


    }

    private void reloadSpeed()
    {
        // Bird
        bird.setVelocity(Vector2.multiply(Vector2.Right, fixed_speed / 5));

        // BackGround
        backGrounds.get(0).setVelocity(Vector2.multiply(Vector2.Left, fixed_speed / 12));
        backGrounds.get(1).setVelocity(Vector2.multiply(Vector2.Left, fixed_speed / 12));

        // BaseGround
        baseGrounds.get(0).setVelocity(Vector2.multiply(Vector2.Left, fixed_speed));
        baseGrounds.get(1).setVelocity(Vector2.multiply(Vector2.Left, fixed_speed));

        // Pipes
        for (int i = 0; i < pipes.size() / 2; i++)
        {
            pipes.get(2 * i).setVelocity(Vector2.multiply(Vector2.Left, fixed_speed));
            pipes.get(2 * i + 1).logicUpdate();
        }

        // Coin
        coin.setVelocity(Vector2.multiply(Vector2.Left, fixed_speed));
    }

    private void onGameOver() {
        playSound(R.raw.die_sound);
        score = 5 * coinCount + 10 * timeCount;
        isGameOver = true;

        // Stop Scene Move
        for (BackGround backGround : backGrounds) {
            backGround.setVelocity(Vector2.Zero);
        }

        for (BaseGround baseGround : baseGrounds) {
            baseGround.setVelocity(Vector2.Zero);
        }

        for (Pipe pipe : pipes) {
            pipe.setVelocity(Vector2.Zero);
        }

        coin.setVelocity(Vector2.Zero);

        // Start Bird Horizontal Move
        bird.setVelocity(new Vector2(fixed_speed, bird.getVelocity().y));

        // Store the score, coins, and best score in the database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int bestScore = getBestScore(dbHelper);
        if (score > bestScore) {
            bestScore = score;
        }
        dbHelper.insertStats(score, coinCount, bestScore);

        // Show Game Over Dialog
        final GameplayActivity gameplayActivity = (GameplayActivity) getContext();
        int finalBestScore = bestScore;
        gameplayActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameplayActivity.showGameOverDialog(score, finalBestScore);
            }
        });
    }

    private int getBestScore(DatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getStats();
        if (cursor != null && cursor.moveToFirst()) {
            int bestScore = cursor.getInt(cursor.getColumnIndexOrThrow("best_score"));
            cursor.close();
            return bestScore;
        }
        return 0;
    }


}
