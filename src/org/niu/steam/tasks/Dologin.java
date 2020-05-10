package org.niu.steam.tasks;

import okhttp3.*;
import org.json.JSONObject;
import org.niu.steam.util.TEA;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Scanner;


public class Dologin extends Task {

    public static JSONObject transfer_parameters;
    public static String steamid = "";
    public static String emailsteamid = "";
    public static String emailauth = "";

    public Dologin(String url) {
        super(url);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("fail: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        byte[] raw = response.body().bytes();
        JSONObject object = new JSONObject(new String(TEA.gzipa(raw)));
        boolean success = object.getBoolean("success");
        if (success) {
            boolean login_complete = object.getBoolean("login_complete");
            if (login_complete) {
                //TODO: 验证成功后
                JSONObject transfer_parameters = object.getJSONObject("transfer_parameters");
                steamid = transfer_parameters.getString("steamid");
                new Transfer("https://help.steampowered.com/login/transfer",true).post();
            }
        } else {
            //TODO: 邮箱验证
            emailsteamid = object.getString("emailsteamid");
            setCode();
            new Getrsakey("https://store.steampowered.com/login/getrsakey").post();
        }
        System.out.println("Dologin over");

//            JSONObject transfer_parameters = object.getJSONObject("transfer_parameters");
//            steamid = transfer_parameters.getString("steamid");
//            token_secure = transfer_parameters.getString("token_secure");
//            auth = transfer_parameters.getString("auth");
//            remember_login = transfer_parameters.getBoolean("remember_login");
//            webcookie = transfer_parameters.getString("webcookie");
//            new Transfer("https://steamcommunity.com/login/transfer",false).post();
//            new GetNotificationCounts("https://steamcommunity.com/actions/GetNotificationCounts").get();
//        success = object.getBoolean("success");
//        requires_twofactor = object.getBoolean("requires_twofactor");
//        login_complete = object.getBoolean("login_complete");
//        message = object.getString("message");
//        emailauth_needed = object.getBoolean("emailauth_needed");
//        emaildomain = object.getString("emaildomain");
//        System.out.println(this);
    }

    private void setCode() {
        Scanner in = new Scanner(System.in);
        System.out.print("邮箱验证码：");
        emailauth = in.nextLine();
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder.addHeader("Host", "store.steampowered.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0 Win64 x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded charset=UTF-8")
                .addHeader("Origin", "https://store.steampowered.com")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Referer", "https://store.steampowered.com/login/")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zhq=0.9");
    }

    public static String[] needCookie(String path) {
        String[] cks = new String[]{"browserid", "steamCountry", "sessionid", "steamLoginSecure"};
        return cks;
    }

    @Override
    public RequestBody requestBody() {
        StringBuilder parameter = new StringBuilder();
        parameter.append("donotcache=" + System.currentTimeMillis() + "&");
        try {
            parameter.append("password=" + URLEncoder.encode(Getrsakey.enpass, "utf-8") + "&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        parameter.append("username=" + Getrsakey.username + "&");
        parameter.append("twofactorcode=&");
        parameter.append("emailauth=" + emailauth + "&");
        parameter.append("loginfriendlyname=&");
        parameter.append("captchagid=-1&");
        parameter.append("captcha_text=&");
        parameter.append("emailsteamid=" + emailsteamid + "&");
        parameter.append("rsatimestamp=" + Getrsakey.timestamp + "&");
        parameter.append("remember_login=false");
        return RequestBody.
                create(MediaType.parse(" application/x-www-form-urlencoded; charset=UTF-8"),
                        parameter.toString());
    }
}
