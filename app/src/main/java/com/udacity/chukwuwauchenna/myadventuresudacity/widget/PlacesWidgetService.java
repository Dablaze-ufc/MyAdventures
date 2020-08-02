package com.udacity.chukwuwauchenna.myadventuresudacity.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.Place;

import java.util.List;

public class PlacesWidgetService  extends RemoteViewsService {
    private List<Place> placeList;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PlaceRemoteViewsFactory(getApplicationContext());
    }

    private class PlaceRemoteViewsFactory implements RemoteViewsFactory {
        private Context mContext;
        PlaceRemoteViewsFactory(Context context){
            mContext = context;
        }


        @Override
        public void onCreate() {
            placeList = AdventureWidget.places;
        }

        @Override
        public void onDataSetChanged() {
            placeList = AdventureWidget.places;

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (placeList == null) return 0;
            return placeList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            Place place = placeList.get(position);

            String placeName = place.getNamePlace();
            String placeTime = place.getTime();
            remoteViews.setTextViewText(R.id.widget_list_item_text, placeTime  + "   " + placeName);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
           return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
