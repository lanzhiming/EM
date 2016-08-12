package com.em.lanzhiming.em;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.em.lanzhiming.em.activities.VoteDetailActivity;
import com.em.lanzhiming.em.adapters.GridRecyclerAdapter;
import com.em.lanzhiming.em.callbacks.CommonCallback;
import com.em.lanzhiming.em.global.AppConstants;
import com.em.lanzhiming.em.model.SerializableListMap;
import com.em.lanzhiming.em.utils.DatePickerUtil;
import com.em.lanzhiming.em.utils.DateUtil;
import com.em.lanzhiming.em.utils.DialogUtil;
import com.em.lanzhiming.em.utils.SpUtils;
import com.em.lanzhiming.em.utils.ToastUtils;
import com.em.lanzhiming.em.views.DividerGridItemDecoration;
import com.em.lanzhiming.em.views.PopView;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {

    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private List<Map<String,String>> mUserList = new ArrayList<Map<String,String>>();
    private GridRecyclerAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private int lastVisibleItem=0;
    private SimpleDateFormat dateFormater=new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");

    private ImageButton personPic;
    private Button detailInfo;
    private SegmentControl mSegmentHorzontal;

    private String startTime;
    private String endTime;


    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogUtil.CHOOSE_TYPE: {
//                    ToastUtils.makeShortText("从日期对话框传来的值"+msg.obj,MainActivity.this);
                    switch (Integer.valueOf(msg.obj.toString())) {
                        case 0: {
                            startTime=dateFormater.format(DateUtil.getCurrentWeekDayStartTime());
                            endTime=dateFormater.format(DateUtil.getCurrentWeekDayEndTime());
                            break;
                        }
                        case 1: {
                            startTime=dateFormater.format(DateUtil.getCurrentMonthStartTime());
                            endTime=dateFormater.format(DateUtil.getCurrentMonthEndTime());
                            break;
                        }
                        case 2: {
                            startTime=dateFormater.format(DateUtil.getCurrentQuarterStartTime());
                            endTime=dateFormater.format(DateUtil.getCurrentQuarterEndTime());
                            break;
                        }
                        case 3: {
                            startTime=dateFormater.format(DateUtil.getCurrentYearStartTime());
                            endTime=dateFormater.format(DateUtil.getCurrentYearEndTime());
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case PopView.POPVIEW_VOTE:{
                    if (String.valueOf(msg.obj).equals("0")){
                        ToastUtils.makeShortText("投票成功！",MainActivity.this);
                    }else{
                        ToastUtils.makeShortText("投票出错！",MainActivity.this);
                    }
                    loadData();
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.c1, R.color.c2,
                R.color.c3);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mLayoutManager = new GridLayoutManager(this,3);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
//        线性布局
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        网格布局
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter = new GridRecyclerAdapter(mDatas));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this,10));

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
//                    mSwipeRefreshWidget.setRefreshing(true);
                    //   此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                    loadData();
//                    Toast.makeText(MainActivity.this,"已经是最新数据",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });


//        mSwipeRefreshWidget.post(new Runnable() {
//
//            @Override
//            public void run() {
//                mSwipeRefreshWidget.setRefreshing(true);
//            }
//        });
        loadData();

        mSegmentHorzontal = (SegmentControl) findViewById(R.id.segment_control);
        mSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                Log.i(TAG, "onSegmentControlClick: index = " + index);
                switch (index){
                    case 0:
//                        ToastUtils.makeShortText("You clicked person_picture item",MainActivity.this);
                        DialogUtil.showCompleteDialog(MainActivity.this,handler,false);
                        break;
                    case 1:
//                        ToastUtils.makeShortText("You clicked 范围 item",MainActivity.this);
                        DatePickerUtil.showDataPicker(MainActivity.this,"开始日期",startTimeListener);

                        break;
                    default:
                        break;
                }

            }
        });

    }

    private void setAdapter(){
        if(mAdapter==null){
            mAdapter = new GridRecyclerAdapter(mUserList);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.updateDatas(mUserList,true);
        }

        mAdapter.setmOnItemClickListener(new GridRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtils.makeShortText("You clicked "+(position+1)+" item",MainActivity.this);
                Map<String,String> datas=new HashMap<String, String>();
                String recorderId=SpUtils.getString(MainActivity.this,AppConstants.RECORDER_ID);
                String reveiverId=String.valueOf(mUserList.get(position).get("id"));
                if (recorderId.equals(reveiverId)){
                    ToastUtils.makeShortText("你不能给自己投钻！",MainActivity.this);
                    return;
                }
                datas.put("recorderId",recorderId );
                datas.put("reveiverId", reveiverId);
                Log.d(TAG, "------*------MainActivity recorderId="+SpUtils.getString(MainActivity.this,AppConstants.RECORDER_ID));
                Log.d(TAG, "------*------MainActivity reveiverId="+String.valueOf(mUserList.get(position).get("id")));
                PopView pp = new PopView(MainActivity.this,R.style.pop_window_dialog,false,datas,handler);
                pp.show();
            }
        });
    }

    protected void initViews()
    {
        personPic=(ImageButton) findViewById(R.id.person_picture);
        detailInfo=(Button) findViewById(R.id.detail_info);

        personPic.setOnClickListener(this);
        detailInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_picture:
//                Dialog alertDialog = new AlertDialog.Builder(this).
//                        setTitle("注销").
//                        setMessage("您确定注销当前账号吗？").
//                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                SpUtils.putBoolean(MainActivity.this, AppConstants.REMEMBER, false);
//                                SpUtils.putString(MainActivity.this, AppConstants.ACCOUNT, "");
//                                SpUtils.putString(MainActivity.this, AppConstants.PASSWORD, "");
//                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                                finish();
//                            }
//                        }).
//                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).
//                        create();
//                alertDialog.show();
                break;
            case R.id.detail_info:
//                ToastUtils.makeShortText("You clicked detail_info item",MainActivity.this);
                Intent mIntent = new Intent(this, VoteDetailActivity.class);
                SerializableListMap listMap= new SerializableListMap();
                listMap.setStartTime(startTime);
                listMap.setEndTime(endTime);
                listMap.setListMap(mUserList);
                mIntent.putExtra("mUserListMap",listMap);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    protected void loadData() {
        if (null==startTime||null==endTime){
            startTime=dateFormater.format(DateUtil.getCurrentYearStartTime());
            endTime=dateFormater.format(DateUtil.getCurrentYearEndTime());
        }
        OkHttpUtils
                .get()//
                .addParams("startTime",startTime)
                .addParams("endTime",endTime)
                .url(AppConstants.INDEX_LIST_URL)//
                .build()//
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, "------*------onError：error="+e.getMessage());
                        ToastUtils.makeShortText("网络连接出错，请检查网络连接！",MainActivity.this);
                        mSwipeRefreshWidget.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(Map<String, Object> stringObjectMap) {
                        mUserList = (ArrayList<Map<String,String>>)stringObjectMap.get("data");
                        setAdapter();
                        mSwipeRefreshWidget.setRefreshing(false);
                    }
                });
    }

    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
//            ToastUtils.makeShortText(dateFormater.format(date),MainActivity.this);
            startTime=dateFormater.format(date);
            DatePickerUtil.showDataPicker(MainActivity.this,"结束日期",endTimeListener);
        }

        @Override
        public void onDateTimeCancel() {
            startTime=null;
        }
    };
    private SlideDateTimeListener endTimeListener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
//            ToastUtils.makeShortText(dateFormater.format(date),MainActivity.this);
            endTime=dateFormater.format(date);
            loadData();
        }

        @Override
        public void onDateTimeCancel() {
            startTime=null;
            endTime=null;
        }
    };

    @Override
    public void onRefresh() {
        loadData();
        Toast.makeText(this,"最新数据更新完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }
}
