package com.em.lanzhiming.em.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.em.lanzhiming.em.R;
import com.em.lanzhiming.em.adapters.ListRecyclerAdapter;
import com.em.lanzhiming.em.callbacks.CommonCallback;
import com.em.lanzhiming.em.effects.SwipeBackController;
import com.em.lanzhiming.em.global.AppConstants;
import com.em.lanzhiming.em.model.SerializableListMap;
import com.em.lanzhiming.em.utils.DateUtil;
import com.em.lanzhiming.em.utils.DialogUtil;
import com.em.lanzhiming.em.utils.ToastUtils;
import com.em.lanzhiming.em.views.PopView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class VoteDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{

    private static final String TAG = "VoteDetailActivity";

    private SwipeBackController swipeBackController;

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;

    private List<Map<String,Object>> mUserList = new ArrayList<Map<String,Object>>();
    private ListRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int lastVisibleItem=0;
    private SimpleDateFormat dateFormater=new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");

    private SerializableListMap listMap;
    private String[] userIds;

    //选择全部
    private boolean allFlag=true;
    //用户id
    private String userId;
    private String startTime;
    private String endTime;

    private ImageButton voteBack;
    private Button voteSelect;
    private Spinner voteSpinner;

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogUtil.USERID_FLAG: {
                    Map<String,String> dataMap=(Map<String,String>)msg.obj;
                    if (dataMap.get("id")!=null){
                        userId= String.valueOf(dataMap.get("id")).substring(0,String.valueOf(dataMap.get("id")).indexOf("."));
                        voteSelect.setText(String.valueOf(dataMap.get("name")));
                        allFlag=false;
                    }else{
                        userId=null;
                        voteSelect.setText(String.valueOf(dataMap.get("name")));
                        allFlag=true;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);

        swipeBackController = new SwipeBackController(this);

        initViews();
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.list_swipeRefreshLayout);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.c1, R.color.c2,
                R.color.c3);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recyclerview);
//        线性布局
        mRecyclerView.setLayoutManager(mLayoutManager);

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));

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


        mSwipeRefreshWidget.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshWidget.setRefreshing(true);
            }
        });

        loadData();
    }

    private void setAdapter(){
        if(mAdapter==null){
            mAdapter = new ListRecyclerAdapter(mUserList);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.updateDatas(mUserList,true);
        }

        mAdapter.setmOnItemClickListener(new ListRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtils.makeShortText("You clicked "+(position+1)+" item",MainActivity.this);
                Map<String,String> datas=new HashMap<String, String>();
                datas.put("level", String.valueOf(mUserList.get(position).get("level")));
                datas.put("descMsg", String.valueOf(mUserList.get(position).get("descMsg")));
                Log.d(TAG, "------*------VoteDetailActivity level="+String.valueOf(mUserList.get(position).get("level")));
                Log.d(TAG, "------*------VoteDetailActivity descMsg="+String.valueOf(mUserList.get(position).get("descMsg")));
                PopView pp = new PopView(VoteDetailActivity.this,R.style.pop_window_dialog,true,datas,handler);
                pp.show();
            }
        });
    }

    protected void initViews()
    {
        voteBack=(ImageButton) findViewById(R.id.vote_back);
        voteSelect=(Button) findViewById(R.id.vote_select);
//        voteSpinner= (Spinner) findViewById(R.id.vote_spinner);

        voteBack.setOnClickListener(this);
        voteSelect.setOnClickListener(this);
    }

    private void loadData() {
        Intent data = getIntent();
        listMap = (SerializableListMap) data.getSerializableExtra("mUserListMap");
        List<Map<String,String>> userListMap = listMap.getListMap();
        startTime=listMap.getStartTime()==null?dateFormater.format(DateUtil.getCurrentYearStartTime()):listMap.getStartTime();
        endTime=listMap.getEndTime()==null?dateFormater.format(DateUtil.getCurrentYearEndTime()):listMap.getEndTime();
//        userIds = new String[userListMap.size()];
//        for(int i=0;i<userIds.length;i++){
//            userIds[i]=String.valueOf(userListMap.get(i).get("id"));
//        }
        if (allFlag){
            userId=null;
            //先求出userListMap中name的值，然后塞入Adapter
            //listMap.getListMap().get(0).get("name") ,size=13
            okHttpGetData(userId,startTime,endTime,AppConstants.ALL_VOTELIST_URL);
        }else{
            if (null!=userId){
                okHttpGetData(userId,startTime,endTime,AppConstants.VOTELIST_URL);
            }else{
                ToastUtils.makeShortText("加载失败！",VoteDetailActivity.this);
            }
        }

    }

    private void okHttpGetData(String userId,String startTime,String endTime,String url){
        OkHttpUtils
                .get()//
                .addParams("userId",userId)
                .addParams("startTime",startTime)
                .addParams("endTime",endTime)
                .url(url)//
                .build()//
                .execute(new CommonCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG, "------*------onError：error="+e.getMessage());
                        ToastUtils.makeShortText("网络连接出错，请检查网络连接！",VoteDetailActivity.this);
                        mSwipeRefreshWidget.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(Map<String, Object> stringObjectMap) {
                        mUserList = (ArrayList<Map<String,Object>>)stringObjectMap.get("data");
                        setAdapter();
                        mSwipeRefreshWidget.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vote_back:
//                ToastUtils.makeShortText("You clicked vote_back item",VoteDetailActivity.this);
                VoteDetailActivity.this.finish();
                break;
            case R.id.vote_select:
                DialogUtil.showUserListDialog(VoteDetailActivity.this,handler,true,listMap.getListMap());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
        Toast.makeText(this,"最新数据更新完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (swipeBackController.processEvent(ev)) {
            return true;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
