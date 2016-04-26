package com.mdb.wyn.stayfocused;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
//            final CustomViewHolder viewHolder = holder;
            holder.appNameTextView.setText(appListItem.appName);
            holder.appIcon.setImageDrawable(appListItem.icon);
            holder.isBlacklistCheckBox.setOnCheckedChangeListener(null);
            holder.isBlacklistCheckBox.setChecked(appListItem.isBlacklisted);
            holder.isBlacklistCheckBox.setOnCheckedChangeListener(new CheckBoxChangeListener(position));

        }

        private class CheckBoxChangeListener implements CompoundButton.OnCheckedChangeListener {
            private int position;

            public CheckBoxChangeListener(int pos) {
                position = pos;
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appListArray.get(position).isBlacklisted = isChecked;
                notifyDataSetChanged();
            }

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
            ImageView appIcon;

            public CustomViewHolder (View view) {
                super(view);
                this.appNameTextView = (TextView) view.findViewById(R.id.appNameTextView);
                this.isBlacklistCheckBox= (CheckBox) view.findViewById(R.id.isBlacklistCheckBox);
                this.appIcon = (ImageView) view.findViewById(R.id.iconImg);

            }
        }
    }

