package com.mdb.wyn.stayfocused;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

    /**
     * Created by Young on 4/2/2016.
     */
    public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.CustomViewHolder>{
        private Context context;
        private ArrayList<BlackListItem> blackListArray;

        public BlackListAdapter(Context context, ArrayList<BlackListItem> blackListItems) {
            this.context = context;
            blackListArray = blackListItems;
            System.out.println("created a blacklist adapter");
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
            return new CustomViewHolder(view);
        }


        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            BlackListItem blackListItem = blackListArray.get(position);
          //  Log.i("ArrayList Testing", "Size is " + blackListArray.size());
            holder.appNameTextView.setText(blackListItem.appName);
            holder.isBlacklistCheckBox.setChecked(false);
         //   System.out.println("into bind view holder method");

        }

        @Override
        public int getItemCount() {
            Log.i("ArrayList", blackListArray.size() + "");
            return blackListArray.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView appNameTextView;
            CheckBox isBlacklistCheckBox;

            public CustomViewHolder (View view) {
                super(view);
                this.appNameTextView = (TextView) view.findViewById(R.id.appNameTextView);
                this.isBlacklistCheckBox= (CheckBox) view.findViewById(R.id.isBlacklistCheckBox);


//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int row= getAdapterPosition();
//                        blackListArray.get(row).isBlacklisted= ! blackListArray.get(row).isBlacklisted;
//
//
//                    }
//                });

            }
        }
    }

