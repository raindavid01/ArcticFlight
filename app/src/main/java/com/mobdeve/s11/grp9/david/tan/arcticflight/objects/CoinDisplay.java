package com.mobdeve.s11.grp9.david.tan.arcticflight.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;

import com.mobdeve.s11.grp9.david.tan.arcticflight.R;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Sprite;
import com.mobdeve.s11.grp9.david.tan.arcticflight.structs.Vector2;
import com.mobdeve.s11.grp9.david.tan.arcticflight.utils.GameObject;

public class CoinDisplay extends GameObject {
    // Paint object for drawing the coin count text
    private Paint coinCountPaint;
    // Bitmap for the coin icon
    private Bitmap coinIcon;
    // Bitmap for the combined coin icon and text
    private Bitmap combinedBitmap;
    // Current coin count
    private int coinCount;

    /**
     * Constructs a new CoinDisplay instance with the given position, size, and initial coin count.
     *
     * @param context  The context for accessing resources.
     * @param position The top-left position of the game object.
     * @param size     The size of the game object.
     * @param coinCount The initial coin count.
     */
    public CoinDisplay(Context context, Vector2 position, Vector2 size, int coinCount) {
        super(position, size);

        this.coinCount = coinCount;

        // Initialize Paint object for the coin count text
        coinCountPaint = new Paint();
        coinCountPaint.setColor(Color.parseColor("#18284a"));
        coinCountPaint.setTextSize(75);
        coinCountPaint.setTypeface(ResourcesCompat.getFont(context, R.font.pixelated));

        // Load and downsize the coin icon
        Bitmap originalCoinIcon = Sprite.loadSprite(R.drawable.coin1);
        coinIcon = Bitmap.createScaledBitmap(originalCoinIcon,
                (int)(originalCoinIcon.getWidth() * size.x),
                (int)(originalCoinIcon.getHeight() * size.y),
                true);

        // Create the initial combined bitmap with the coin icon and coin count
        combinedBitmap = createCombinedBitmap(coinIcon, coinCount);
        setSprite(combinedBitmap);
    }

    /**
     * Creates a combined bitmap with the coin icon and coin count text.
     *
     * @param icon  The bitmap of the coin icon.
     * @param count The current coin count.
     * @return The combined bitmap.
     */
    private Bitmap createCombinedBitmap(Bitmap icon, int count) {
        // Calculate text size and position
        int textWidth = (int) coinCountPaint.measureText(count + " Coins");
        int iconWidth = icon.getWidth();
        int iconHeight = icon.getHeight();
        int combinedWidth = iconWidth + textWidth + 10;
        int combinedHeight = Math.max(iconHeight, (int) coinCountPaint.getTextSize());

        // Create a bitmap for the combined icon and text
        Bitmap combinedBitmap = Bitmap.createBitmap(combinedWidth, combinedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        // Draw the icon on the canvas
        canvas.drawBitmap(icon, 0, (combinedHeight - iconHeight) / 2, null);

        // Draw the text on the canvas
        canvas.drawText(count + " Coins", iconWidth + 10, (combinedHeight + coinCountPaint.getTextSize()) / 2 - 10, coinCountPaint);

        return combinedBitmap;
    }

    /**
     * Updates the coin count and recreates the combined bitmap.
     *
     * @param coinCount The new coin count.
     */
    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
        // Recreate the combined bitmap with the updated coin count
        combinedBitmap = createCombinedBitmap(coinIcon, coinCount);
        setSprite(combinedBitmap);
    }

    /**
     * Cleans up resources by recycling bitmaps.
     */
    public void cleanup() {
        if (coinIcon != null) {
            coinIcon.recycle();
            coinIcon = null;
        }
        if (combinedBitmap != null) {
            combinedBitmap.recycle();
            combinedBitmap = null;
        }
    }
}
