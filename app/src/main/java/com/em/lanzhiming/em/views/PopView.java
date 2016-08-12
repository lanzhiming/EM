package com.em.lanzhiming.em.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.em.lanzhiming.em.R;
import com.em.lanzhiming.em.callbacks.CommonCallback;
import com.em.lanzhiming.em.global.AppConstants;
import com.em.lanzhiming.em.utils.ContextUtils;
import com.em.lanzhiming.em.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by libo on 16/7/19.
 */
public class PopView extends Dialog implements View.OnClickListener{

    Context context;

    public final static int POPVIEW_VOTE=200;

    private static final String TAG = "PopView";

    //是否是详情界面弹出框
    private   boolean isDetailPop;
    //所需要传进来的值，（投票数、投票理由）
    private Map<String,String> map;
    //数据传出所需要的handler
    private Handler handler;

    public PopView(Context context) {
        super(context);

        this.context = context;


        init();
    }

    public PopView(Context context, int themeResId,Boolean isDetailPop,Map<String,String> value,Handler handler) {
        super(context, themeResId);
        this.context = context;
        this.map=value;
        this.handler=handler;
        this.isDetailPop=isDetailPop;
        init();
    }




    View view;

    ImageButton closeBtn,btn1,btn2,btn3;

    Button voteBtn;

    EditText voteContent;

    TextView msgContent;

    private void init() {


        view = ContextUtils.inflate(context, R.layout.vote_dialog);
        setContentView(view);

        findViewById();

        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER);

        WindowManager m = this.getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.5
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        this.getWindow().setAttributes(p);

    }

   private void findViewById(){

       closeBtn = (ImageButton) view.findViewById(R.id.close);
       closeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });

       btn1 = (ImageButton) view.findViewById(R.id.btn1);
       btn2 = (ImageButton) view.findViewById(R.id.btn2);
       btn3 = (ImageButton) view.findViewById(R.id.btn3);
       voteBtn = (Button) findViewById(R.id.voteBtn);
       btn1.setOnClickListener(this);
       btn2.setOnClickListener(this);
       btn3.setOnClickListener(this);
       voteBtn.setOnClickListener(this);

       msgContent= (TextView) view.findViewById(R.id.msg_content);

       voteContent=(EditText) view.findViewById(R.id.content);
        //增加对详情页面的处理
       if (isDetailPop){
           closeBtn.setVisibility(View.INVISIBLE);
           voteContent.setVisibility(View.GONE);
           msgContent.setVisibility(View.VISIBLE);
           btn1.setClickable(false);
           btn2.setClickable(false);
           btn3.setClickable(false);
           voteBtn.setText("关闭");
           setLevel(Integer.valueOf(map.get("level").substring(0,map.get("level").indexOf("."))));
           msgContent.setText(map.get("descMsg"));
       }
   }

    private int countLevel(){
        List<ImageButton> buttons = new ArrayList<ImageButton>();
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        int level = 0;
        for (ImageButton tag : buttons) {
            if (tag.isSelected()){
                level ++;
            }
        }
        return level;

    }
    //设置等级
    private void setLevel(int level){
        switch (level){
            case 1:
                btn3.setSelected(true);
                break;
            case 2:
                btn2.setSelected(true);
                btn3.setSelected(true);
                break;
            case 3:
                btn1.setSelected(true);
                btn2.setSelected(true);
                btn3.setSelected(true);
                break;
            default:
                btn3.setSelected(false);
                btn1.setSelected(false);
                btn2.setSelected(false);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        if (v.isSelected()){
            if (v.getId() == R.id.btn1){
                btn1.setSelected(false);
            }else if (v.getId() == R.id.btn2){
                btn2.setSelected(false);
                btn1.setSelected(false);

            }else if (v.getId() == R.id.btn3){
                btn3.setSelected(false);
                btn1.setSelected(false);
                btn2.setSelected(false);

            }


        }else {
            if (v.getId() == R.id.btn1){
                btn1.setSelected(true);
                btn2.setSelected(true);
                btn3.setSelected(true);
            }else if (v.getId() == R.id.btn2){
                btn2.setSelected(true);
                btn3.setSelected(true);
            }else {
                btn3.setSelected(true);
            }



        }

        if (v.getId() == R.id.voteBtn){
            if (isDetailPop){
                dismiss();
            }else{
//                vote
                if (voteContent.getText().toString().trim().equals("")){
                    ToastUtils.makeShortText("请输入投票的理由！",context);
                }else{
                    Log.d(TAG, "------*------PopView recorderId="+map.get("recorderId").substring(0,map.get("recorderId").indexOf(".")));
                    Log.d(TAG, "------*------PopView reveiverId="+map.get("reveiverId").substring(0,map.get("reveiverId").indexOf(".")));
                    Log.d(TAG, "------*------PopView level="+countLevel());
                    Log.d(TAG, "------*------PopView voteContent.getText()="+voteContent.getText().toString());
                    if (voteContent.getText().toString().trim().length() < 5){
                        ToastUtils.makeShortText("理由不得少于五个字！",context);
                    }else {
                        OkHttpUtils
                                .post()//
                                .url(AppConstants.VOTE_URL)
                                .addParams("recorderId", map.get("recorderId").substring(0, map.get("recorderId").indexOf(".")))//
                                .addParams("reveiverId", map.get("reveiverId").substring(0, map.get("reveiverId").indexOf(".")))//
                                .addParams("level", countLevel() + "")
                                .addParams("descMsg", voteContent.getText().toString().trim())
                                .build()//
                                .execute(new CommonCallback() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtils.makeShortText("网络出错！", context);
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> stringObjectMap) {
                                        Message msg = new Message();
                                        msg.what = PopView.POPVIEW_VOTE;
                                        msg.obj = stringObjectMap.get("code");
                                        handler.sendMessage(msg);
                                        dismiss();
                                    }
                                });
                    }
                }
            }
            return;
        }



    }
}
