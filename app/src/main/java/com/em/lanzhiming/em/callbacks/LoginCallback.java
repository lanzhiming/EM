package com.em.lanzhiming.em.callbacks;

import com.em.lanzhiming.em.model.DTO;
import com.em.lanzhiming.em.model.User;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 作者：lanzhm on 2016/7/19 10:37
 * 邮箱：18770915807@163.com
 */
public abstract class LoginCallback extends Callback<DTO<User>>
{
    @Override
    public DTO<User> parseNetworkResponse(Response response) throws IOException
    {
        String string = response.body().string();
        DTO<User> user = new Gson().fromJson(string, DTO.class);
        return user;
    }

}

