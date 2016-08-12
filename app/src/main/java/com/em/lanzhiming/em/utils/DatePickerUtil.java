package com.em.lanzhiming.em.utils;

import android.support.v4.app.FragmentActivity;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.util.Date;

/**
 * 作者：lanzhm on 2016/7/21 11:22
 * 邮箱：18770915807@163.com
 */
public class DatePickerUtil {
    public static void  showDataPicker(FragmentActivity context, String title, SlideDateTimeListener listener){

        new SlideDateTimePicker.Builder(context.getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .setTitle(title)
                .build().show();
    }
}
