package com.softwaretraining.cddgame.rank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.softwaretraining.cddgame.R;

import java.util.List;

class RankListAdapter extends ArrayAdapter {

    private final int resourceId;

    public RankListAdapter(Context context, int textViewResourceId, List<RankList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankList rankList = (RankList) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView textViewSeqNumber = view.findViewById(R.id.textViewSeqNumber);
        TextView textViewUserName = view.findViewById(R.id.textViewUserName);
        TextView textViewUserScore = view.findViewById(R.id.textViewUserScore);

        textViewSeqNumber.setText(rankList.getSeqNumber());
        textViewUserName.setText(rankList.getUsername());
        textViewUserScore.setText(rankList.getScore());

        return view;
    }

}
