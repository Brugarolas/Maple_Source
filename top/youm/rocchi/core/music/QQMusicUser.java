package top.youm.rocchi.core.music;

/**
 * @author YouM
 * Created on 2023/7/20
 */
public class QQMusicUser {
    private String userCookie;
    private String userUin;

    public QQMusicUser(String userUin,String userCookie) {
        this.userCookie = userCookie;
        this.userUin = userUin;
    }

    public String getUserCookie() {
        return userCookie;
    }
    public void setUserCookie(String userCookie) {
        this.userCookie = userCookie;
    }
    public String getUserUin() {
        return userUin;
    }
    public void setUserUin(String userUin) {
        this.userUin = userUin;
    }
}
