package lri.prototype.cosquare.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import lri.prototype.cosquare.app.MainActivity;
import lri.prototype.cosquare.app.R;
import lri.prototype.cosquare.app.bff.BandManager;
import lri.prototype.cosquare.app.settings.SettingsUtils;

/**
 * Implementation of App Widget functionality.
 */
public class BandWidget extends AppWidgetProvider {

    static final String TAG = "BAND_WIDGET";
    static public final String ACTION_UPDATE_BAND = "lri.prototype.cosquare.app.widget.ACTION_UPDATE_BAND";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    public void onReceive(Context context,Intent intent){
        Log.d(TAG, "onReceive");
        super.onReceive(context,intent);
        final String action = intent.getAction();
        if (ACTION_UPDATE_BAND.equals(action)) {
            AppWidgetManager awp = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context,BandWidget.class);
            onUpdate(context, awp, awp.getAppWidgetIds(cn));
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId)
    {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_band);
        //Check if settings have been completed
        if(SettingsUtils.settingsCompleted(context)){
            views.setImageViewBitmap(R.id.bff_band_image_view, BandManager.getBitmapReceived(context));
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            views.setOnClickPendingIntent(R.id.bff_band_image_view, PendingIntent.getActivity(context,Context.MODE_PRIVATE,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        }
        else{
            views.setImageViewBitmap(R.id.bff_band_image_view, BandManager.getBitmapBlack(context));
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            views.setOnClickPendingIntent(R.id.bff_band_image_view, PendingIntent.getActivity(context,Context.MODE_PRIVATE,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


