package org.niu.steam.tasks;

import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventory extends Task {

    private static boolean isForward = false;

    public Inventory(String url,boolean isForward) {
        super(url);
        this.isForward = isForward;
    }

    // https://steamcommunity.com/profiles/76561198098892720/inventory/

    public static String[] needCookie(String path){
        List<String> list = new ArrayList<>();
        list.add("steamCountry");
        list.add("steamLoginSecure");
        list.add("sessionid");
        list.add("steamMachineAuth"+Dologin.steamid);
        list.add("browserid");
        if (isForward){
            list.add("webTradeEligibility");
        }
//        System.out.println(Arrays.toString(list.toArray(new String[]{})));
        return list.toArray(new String[]{});
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder.addHeader("Host", "steamcommunity.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "cross-site")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Referer", "https://store.steampowered.com/")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9");
    }

    @Override
    public RequestBody requestBody() {
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("Inventory fail: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() == 302){
            String location = response.header("Location");
            new Inventory(location,true).get();
            ALL.isOk = true;
        }else{
            if (ALL.isOk){
                new ALL("https://steamcommunity.com/inventory/"+Dologin.steamid+"/"+ Getrsakey.appid+"/2?l=schinese&count=75").get();
            }
        }

    }
}
