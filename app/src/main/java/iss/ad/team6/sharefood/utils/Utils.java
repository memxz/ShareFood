package iss.ad.team6.sharefood.utils;

import java.util.Map;

public class Utils {
    public static String parseGetUrl(String url, Map<String, String> params){
        StringBuilder builder = new StringBuilder(url);
        if (params!=null){
            int num = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (num == 0){
                    builder.append("?"+entry.getKey()+"="+entry.getValue());
                    num++;
                }else {
                    builder.append("&"+entry.getKey()+"="+entry.getValue());
                }
            }
        }

        return builder.toString();
    }
}
