package com.jinghanit.jcalendar.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinghanit.jcalendar.CalendarModel;
import com.jinghanit.jcalendar.CalendarView;
import com.jinghanit.jcalendar.R;
import com.jinghanit.jcalendar.adapter.holder.CalendarHolder;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarHolder> {

    private CalendarView calendarView;
    private List<CalendarModel> list = new ArrayList<>();

    public CalendarAdapter(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public void updateList(List<CalendarModel> dayList) {
        list.clear();
        list.addAll(dayList);
        notifyDataSetChanged();
    }

    @Override
    public CalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CalendarHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.jcalendar_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(CalendarHolder holder, int position) {
        final CalendarModel model = list.get(position);
        if (model.isClick) {
            if (model.isSelect) {
                holder.mTvDay.setBackgroundResource(R.drawable.jcalendar_day_bg);
                holder.mTvDay.setTextColor(0xff3db2a0);
            } else {
                holder.mTvDay.setBackgroundColor(Color.TRANSPARENT);
                holder.mTvDay.setTextColor(0xff313133);
            }
        } else {
            holder.mTvDay.setBackgroundColor(Color.TRANSPARENT);
            holder.mTvDay.setTextColor(0xffb4b4b4);
        }
        holder.mTvDay.setText(model.day > 0 ? "" + model.day : "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.isClick && !model.isSelect) {
                    calendarView.setDay(model.day);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
