package com.em.lanzhiming.em.callbacks;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * 作者：lanzhm on 2016/7/19 10:37
 * 邮箱：18770915807@163.com
 */
public abstract class CommonCallback extends Callback<Map<String,Object>>
{
    @Override
    public Map<String,Object> parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        Map<String,Object> resultMaps = new Gson().fromJson(string, Map.class);
        return resultMaps;
    }


}
