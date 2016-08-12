package com.em.lanzhiming.em.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.em.lanzhiming.em.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lanzhm on 2016/7/22 21:33
 * 邮箱：18770915807@163.com
 */
public class UserIdListAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> idItems=new ArrayList<Map<String, String>>();


    public UserIdListAdapter(){

    }

    public UserIdListAdapter(Context context, List<Map<String, String>> idItems) {
        this.context = context;
        Map<String, String> all=new HashMap<String, String>();
        all.put("id",null);
        all.put("name","全部");
        this.idItems.add(all);
        if (!idItems.isEmpty()){
            for (Map<String, String> map:idItems) {
                this.idItems.add(map);
            }
        }
    }

    private class ViewHolder {
        TextView txtName;
    }

    @Override
    public int getCount() {
        return idItems.size();
    }

    @Override
    public Object getItem(int position) {
        return idItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.item_user_id_list, null);
        if (convertView != null) {
            TextView itemUserName = (TextView) convertView
                    .findViewById(R.id.item_user_name);
            TextView itemUserId = (TextView) convertView
                    .findViewById(R.id.item_user_id);
            itemUserName.setText(idItems.get(position).get("name").toString());
            itemUserId.setText(String.valueOf(idItems.get(position).get("id")));
        }
        return convertView;
    }
}
