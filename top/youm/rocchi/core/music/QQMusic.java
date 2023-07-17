package top.youm.rocchi.core.music;

import top.youm.rocchi.utils.network.HttpUtils;

/**
 * @author YouM
 * Created on 2023/7/17
 * dinner(stupid) QQ Music Player and Tencent fuck you
 */
public class QQMusic {
    public static final String getHome = "https://c.y.qq.com/rsc/fcgi-bin/fcg_get_profile_homepage.fcg";
    public static boolean getHomepage(String uin){
        String s = HttpUtils.sendRequest(getHome +
                        "?_=1689578883856" +
                        "&cv=4747474&ct=24" +
                        "&format=json&inCharset=utf-8&outCharset=utf-8&notice=0" +
                        "&platform=yqq.json&needNewCode=0" +
                        "&uin=" + uin +
                        "&g_tk_new_20200303=199512355&g_tk=199512355&cid=205360838" +
                        "&userid=" + uin + "&reqfrom=1&reqtype=0&hostUin=0&loginUin=" + uin,
                "GET");
        System.out.println(s);
        return false;
    }
    public static boolean getDiss(String uin){
        String s = HttpUtils.sendRequest(getHome +
                        "?_=1689578883856" +
                        "&cv=4747474&ct=24" +
                        "&format=json&inCharset=utf-8&outCharset=utf-8&notice=0" +
                        "&platform=yqq.json&needNewCode=0" +
                        "&uin=" + uin +
                        "&g_tk_new_20200303=199512355&g_tk=199512355&cid=205360838" +
                        "&userid=" + uin + "&reqfrom=1&reqtype=0&hostUin=0&loginUin=" + uin,
                "GET");
        System.out.println(s);
        return false;
    }
    public static void main(String[] args) {
        getHomepage("1055965862");
    }
}
