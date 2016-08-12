package com.em.lanzhiming.em.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * 作者：lanzhm on 2016/7/18 19:08
 * 邮箱：18770915807@163.com
 */
public class NetUtil {
    private static final String TAG = "NetUtil";

    // HttpGet方式请求
//    public static String requestByHttpGet(String path) throws Exception {
////        String path = "https://reg.163.com/logins.jsp?id=helloworld&pwd=android";
//        // 新建HttpGet对象
//        HttpGet httpGet = new HttpGet(path);
//        // 获取HttpClient对象
//        HttpClient httpClient = new DefaultHttpClient();
//        // 获取HttpResponse实例
//        HttpResponse httpResp = httpClient.execute(httpGet);
//        // 判断是够请求成功
//        String result = null;
//        if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//            // 获取返回的数据
//            result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
//            Log.i(TAG, "HttpGet方式请求成功，返回数据如下：");
//            Log.i(TAG, result);
//        } else {
//            Log.i(TAG, "HttpGet方式请求失败");
//        }
//        return result;
//    }

    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        Log.e(TAG, "the net is ok");
                        return true;
                    }
                }
            }
        }
        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 网络可用状态下，通过get方式向server端发送请求，并返回响应数据
     *
     * @param strUrl 请求网址
     * @param context 上下文
     * @return 响应数据
     */
//    public static JSONObject getResponseForGet(String strUrl, Context context) {
//        if (isConnnected(context)) {
//            return getResponseForGet(strUrl);
//        }
//        return null;
//    }

    /**
     * 通过Get方式处理请求，并返回相应数据
     *
     * @param strUrl 请求网址
     * @return 响应的JSON数据
     */
//    public static JSONObject getResponseForGet(String strUrl) {
//        HttpGet httpRequest = new HttpGet(strUrl);
//        return getRespose(httpRequest);
//    }

    /**
     * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据
     *
     * @param market_uri 请求网址
     * @param nameValuePairs 参数信息
     * @param context 上下文
     * @return 响应数据
     */
//    public static JSONObject getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs, Context context) {
//        if (isConnnected(context)) {
//            return getResponseForPost(market_uri, nameValuePairs);
//        }
//        return null;
//    }

    /**
     * 通过post方式向服务器发送请求，并返回响应数据
     *
     * @param  strUrl 请求网址
     * @param nameValuePairs 参数信息
     * @return 响应数据
     */
//    public static JSONObject getResponseForPost(String market_uri, List<NameValuePair> nameValuePairs) {
//        if (null == market_uri || "" == market_uri) {
//            return null;
//        }
//        HttpPost request = new HttpPost(market_uri);
//        try {
//            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            return getRespose(request);
//        } catch (UnsupportedEncodingException e1) {
//            e1.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 响应客户端请求
     *
     * @param request 客户端请求get/post
     * @return 响应数据
     */
//    public static JSONObject getRespose(HttpUriRequest request) {
//        try {
//            HttpResponse httpResponse = new DefaultHttpClient().execute(request);
//            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            if (HttpStatus.SC_OK == statusCode) {
//                String result = EntityUtils.toString(httpResponse.getEntity());
//                Log.i(TAG, "results=" + result);
//                return new JSONObject(result);
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
