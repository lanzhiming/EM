package com.em.lanzhiming.em.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.em.lanzhiming.em.R;
import com.em.lanzhiming.em.utils.ImageLoadProxy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;
import java.util.Map;

/**
 * 作者：lanzhm on 2016/7/19 15:10
 * 邮箱：18770915807@163.com
 */
public class GridRecyclerAdapter extends RecyclerView.Adapter{

    private static final String TAG = "GridRecyclerAdapter";

    private List<Map<String,String>> models;

    private DisplayImageOptions options;


    public GridRecyclerAdapter(List<Map<String,String>> models){
        this.models = models;
        options=ImageLoadProxy.getOptions4PictureList(R.drawable.icon_gray);
    }

    public void updateDatas(List<Map<String,String>> datas, boolean isClear) {
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

        private ImageView picture;
        private TextView userName,job,num;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = (ImageView) itemView.findViewById(R.id.item_picture);
            userName = (TextView) itemView.findViewById(R.id.item_username);
            job = (TextView) itemView.findViewById(R.id.item_job);
            num = (TextView) itemView.findViewById(R.id.item_num);
        }

        public ImageView getPicture(){
            return picture;
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getJob() {
            return job;
        }

        public TextView getNum() {
            return num;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_main,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder vh = (ViewHolder) holder;
        //TODO 增加对imageloader的优化处理
        ImageLoadProxy.displayImage(models.get(position).get("imgPath"),vh.getPicture(),options);
//        ImageLoader.getInstance().displayImage(models.get(position).get("imgPath"),vh.getPicture());
        vh.getUserName().setText(models.get(position).get("name"));
        vh.getJob().setText(models.get(position).get("position"));
//        因为getIntegral()的返回值为int,所以使用setText()一直报错，需要将int转成String类型。
        int num=(int)Double.parseDouble(String.valueOf(models.get(position).get("integral")));
        vh.getNum().setText(String.valueOf(num));

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
