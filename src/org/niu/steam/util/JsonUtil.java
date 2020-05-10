package org.niu.steam.util;

import org.json.JSONObject;

import java.util.Set;

public class JsonUtil {

    public static void main(String[] args) {
        String json = "{\"success\":true,\"requires_twofactor\":false,\"login_complete\":true,\"transfer_urls\":[\"https:\\/\\/steamcommunity.com\\/login\\/transfer\",\"https:\\/\\/help.steampowered.com\\/login\\/transfer\"],\"transfer_parameters\":{\"steamid\":\"76561198098892720\",\"token_secure\":\"3516B87ACE76E5C955C87E8E84F55D0CD96E0157\",\"auth\":\"7d73375cba25d73a0c41c6e749a7a1d7\",\"remember_login\":false,\"webcookie\":\"1E93E65916F3780728DB650D147DDB00C6226EF8\"}}";
        JSONObject jsonObject = new JSONObject(json);
        jsonObject = jsonObject.getJSONObject("transfer_parameters");
        StringBuilder parameter = new StringBuilder();
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            parameter.append(key).append("=").append(jsonObject.get(key)).append("&");
        }
        parameter.deleteCharAt(parameter.length() - 1);
        System.out.println(parameter.toString());
    }

    public static void jsonObject2Map() {

    }
}
