package org.simplehttp.server.pojo.protocol;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class URLWrapper {
    private URL url;
    private HashMap<String, String> parameterMap;

    public URLWrapper(URL url) throws RuntimeException{
        this.url = url;
        if(url.getQuery() != null && !url.getQuery().isEmpty()){
            parameterMap = new HashMap<>();
            // 编码检查，不接受未经编码的请求
            checkQueryFormat(url.getQuery());

            String[] query = url.getQuery().split("[&=]");

            for (int i = 0; i < query.length; ) {
                String key = decodeKeyAndValue(query[i]);
                String value = i + 1 < query.length ? decodeKeyAndValue(query[i + 1]) : "";
                parameterMap.put(key, value);
                i += 2;
            }
        }
    }

    public String getQueryValue(String key){
        return parameterMap.get(key);
    }

    /**
     * 检查请求合法性
     * @param query 请求串
     */
    private void checkQueryFormat(String query) throws RuntimeException{
        char[] chars = query.toCharArray();
        int equalCount = 0;
        int andCount = 0;
        int EXTEND_ASCII_LIMIT = 255;

        for(char c : chars){
            if(c > EXTEND_ASCII_LIMIT){
                throw new RuntimeException("Bad Url");
            }
            if('=' == c){
                ++ equalCount;
            }else if('&' == c){
                ++ andCount;
            }
        }

        if(andCount != equalCount - 1){
            throw new RuntimeException("Bad Url");
        }
    }

    /**
     * 请求解码，UTF-8
     */
    private String decodeKeyAndValue(String kov){
        byte[] origin = kov.getBytes();
        return new String(origin, StandardCharsets.UTF_8);
    }
}
