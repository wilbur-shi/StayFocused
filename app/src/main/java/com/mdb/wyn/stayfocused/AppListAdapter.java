package com.mdb.wyn.stayfocused;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

    /**
     * Created by Young on 4/2/2016.
     */
    public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.CustomViewHolder>{
        private Context context;
        public static ArrayList<AppListItem> appListArray;

        public AppListAdapter(Context context, ArrayList<AppListItem> appListItems) {
            this.context = context;
            appListArray = appListItems;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applist_row_view, parent, false);
            return new CustomViewHolder(view);
        }


        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            final AppListItem appListItem = appListArray.get(position);
            holder.appNameTextView.setText(appListItem.appName);
            holder.isBlacklistCheckBox.setChecked(appListItem.isBlacklisted);
            holder.isBlacklistCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    appListItem.isBlacklisted = isChecked;
//                    System.out.println("on check change listener");
//                    notifyDataSetChanged();
                    //result of changing the checkbox
                    //dunno if need to notify datasetchanged
                }
            });

        }

        @Override
        public int getItemCount() {
            return appListArray.size();
        }

        public ArrayList<AppListItem> getAppListArray() {
            return appListArray;
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView appNameTextView;
            CheckBox isBlacklistCheckBox;

            public CustomViewHolder (View view) {
                super(view);
                this.appNameTextView = (TextView) view.findViewById(R.id.appNameTextView);
                this.isBlacklistCheckBox= (CheckBox) view.findViewById(R.id.isBlacklistCheckBox);

            }
        }
    }

