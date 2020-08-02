package com.udacity.chukwuwauchenna.myadventuresudacity.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.chukwuwauchenna.myadventuresudacity.MainActivity;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;
import com.udacity.chukwuwauchenna.myadventuresudacity.ui.journal.JournalActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.ID_PREF;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.NAME_PREF;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.PLACES_PREF;
import static com.udacity.chukwuwauchenna.myadventuresudacity.util.Constants.WIDGET_PREF;

/**
 * Implementation of App Widget functionality.
 */
public class AdventureWidget extends AppWidgetProvider {

    public static List<Place> places = new ArrayList<>();
    private static String text;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<List<Place>>() {
        }.getType();
        String placesString = sharedPreferences.getString(PLACES_PREF,null);
        Log.d("TAG", "updateAppWidget: " + placesString);
        places = gson.fromJson(placesString, itemType);

        text = sharedPreferences.getString(NAME_PREF, "no recipe");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.adventure_widget);
        views.setTextViewText(R.id.widget_text_app, text);
        //open JournalActivity when title is clicked
        Intent clickIntent = new Intent(context, JournalActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_text_app, pendingIntent);
        //set adapter
        Intent intent = new Intent(context, PlacesWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
    }
    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, AdventureWidget.class));
            //Now update all widgets
        for (int appWidgetId : appWidgetIds) {
            AdventureWidget.updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.d("TAG", "updateWidget: " + context.getPackageName());
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.adventure_widget);
            Intent appIntent =  new Intent(context, JournalActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 , appIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_text_app, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(NAME_PREF);
        editor.remove(ID_PREF);
        editor.apply();
    }
}

