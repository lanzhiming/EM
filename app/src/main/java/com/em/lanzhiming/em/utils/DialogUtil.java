package com.em.lanzhiming.em.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.em.lanzhiming.em.R;
import com.em.lanzhiming.em.adapters.UserIdListAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.List;
import java.util.Map;

public class DialogUtil {
    //处理日期选择类型
    public final static int CHOOSE_TYPE=100;
    //处理详情过滤（通过userId）
    public final static int USERID_FLAG=101;

    public static void showCompleteDialog(final Context context, final Handler mHandler,boolean expanded) {
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ListHolder())
                .setHeader(R.layout.dialog_header)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setContentBackgroundResource(R.color.dialog_content_background)
                .setAdapter(new ArrayAdapter<>(context, R.layout.dialog_simple_list_item,
                        new String[]{"本周","本月","本季度","本年"}))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Message msg=new Message();
                        msg.what=CHOOSE_TYPE;
                        msg.obj=position;
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                    }
                })
                .setExpanded(expanded)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .create();
        dialog.show();
    }
    public static void showUserListDialog(final Context context, final Handler mHandler,boolean expanded,List<Map<String, String>> datas) {
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ListHolder())
                .setCancelable(true)
                .setGravity(Gravity.TOP)
                .setContentBackgroundResource(R.color.dialog_content_background)
                .setAdapter(new UserIdListAdapter(context,datas))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Object list=((ListView)dialog.getHolderView()).getAdapter().getItem(position);
                        Map<String,String> dataMap=(Map<String,String>)list;
                        Message msg=new Message();
                        msg.what=USERID_FLAG;
                        msg.obj=dataMap;
//                        if (dataMap.get("id")!=null){
//                            msg.obj=String.valueOf(dataMap.get("id")).substring(0,String.valueOf(dataMap.get("id")).indexOf("."));
//                        }else{
//                            msg.obj=null;
//                        }
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                    }
                })
                .setExpanded(expanded)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .create();
        dialog.show();
    }


}
