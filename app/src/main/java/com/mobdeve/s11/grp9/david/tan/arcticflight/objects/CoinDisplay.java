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
    // Render
    private Paint coinCountPaint;
    private Bitmap coinIcon;
    private Bitmap combinedBitmap;
    private int coinCount;

    /**
     * Constructs a new GameObject instance with the given position and size.
     *
     * @param context  The context for accessing resources.
     * @param position The top-left position of the game object.
     * @param size     The size of the game object.
     * @param coinCount The initial coin count.
     */
    public CoinDisplay(Context context, Vector2 position, Vector2 size, int coinCount) {
        super(position, size);

        this.coinCount = coinCount;

        // Initialize Paint object
        coinCountPaint = new Paint();
        coinCountPaint.setColor(Color.parseColor("#18284a"));
        coinCountPaint.setTextSize(75);
        coinCountPaint.setTypeface(ResourcesCompat.getFont(context, R.font.pixelated));

        // Load and downsize coin icon as sprite
        Bitmap originalCoinIcon = Sprite.loadSprite(R.drawable.coin1);
        coinIcon = Bitmap.createScaledBitmap(originalCoinIcon,
                (int)(originalCoinIcon.getWidth() * size.x),
                (int)(originalCoinIcon.getHeight() * size.y),
                true);

        // Create initial combined bitmap
        combinedBitmap = createCombinedBitmap(coinIcon, coinCount);
        setSprite(combinedBitmap);
    }

    private Bitmap createCombinedBitmap(Bitmap icon, int count) {
        // Calculate text size and position
        int textWidth = (int) coinCountPaint.measureText(count + " Coins");
        int iconWidth = icon.getWidth();
        int iconHeight = icon.getHeight();
        int combinedWidth = iconWidth + textWidth + 10;
        int combinedHeight = Math.max(iconHeight, (int) coinCountPaint.getTextSize());

        Bitmap combinedBitmap = Bitmap.createBitmap(combinedWidth, combinedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        // Draw the icon
        canvas.drawBitmap(icon, 0, (combinedHeight - iconHeight) / 2, null);

        // Draw the text
        canvas.drawText(count + " Coins", iconWidth + 10, (combinedHeight + coinCountPaint.getTextSize()) / 2 - 10, coinCountPaint);

        return combinedBitmap;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
        // Recreate the combined bitmap with the updated coin count
        combinedBitmap = createCombinedBitmap(coinIcon, coinCount);
        setSprite(combinedBitmap);
    }

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
