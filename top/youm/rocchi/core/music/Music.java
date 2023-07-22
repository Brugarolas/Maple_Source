package top.youm.rocchi.core.music;

/**
 * @author YouM
 * Created on 2023/7/20
 */
public class Music {
    private String name;
    private String url;
    private String pictureURL;

    public Music() {
    }

    public Music(String name, String url, String pictureURL) {
        this.name = name;
        this.url = url;
        this.pictureURL = pictureURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
