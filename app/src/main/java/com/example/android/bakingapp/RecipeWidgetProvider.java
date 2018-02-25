package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * This si the Widget Provide class to display the Widget on the Home Screen.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i <appWidgetIds.length; i++) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_listview_layout);

        Intent svcIntent = new Intent(context, RecipeWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.listViewWidget, svcIntent);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String savedRecipeName = preferences.getString(MainBakingActivity.RECIPE_NAME, null);
        if (savedRecipeName != null) {
            remoteViews.setTextViewText(R.id.tv_widget_recipe_name, savedRecipeName + " Ingredients");
        } else {
            remoteViews.setTextViewText(R.id.tv_widget_recipe_name, context.getResources().getString(R.string.no_recipe_saved));
        }

        Intent appIntent = new Intent(context, MainBakingActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.tv_widget_recipe_name, appPendingIntent);

        remoteViews.setEmptyView(R.id.listViewWidget, R.id.tv_empty_widget_message);

        return remoteViews;

    }

}
