package top.youm.maple.utils.tools;

/**
 * @author YouM
 * Created on 2023/8/22
 */
public class StringUtils {
    public static String upperCaseLowerOther(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
