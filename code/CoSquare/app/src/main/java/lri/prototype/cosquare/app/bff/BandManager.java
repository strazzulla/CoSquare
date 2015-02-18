package lri.prototype.cosquare.app.bff;

import android.app.WallpaperManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;

import java.io.IOException;

import lri.prototype.cosquare.app.sqldatabase.ColorTokenReceived;
import lri.prototype.cosquare.app.sqldatabase.ColorTokenSent;
import lri.prototype.cosquare.app.sqldatabase.LocationTokenReceived;
import lri.prototype.cosquare.app.sqldatabase.LocationTokenSent;

/**
 * Created by louis on 20/06/2014.
 */
public class BandManager {

    private static final String TAG = "BAND_MANAGER";
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    private static final int LENGTH_TIMELINE = 10*HOUR;
    private static final int LENGTH_UNIT = HOUR;
    private static final int WIDTH_BITMAP = 1024;
    private static final int HEIGHT_BITMAP = 256;


    public static void setWallpaper(int color,Context context){
        int[] colors = new int[1];
        colors[0] = color;
                WallpaperManager wall = WallpaperManager.getInstance(context);
        try {
            wall.setBitmap(Bitmap.createBitmap(colors, 1, 1, Bitmap.Config.ARGB_8888));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapReceived(Context context){
        Cursor cursor = new LocationTokenReceived(context).getAll();
        if(cursor.getCount()==0) {
            cursor.close();
            return getBitmapBlack(context);
        }
        int len = LENGTH_TIMELINE / LENGTH_UNIT;
        int[] distances = new int[len];
        int[] weights = new int[len];
        int[] colors = new int[len];
        Time now = new Time();
        now.setToNow();
        long nowMillis = now.toMillis(false);
        int lastColorSent = ColorTokenReceived.getLastColor(context);

        int timeIndex = cursor.getColumnIndex(LocationTokenSent.COLUMN_TIME);
        int distanceIndex = cursor.getColumnIndex(LocationTokenSent.COLUMN_DISTANCE);
        cursor.moveToLast();

        while(!cursor.isBeforeFirst()){
            long timeMillis = Long.parseLong(cursor.getString(timeIndex));
            int k = (int)((nowMillis - timeMillis)/(LENGTH_UNIT));

            if(k<len && k>=0){
                distances[len-1-k]=(cursor.getInt(distanceIndex)+ distances[len-1-k])/2;
                weights[len-1-k]+=1;
            }
            cursor.moveToPrevious();
        }

        for(int i = 0; i<len; i++){
            if(weights[i]!=0)colors[i] = ColorManager.getColor(distances[i],lastColorSent);
        }

        cursor.close();
        Bitmap bitmap = Bitmap.createBitmap(colors,len,1,Bitmap.Config.ARGB_8888);


        return getBitmapColorReceived(Bitmap.createScaledBitmap(bitmap, WIDTH_BITMAP, HEIGHT_BITMAP, false), context);
    }

    public static Bitmap getBitmapSent(Context context){
        Cursor cursor = new LocationTokenSent(context).getAll();
        if(cursor.getCount()==0) {
            cursor.close();
            return getBitmapColorSent(getBitmapBlack(context), context);
        }
        int len = LENGTH_TIMELINE / LENGTH_UNIT;
        int[] distances = new int[len];
        int[] weights = new int[len];
        int[] colors = new int[len];
        Time now = new Time();
        now.setToNow();
        long nowMillis = now.toMillis(false);
        int lastColorSent = ColorTokenSent.getLastColor(context);

        int timeIndex = cursor.getColumnIndex(LocationTokenSent.COLUMN_TIME);
        int distanceIndex = cursor.getColumnIndex(LocationTokenSent.COLUMN_DISTANCE);
        cursor.moveToLast();

        while(!cursor.isBeforeFirst()){
            long timeMillis = Long.parseLong(cursor.getString(timeIndex));
            int k = (int)((nowMillis - timeMillis)/(LENGTH_UNIT));

            if(k<len && k>=0){
                distances[len-1-k]=(cursor.getInt(distanceIndex)+ distances[len-1-k])/2;
                weights[len-1-k]+=1;
            }
            cursor.moveToPrevious();
        }

        for(int i = 0; i<len; i++){
            if(weights[i]!=0)colors[i] = ColorManager.getColor(distances[i],lastColorSent);
        }

        cursor.close();
        Bitmap bitmap = Bitmap.createBitmap(colors,len,1,Bitmap.Config.ARGB_8888);


        return getBitmapColorSent(Bitmap.createScaledBitmap(bitmap, WIDTH_BITMAP, HEIGHT_BITMAP, false), context);
    }

    public static Bitmap getBitmapColorReceived(Bitmap b, Context context){
        Cursor cursor = new ColorTokenReceived(context).getAll();
        if(cursor.getCount()==0){
            cursor.close();
            return b;
        }
        Bitmap bitmap = Bitmap.createBitmap(b);
        Log.d(TAG,"width : " + bitmap.getWidth() + "     height : " + bitmap.getHeight());
        Time now = new Time();
        now.setToNow();
        long nowMillis = now.toMillis(false);

        //Log.d(TAG,"while...");
        int timeIndex = cursor.getColumnIndex(ColorTokenSent.COLUMN_TIME);
        //Log.d(TAG,"timeIndex : " + timeIndex);
        int colorIndex = cursor.getColumnIndex(ColorTokenSent.COLUMN_COLOR);
        //Log.d(TAG,"colorIndex : " + colorIndex);
        cursor.moveToLast();

        while(!cursor.isBeforeFirst()){
            long timeMillis = Long.parseLong(cursor.getString(timeIndex));
            int k = (int)(nowMillis-timeMillis)/SECOND;
            int color = cursor.getInt(colorIndex);
            for(int i = 0; i< HEIGHT_BITMAP; i++){
                for(int j = -HEIGHT_BITMAP/4; j<=HEIGHT_BITMAP/4; j++) {
                    int x = WIDTH_BITMAP - (WIDTH_BITMAP * k) / (LENGTH_TIMELINE/SECOND) - 1 + j;
                    int y = i;
                    if ((HEIGHT_BITMAP / 2 - i) * (HEIGHT_BITMAP / 2 - i) + j * j < HEIGHT_BITMAP * HEIGHT_BITMAP / 58)
                        if (x >= 0 && y >= 0 && x < WIDTH_BITMAP && y < HEIGHT_BITMAP)
                            bitmap.setPixel(x, y, Color.BLACK);
                    if ((HEIGHT_BITMAP / 2 - i) * (HEIGHT_BITMAP / 2 - i) + j * j < HEIGHT_BITMAP * HEIGHT_BITMAP / 64)
                        if (x >= 0 && y >= 0 && x < WIDTH_BITMAP && y < HEIGHT_BITMAP)
                            bitmap.setPixel(x, y, color);
                }
            }
            cursor.moveToPrevious();
        }

        cursor.close();
        return bitmap;//Bitmap.createScaledBitmap(bitmap,1024,256,true);
    }

    public static Bitmap getBitmapColorSent(Bitmap b, Context context){
        Cursor cursor = new ColorTokenSent(context).getAll();
        if(cursor.getCount()==0){
            cursor.close();
            return b;
        }
        Bitmap bitmap = Bitmap.createBitmap(b);
        Time now = new Time();
        now.setToNow();
        long nowMillis = now.toMillis(false);

        //Log.d(TAG,"while...");
        int timeIndex = cursor.getColumnIndex(ColorTokenSent.COLUMN_TIME);
        //Log.d(TAG,"timeIndex : " + timeIndex);
        int colorIndex = cursor.getColumnIndex(ColorTokenSent.COLUMN_COLOR);
        //Log.d(TAG,"colorIndex : " + colorIndex);
        cursor.moveToLast();

        while(!cursor.isBeforeFirst()){
            long timeMillis = Long.parseLong(cursor.getString(timeIndex));
            int k = (int)(nowMillis-timeMillis)/SECOND;
            int color = cursor.getInt(colorIndex);
            for(int i = 0; i< HEIGHT_BITMAP; i++){
                for(int j = -HEIGHT_BITMAP/4; j<=HEIGHT_BITMAP/4; j++) {
                    int x = WIDTH_BITMAP - (WIDTH_BITMAP * k) / (LENGTH_TIMELINE/SECOND) - 1 + j;
                    int y = i;
                    if ((HEIGHT_BITMAP / 2 - i) * (HEIGHT_BITMAP / 2 - i) + j * j < HEIGHT_BITMAP * HEIGHT_BITMAP / 58)
                        if (x >= 0 && y >= 0 && x < WIDTH_BITMAP && y < HEIGHT_BITMAP)
                            bitmap.setPixel(x, y, Color.BLACK);
                    if ((HEIGHT_BITMAP / 2 - i) * (HEIGHT_BITMAP / 2 - i) + j * j < HEIGHT_BITMAP * HEIGHT_BITMAP / 64)
                        if (x >= 0 && y >= 0 && x < WIDTH_BITMAP && y < HEIGHT_BITMAP)
                            bitmap.setPixel(x, y, color);
                }
            }
            cursor.moveToPrevious();
        }

        cursor.close();
        return bitmap;//Bitmap.createScaledBitmap(bitmap,1024,256,true);
    }

    public static Bitmap getBitmapColorTest(Context context){
        int[] colors = new int[8];
        int color = ColorTokenReceived.getLastColor(context);
        colors[0]=ColorManager.getColor(0,color);
        colors[1]=ColorManager.getColor(14,color);
        colors[2]=ColorManager.getColor(28,color);
        colors[3]=ColorManager.getColor(42,color);
        colors[4]=ColorManager.getColor(56,color);
        colors[5]=ColorManager.getColor(70,color);
        colors[6]=ColorManager.getColor(84,color);
        colors[7]=ColorManager.getColor(98,color);
        Bitmap bitmap = Bitmap.createBitmap(colors,8,1,Bitmap.Config.ARGB_8888);
        return Bitmap.createScaledBitmap(bitmap,1024,256,false);
    }

    public static Bitmap getBitmapBlack(Context context){
        int[] colors = new int[1];
        int color = Color.BLACK;
        colors[0]= ColorManager.getColor(0,color);
        Bitmap bitmap = Bitmap.createBitmap(colors,1,1,Bitmap.Config.ARGB_8888);
        return Bitmap.createScaledBitmap(bitmap,1024,256,false);
    }

}
