package com.jinghanit.jcalendar;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinghanit.jcalendar.adapter.CalendarAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarView extends LinearLayout implements View.OnClickListener {

    private TextView mTvYear;//年
    private TextView mTvMonth;//月
    private RecyclerView mRvDays;//日
    private RelativeLayout mRlYearRight;//下一年
    private RelativeLayout mRlMonthRight;//下个月
    private ImageView mIvYearRight;//下一年
    private ImageView mIvMonthRight;//下个月

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int selectYear;
    private int selectMonth;
    private int selectDay;
    private Calendar selectCalendar;//当前的日历
    private CalendarAdapter calendarAdapter;
    private List<CalendarModel> dayList;
    private boolean isUpdate;//是否是外部控件触发

    public Calendar getSelectCalendar() {
        return selectCalendar;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.jcalendar_view, this);
        mTvYear = (TextView) findViewById(R.id.tv_year);
        mTvMonth = (TextView) findViewById(R.id.tv_month);
        mRvDays = (RecyclerView) findViewById(R.id.rv_days);
        mRlYearRight = (RelativeLayout) findViewById(R.id.rl_year_right);
        mRlMonthRight = (RelativeLayout) findViewById(R.id.rl_month_right);
        mIvYearRight = (ImageView) findViewById(R.id.iv_year_right);
        mIvMonthRight = (ImageView) findViewById(R.id.iv_month_right);
        mRlYearRight.setOnClickListener(this);
        mRlMonthRight.setOnClickListener(this);
        mIvYearRight.setOnClickListener(this);
        mIvMonthRight.setOnClickListener(this);
        initUiAndListener();
    }

    public void initUiAndListener() {
        mRvDays.setLayoutManager(new GridLayoutManager(getContext(), 7));
        calendarAdapter = new CalendarAdapter(this);
        mRvDays.setAdapter(calendarAdapter);
        selectCalendar = Calendar.getInstance();
        currentYear = selectCalendar.get(Calendar.YEAR);
        currentMonth = selectCalendar.get(Calendar.MONTH);
        currentDay = selectCalendar.get(Calendar.DAY_OF_MONTH);
        dayList = new ArrayList<>();//显示的日
        isUpdate = false;
        loadData();
    }

    /**
     * 加载日期
     */
    private void loadData() {
        if (isUpdateDate()) {
            updateYear();
            updateMonth();
            addEmptyDay();
            addDay();
            if (!isUpdate && onCalendarChangeListener != null) {
                onCalendarChangeListener.onChange(getSelectYear(), getSelectMonth(), getSelectDay());
            }
        }
    }

    /**
     * 是否修改日期
     */
    private boolean isUpdateDate() {
        int newYear = selectCalendar.get(Calendar.YEAR);
        int newMonth = selectCalendar.get(Calendar.MONTH);
        int newDay = selectCalendar.get(Calendar.DAY_OF_MONTH);
        if (selectYear != newYear || selectMonth != newMonth || selectDay != newDay) {
//            Logger.d(hashCode() + "=onDateChangeListener=" + newYear + "-" + newMonth + "-" + newDay);
            return true;
        }
        return false;
    }

    /**
     * 更新年
     */
    private void updateYear() {
        selectYear = selectCalendar.get(Calendar.YEAR);
        mTvYear.setText(selectYear + "");
        if (selectYear < currentYear) {
            mRlYearRight.setClickable(true);
            mIvYearRight.setImageResource(R.mipmap.jcalendar_arrow_right);
        } else {
            mRlYearRight.setClickable(false);
            mIvYearRight.setImageResource(R.mipmap.jcalendar_right_disable);
        }
    }

    /**
     * 更新月
     */
    private void updateMonth() {
        selectMonth = selectCalendar.get(Calendar.MONTH);
        mTvMonth.setText(selectMonth + 1 + "月");
        if (selectYear != currentYear) {
            mRlMonthRight.setClickable(true);
            mIvMonthRight.setImageResource(R.mipmap.jcalendar_arrow_right);
        } else {
            if (selectMonth < currentMonth) {
                mRlMonthRight.setClickable(true);
                mIvMonthRight.setImageResource(R.mipmap.jcalendar_arrow_right);
            } else {
                mRlMonthRight.setClickable(false);
                mIvMonthRight.setImageResource(R.mipmap.jcalendar_right_disable);
            }
        }
    }

    /**
     * 添加空白日期
     */
    private void addEmptyDay() {
        dayList.clear();
        Calendar emptyCalendar = Calendar.getInstance();
        emptyCalendar.set(selectYear, selectMonth, 1);
        int day = emptyCalendar.get(Calendar.DAY_OF_WEEK);
        if (day != 1) {//如果不是星期天
            for (int i = 1; i < day; i++) {
                dayList.add(new CalendarModel(0, false, false));
            }
        }
    }

    /**
     * 添加日期
     */
    private void addDay() {
        selectDay = selectCalendar.get(Calendar.DAY_OF_MONTH);//默认选中今天
        boolean isCurrentMonth = selectYear == currentYear && selectMonth == currentMonth; //是否是当前月
        if (isCurrentMonth && selectDay > currentDay) {
            selectDay = currentDay;
        }
        for (int i = 1; i <= selectCalendar.getActualMaximum(Calendar.DATE); i++) {
            if (isCurrentMonth) {
                if (i > currentDay) {
                    dayList.add(new CalendarModel(i, false, false));
                } else {
                    dayList.add(new CalendarModel(i, i == selectDay ? true : false, true));
                }
            } else {
                dayList.add(new CalendarModel(i, i == selectDay ? true : false, true));
            }
        }
        calendarAdapter.updateList(dayList);
    }



    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.rl_year_left) {
            isUpdate = false;
            selectCalendar.add(Calendar.YEAR, -1);
            loadData();
        } else if (i == R.id.rl_year_right) {
            isUpdate = false;
            selectCalendar.add(Calendar.YEAR, 1);
            if (selectCalendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                selectCalendar.set(currentYear, currentMonth, currentDay);
            }
            loadData();
        } else if (i == R.id.rl_month_left) {
            isUpdate = false;
            selectCalendar.add(Calendar.MONTH, -1);
            loadData();
        } else if (i == R.id.rl_month_right) {
            isUpdate = false;
            selectCalendar.add(Calendar.MONTH, 1);
            loadData();
        }
    }

    private OnCalendarChangeListener onCalendarChangeListener;

    public interface OnCalendarChangeListener {

        void onChange(int year, int month, int day);

    }


    public void setOnDateChangeListener(OnCalendarChangeListener onCalendarChangeListener) {
        this.onCalendarChangeListener = onCalendarChangeListener;
    }

    public long getTimeInMillis() {
        return selectCalendar.getTimeInMillis();
    }

    public int getSelectYear() {
        return selectYear;
    }

    public int getSelectMonth() {
        return selectMonth;
    }

    public int getSelectDay() {
        return selectDay;
    }

    /**
     * 设置日
     */
    public void setDay(int day) {
        isUpdate = false;
        selectCalendar.set(Calendar.DAY_OF_MONTH, day);
        loadData();
    }

    /**
     * 设置日期，主动设置的日期值不该有错，所以不调用日期改变事件，防止日历联动时死循环
     */
    public void setDate(int year, int month, int day) {
        isUpdate = true;
        selectCalendar.set(Calendar.YEAR, year);
        selectCalendar.set(Calendar.MONTH, month);
        selectCalendar.set(Calendar.DAY_OF_MONTH, day);
        loadData();
    }

}
