package org.niu.steam.tasks;

import okhttp3.*;

import java.io.IOException;
import java.util.Set;

public class Transfer extends Task {

    private boolean isHelper = false;

    /**
     * POST https://steamcommunity.com/login/transfer
     * POST https://help.steampowered.com/login/transfer
     * @param url
     * @param isHelper
     */
    public Transfer(String url,boolean isHelper) {
        super(url);
        this.isHelper = isHelper;
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder .addHeader("Connection","keep-alive")
                .addHeader("Cache-Control","max-age=0")
                .addHeader("Origin","https://store.steampowered.com")
                .addHeader("Upgrade-Insecure-Requests","1")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Sec-Fetch-Dest","iframe")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Mode","navigate")
                .addHeader("Referer","https://store.steampowered.com/login/")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","zh-CN,zh;q=0.9");
        if (isHelper){
            builder.addHeader("Host","help.steampowered.com")
                    .addHeader("Sec-Fetch-Site","same-site");
        }else{
            builder.addHeader("Host","steamcommunity.com")
                    .addHeader("Sec-Fetch-Site","cross-site");
        }
    }


    @Override
    public RequestBody requestBody() {
        StringBuilder parameter = new StringBuilder();
        if (Dologin.transfer_parameters != null){
            Set<String> keySet = Dologin.transfer_parameters.keySet();
            for (String key:keySet){
                parameter.append(key).append("=").append(Dologin.transfer_parameters.getString(key)).append("&");
            }
            parameter.deleteCharAt(parameter.length()-1);
        }
        return RequestBody.
                create(MediaType.parse("application/x-www-form-urlencoded"),
                        parameter.toString());
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("transfer fail: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
//        byte[] raw = response.body().bytes();
//        System.out.println(new String(TEA.gzipa(raw)));
        if (isHelper){
            new Transfer("https://steamcommunity.com/login/transfer",false).post();
        }else{
            //TODO: 转移完成，准备获取通知信息数量
            new GetNotificationCounts("https://steamcommunity.com/actions/GetNotificationCounts").get();
        }
        System.out.println("Transfer over");
    }
}
