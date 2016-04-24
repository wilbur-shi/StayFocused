package com.mdb.wyn.stayfocused;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blacklist_row_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final AppListItem appListItem = blackListArray.get(position);
        holder.blackListAppNameTextView.setText(appListItem.appName);
        holder.isActivated.setChecked(appListItem.isBlacklisted);
        holder.isActivated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appListItem.isBlacklisted = isChecked;
                MainActivity.customPrefs.changeChecked(appListItem.appName, appListItem.isBlacklisted);
//                notifyDataSetChanged();
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
        if (!blackListArray.contains(item)) {
            blackListArray.add(item);
        }
    }

    public void setBlackListArray(ArrayList<AppListItem> array) {
        blackListArray = array;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView blackListAppNameTextView;
        Switch isActivated;

        public CustomViewHolder (View view) {
            super(view);
            blackListAppNameTextView = (TextView) view.findViewById(R.id.blackListAppNameTextView);
            isActivated= (Switch) view.findViewById(R.id.activationSwitch);
        }
    }
}

