package com.example.android.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * This is the widget Service class used to extend the remote view service
 */

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new RecipeListProvider(this.getApplicationContext(), intent));
    }
}
