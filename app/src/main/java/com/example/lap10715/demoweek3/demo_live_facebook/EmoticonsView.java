package com.example.lap10715.demoweek3.demo_live_facebook;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.lap10715.demoweek3.R;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class EmoticonsView extends View {
    private final int X_STEP = 8, Y_OFFSET = 100, Y_RANGE = 400;

    private Paint paint;
    private Path animPath;
    private Matrix matrix;
    private Bitmap bmLike, bmHaha, bmLove, bmSad, bmAngry, bmWow;

    private ArrayList<LiveEmoticon> liveEmoticons = new ArrayList<>();
    private int screenWidth;

    public EmoticonsView(Context context) {
        super(context);
    }

    public EmoticonsView(Context activity, AttributeSet attrs) {
        super(activity, attrs);
    }

    public EmoticonsView(Context activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
    }

    public void initView(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        paint = new Paint();

        animPath = new Path();
        matrix = new Matrix();

        Resources res = getResources();

        bmLike = BitmapFactory.decodeResource(res, R.drawable.like_48);
        bmLove = BitmapFactory.decodeResource(res, R.drawable.love_48);
        bmHaha = BitmapFactory.decodeResource(res, R.drawable.haha_48);
        bmWow = BitmapFactory.decodeResource(res, R.drawable.wow_48);
        bmSad = BitmapFactory.decodeResource(res, R.drawable.sad_48);
        bmAngry = BitmapFactory.decodeResource(res, R.drawable.angry_48);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawPath(animPath, paint);
        drawAllLiveEmoticons(canvas);
    }

    private void drawAllLiveEmoticons(Canvas canvas) {
        ListIterator<LiveEmoticon> iterator = liveEmoticons.listIterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            LiveEmoticon liveEmoticon = (LiveEmoticon) object;
            Integer xCoordinate = liveEmoticon.getX() - X_STEP;
            Integer yCoordinate = liveEmoticon.getY();
            liveEmoticon.setX(xCoordinate);
            if (xCoordinate > 0) {
                matrix.reset();
                matrix.postTranslate(xCoordinate, yCoordinate);
                resizeImageSizeBasedOnXCoordinates(canvas, liveEmoticon);
                invalidate();
            } else {
                iterator.remove();
            }
        }
    }

    private void resizeImageSizeBasedOnXCoordinates(Canvas canvas, LiveEmoticon liveEmoticon) {
        if (liveEmoticon == null) {
            return;
        }

        int xCoordinate = liveEmoticon.getX();
        Bitmap bitMap = null;
        Bitmap scaled = null;

        Emoticons emoticons = liveEmoticon.getEmoticons();
        if (emoticons == null) {
            return;
        }

        switch (emoticons) {
            case LIKE:
                bitMap = bmLike;
                break;
            case LOVE:
                bitMap = bmLove;
                break;
            case HAHA:
                bitMap = bmHaha;
                break;
            case WOW:
                bitMap = bmWow;
                break;
            case SAD:
                bitMap = bmSad;
                break;
            case ANGRY:
                bitMap = bmAngry;
                break;
        }

        if (xCoordinate > screenWidth / 2) {
            canvas.drawBitmap(bitMap, matrix, null);
        } else if (xCoordinate > screenWidth / 4) {
            scaled = Bitmap.createScaledBitmap(bitMap, 3 * bitMap.getWidth() / 4,
                    3 * bitMap.getHeight() / 4, false);
            canvas.drawBitmap(scaled, matrix, null);
        } else {
            scaled = Bitmap.createScaledBitmap(bitMap, bitMap.getWidth() / 2,
                    bitMap.getHeight() / 2, false);
            canvas.drawBitmap(scaled, matrix, null);
        }
    }

    public void addView(Emoticons emoticons) {
        int startXCoordinate = screenWidth;
        int startYCoordinate = new Random().nextInt(Y_RANGE) + Y_OFFSET;
        LiveEmoticon liveEmoticon = new LiveEmoticon(emoticons, startXCoordinate,
                startYCoordinate);
        liveEmoticons.add(liveEmoticon);
        invalidate();
    }
}
