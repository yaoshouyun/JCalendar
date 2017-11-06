package com.jinghanit.jcalendar.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.jinghanit.jcalendar.R;

public class CalendarHolder extends RecyclerView.ViewHolder {

    public TextView mTvDay;

    public CalendarHolder(View itemView) {
        super(itemView);
        mTvDay = (TextView) itemView.findViewById(R.id.tv_day);
    }

}
