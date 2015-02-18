package lri.prototype.cosquare.app.bff;

import android.graphics.Color;

/**
 * Created by louis on 30/04/2014.
 * This class is for colors management
 */
public class ColorManager {

    /**
     * @param p : an int between 0 and 100. 0 gives color X and 100 color Y.
     * @return a color between X and Y.
     */
    public static int getColor(int p,int color){
        float progress = Math.min(100f,Math.max(0f,p));
        float[] hsv = new float[3];
        Color.colorToHSV(color,hsv);
        if(progress<50f){
            hsv[1]= hsv[1] + (1f-hsv[1])*(0.5f - progress/100f);
            hsv[2]= hsv[2]*(0.5f + progress/100f);
        }else {
            hsv[1] = hsv[1] * (1.75f - 1.5f * progress / 100f);
            hsv[2] = hsv[2] + (1f - hsv[2]) * (1.5f * progress / 100f - 0.75f);
        }
        return Color.HSVToColor(hsv);
    }






}
