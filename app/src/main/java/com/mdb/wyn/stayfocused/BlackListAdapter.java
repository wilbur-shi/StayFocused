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
 * Created by Wilbur on 4/22/2016.
 */
public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.CustomViewHolder>{
    private Context context;
    public ArrayList<AppListItem> blackListArray;

    public BlackListAdapter(Context context, ArrayList<AppListItem> blackListItems) {
        this.context = context;
        blackListArray = blackListItems;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applist_row_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        AppListItem appListItem = blackListArray.get(position);
        holder.appNameTextView.setText(appListItem.appName);
        holder.isBlacklistCheckBox.setChecked(false);
        holder.isBlacklistCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //result of changing the checkbox
                //dunno if need to notify datasetchanged
            }
        });
    }

    @Override
    public int getItemCount() {
        return blackListArray.size();
    }

    public AppListItem getItem(int position) {
        return blackListArray.get(position);
    }

    public void addAppListItem(AppListItem item) {
        blackListArray.add(item);
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

