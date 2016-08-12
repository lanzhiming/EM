package com.em.lanzhiming.em.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.em.lanzhiming.em.R;

import java.util.List;
import java.util.Map;

/**
 * 作者：lanzhm on 2016/7/19 15:10
 * 邮箱：18770915807@163.com
 */
public class ListRecyclerAdapter extends RecyclerView.Adapter{

    private static final String TAG = "ListRecyclerAdapter";

    private List<Map<String,Object>> models;


    public ListRecyclerAdapter(List<Map<String,Object>> models){
        this.models = models;
    }

    public void updateDatas(List<Map<String,Object>> datas, boolean isClear) {
        if (isClear) {
            this.models.clear();
        }
        if (!datas.isEmpty()){
            this.models.addAll(datas);
            notifyDataSetChanged();
        }
    }
//
//    public void clear() {
//        this.models.getData().clear();
//        notifyDataSetChanged();
//    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView vd_createDate,vd_name,vd_recorder,vd_num;

        public ViewHolder(View itemView) {
            super(itemView);

            vd_createDate = (TextView) itemView.findViewById(R.id.vd_createDate);
            vd_name = (TextView) itemView.findViewById(R.id.vd_name);
            vd_recorder = (TextView) itemView.findViewById(R.id.vd_recorder);
            vd_num = (TextView) itemView.findViewById(R.id.vd_num);
        }

        public TextView getVd_createDate() {
            return vd_createDate;
        }

        public TextView getVd_name() {
            return vd_name;
        }

        public TextView getVd_recorder() {
            return vd_recorder;
        }

        public TextView getVd_num() {
            return vd_num;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_vote_detail,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder vh = (ViewHolder) holder;

        vh.getVd_createDate().setText(models.get(position).get("createDate").toString());
        Map<String,String> reveiverMap=(Map<String,String>)models.get(position).get("reveiver");
        Map<String,String> recorderMap=(Map<String,String>)models.get(position).get("recorder");
        vh.getVd_name().setText(reveiverMap.get("name"));
        vh.getVd_recorder().setText(recorderMap.get("name"));
//        因为getIntegral()的返回值为int,所以使用setText()一直报错，需要将int转成String类型。
        int num=(int)Double.parseDouble(String.valueOf(models.get(position).get("level").toString()));
        vh.getVd_num().setText(String.valueOf(num));

        //如果设置了回调，就设置点击事件
        if (mOnItemClickListener != null){
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(vh.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (null!=models){
            return models.size();
        }else {
            return 0;
        }
    }

    /** * ItemClick的回调接口 */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
