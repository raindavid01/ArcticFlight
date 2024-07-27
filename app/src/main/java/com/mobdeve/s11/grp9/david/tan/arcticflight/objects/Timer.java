package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;

import java.util.ArrayList;

public class Timer extends GameObject {
    // Render
    public final int[] NUM_SIZE = new int[2];  // Size of number sprites
    public boolean CanGlow;  // Flag to indicate if the timer can glow
    private int timeCount;  // Current time count
    private Vector2 alignTopLeft = Vector2.Zero.copy();  // Position to align top-left
    private Vector2 alignTopCenter = null;  // Position to align top-center
    private Vector2 alignTopRight = null;  // Position to align top-right
    private final int GAP = 5;  // Gap between number sprites
    private final ArrayList<Bitmap> numSprites = new ArrayList<>();  // List of number sprites

    /**
     * Constructs a new Timer instance with the given position and size.
     *
     * @param position The top-left position of the game object
     * @param size     The size of the game object
     */
    public Timer(Vector2 position, Vector2 size) {
        super(position, size);

        // Initialize number sprites
        numSprites.add(Sprite.loadSprite(R.drawable.number0));
        numSprites.add(Sprite.loadSprite(R.drawable.number1));
        numSprites.add(Sprite.loadSprite(R.drawable.number2));
        numSprites.add(Sprite.loadSprite(R.drawable.number3));
        numSprites.add(Sprite.loadSprite(R.drawable.number4));
        numSprites.add(Sprite.loadSprite(R.drawable.number5));
        numSprites.add(Sprite.loadSprite(R.drawable.number6));
        numSprites.add(Sprite.loadSprite(R.drawable.number7));
        numSprites.add(Sprite.loadSprite(R.drawable.number8));
        numSprites.add(Sprite.loadSprite(R.drawable.number9));

        // Set the size of the number sprites
        NUM_SIZE[0] = numSprites.get(0).getWidth();
        NUM_SIZE[1] = numSprites.get(0).getHeight();
        timeCount = 0;
        CanGlow = false;
    }

    /**
     * Sets the alignment to top-left.
     *
     * @param alignPos The top-left alignment position
     */
    public void setAlignTopLeft(Vector2 alignPos) {
        alignTopLeft = alignPos;
        alignTopCenter = null;
        alignTopRight = null;
    }

    /**
     * Sets the alignment to top-center.
     *
     * @param alignPos The top-center alignment position
     */
    public void setAlignTopCenter(Vector2 alignPos) {
        alignTopLeft = null;
        alignTopCenter = alignPos;
        alignTopRight = null;
    }

    /**
     * Sets the alignment to top-right.
     *
     * @param alignPos The top-right alignment position
     */
    public void setAlignTopRight(Vector2 alignPos) {
        alignTopLeft = null;
        alignTopCenter = null;
        alignTopRight = alignPos;
    }

    /**
     * Updates the timer with the given time.
     *
     * @param time The new time count
     */
    public void timerUpdate(int time) {
        timeCount = time;

        // Create a new thread for calculating the image result
        new Thread(new Runnable() {
            @Override
            public void run() {
                rawTimerUpdate(time);
            }
        }).start();
    }

    /**
     * Updates the timer's display with the given time.
     *
     * @param time The new time count
     */
    public void rawTimerUpdate(int time) {
        int digitWidth = NUM_SIZE[0];
        int digitHeight = NUM_SIZE[1];
        int bitmapWidth = (digitWidth + GAP) * String.valueOf(time).length() - GAP;
        Bitmap timerBitmap = Bitmap.createBitmap(bitmapWidth, digitHeight, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[bitmapWidth * digitHeight];
        int index = 0;

        // Draw each digit of the time onto the bitmap
        for (char digit : String.valueOf(time).toCharArray()) {
            int digitInt = Character.getNumericValue(digit);
            Log.d("Timer", "Processing digit: " + digitInt);

            Bitmap digitBitmap = numSprites.get(digitInt);
            int originalDigitWidth = digitBitmap.getWidth();
            int originalDigitHeight = digitBitmap.getHeight();
            int x = (digitWidth - originalDigitWidth) / 2 + index;

            for (int i = 0; i < originalDigitHeight; i++) {
                for (int j = 0; j < originalDigitWidth; j++) {
                    int pixelX = x + j;
                    if (pixelX >= 0 && pixelX < bitmapWidth) {
                        pixels[i * bitmapWidth + pixelX] = digitBitmap.getPixel(j, i);
                    }
                }
            }

            index += digitWidth + GAP;
        }

        timerBitmap.setPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, digitHeight);
        sprite = timerBitmap;

        // Align the position
        if (alignTopCenter != null) {
            position.x = alignTopCenter.x - (float) getRect().width() / 2;
            position.y = alignTopCenter.y;
        } else if (alignTopLeft != null) {
            position.x = alignTopLeft.x;
            position.y = alignTopLeft.y;
        } else {
            position.x = alignTopRight.x - getRect().width();
            position.y = alignTopRight.y;
        }
    }

    /**
     * Sets the number sprites to green for debugging.
     */
    public void setDebugGreen() {
        numSprites.replaceAll(sprite -> Sprite.colorSprite(sprite, Color.GREEN));
    }

    /**
     * Cleans up resources by recycling bitmaps.
     */
    public void cleanup() {
        // Recycle and nullify bitmaps
        for (Bitmap numSprite : numSprites) {
            if (numSprite != null) {
                numSprite.recycle();
            }
        }
        numSprites.clear();
    }
}
