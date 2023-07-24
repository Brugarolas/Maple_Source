package top.youm.rocchi.core.music;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import top.youm.rocchi.utils.network.HttpUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author NameFlying
 * Created on 2023/7/17
 * My name is NameFlying ,This is my first file in the project
 * 实现了QQMusicAPI的调用 纯java实现
 * QQ音乐的登录处理在网上没有找到合适的方案，所以按照某个github项目的实现<br/>
 */
@SuppressWarnings("all")
public class QQMusicApi {
    private final String HOST = "c.y.qq.com";
    //用户的主页API获取,通过此API可以实现 获取用户头像，创建的歌单...
    private final String HOME_PAGE_API = "https://c.y.qq.com/rsc/fcgi-bin/fcg_get_profile_homepage.fcg";
    //用户的歌单的详细信,通过此API可以获取歌单内的歌曲详细信息，包括歌曲 mid 和 图片的 id
    private final String DISSID_API = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_playlist_cp.fcg?cv=10000&ct=19&newsong=1&tpl=wk&id=";
    //获取音乐播放地址,通过此API 获取 歌曲的播放地址 需要id参数 内容是 歌曲的 mid 可以通过DISSID_API获取
    private final String GET_MUSIC_PLAYER = "http://yumbo.top:3300/song/url?id=";
    //设置用户cookie 通过此API 设置用户cookie
    private final String SET_COOKIE = "http://yumbo.top:3300/user/setCookie";
    //获取歌曲图片,通过此API 获取歌曲的图片,需要传入歌曲 id 参数 并进行一些数学计算 (
    private final String GET_PICTURE = "http://imgcache.qq.com/music/photo/album_300/";
    private final String userInfo;
    private final String uin;

    private static String cookie;
    //请求头
    private static final Map<String,String> HEADERS = new HashMap<>();
    public QQMusicApi(String uin,String cookie){
        this.cookie = cookie;
        this.uin = uin;
        this.userInfo = "?_=1689578883856" +
                "&cv=4747474&ct=24" +
                "&format=json&inCharset=utf-8&outCharset=utf-8&notice=0" +
                "&platform=yqq.json&needNewCode=0" +
                "&uin=" + uin +
                "&g_tk_new_20200303=199512355&g_tk=199512355&cid=205360838" +
                "&userid=" + uin + "&reqfrom=1&reqtype=0&hostUin=0&loginUin=" + uin;
        this.HEADERS.put("Accept","application/json;charset=UTF-8");
        this.HEADERS.put("Accept-Encoding","gzip, deflate, br");
        this.HEADERS.put("Connection","keep-alive");
        this.HEADERS.put("Host",HOST);
        this.HEADERS.put("Cookie",cookie);
        this.HEADERS.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0");
    }

    public Result<?> setCookie(){
        try {
            String s = HttpUtils.POSTRequest(SET_COOKIE, HEADERS,"{\"data\":\"" + cookie + "\"}");
            return Result.success(s);
        } catch (IOException e) {
            return Result.failed("failed to set cookie");
        }
    }
    public Result<List<String>> getDissesID(){
        try {
            String context = HttpUtils.GETRequest(HOME_PAGE_API + userInfo,HEADERS);
            JsonObject mainJsonObj = new JsonParser().parse(context).getAsJsonObject();
            JsonObject dataJsonObj = mainJsonObj.get("data").getAsJsonObject();
            JsonObject mydissJsonObject = dataJsonObj.get("mydiss").getAsJsonObject();
            int num = mydissJsonObject.get("num").getAsInt();
            JsonArray list = mydissJsonObject.get("list").getAsJsonArray();
            List<String> disses = new ArrayList<>();
            for (JsonElement element : list) {
                JsonObject asJsonObject = element.getAsJsonObject();
                String subtitle = asJsonObject.get("subtitle").getAsString();
                int number = subtitle.indexOf("首");
                String substring = subtitle.substring(0, number);
                int musicNum = Integer.parseInt(substring);
                disses.add(asJsonObject.get("dissid").getAsString());
            }
            return (Result<List<String>>) Result.success(disses);
        } catch (IOException e) {
            e.printStackTrace();
            return (Result<List<String>>) Result.failed(null);
        }
    }
    public Result<List<Music>> getSongsByDissid(String dissid){
        try {
            String context = HttpUtils.htmlRequest(DISSID_API + dissid + "&g_tk=1126825984&platform=mac&g_tk_new_20200303=1694301397&loginUin=1055965862&hostUin=0&format=json&inCharset=GB2312&outCharset=utf-8&notice=0&platform=jqspaframe.json&needNewCode=0", "GET", HOST, cookie);
            System.out.println(context);
            JsonObject mainJsonObj = new JsonParser().parse(context).getAsJsonObject();
            JsonObject dataJsonObj = mainJsonObj.get("data").getAsJsonObject();
            JsonArray cdlist = dataJsonObj.get("cdlist").getAsJsonArray();
            JsonObject cd = cdlist.get(0).getAsJsonObject();
            JsonArray songlist = cd.get("songlist").getAsJsonArray();
            ArrayList<Music> arrayList = new ArrayList<>();
            for (JsonElement songid : songlist) {
                arrayList.add(new Music(songid.getAsJsonObject().get("title").toString(),songid.getAsJsonObject().get("mid").toString(),""/*getPictureByID(Integer.parseInt(songid.getAsJsonObject().get("id").getAsString())*/));
            }
            return (Result<List<Music>>) Result.success(arrayList);
        } catch (IOException e) {
            e.printStackTrace();
            return (Result<List<Music>>) Result.failed("failed to get songs");
        }
    }
    public Result<?> getMusicPlayer(String mid)  {
        try {
            String context = HttpUtils.GETRequest(GET_MUSIC_PLAYER + mid,HEADERS);
            JsonObject mainJsonObj = new JsonParser().parse(context).getAsJsonObject();
            JsonElement element = mainJsonObj.get("data");
            return Result.success(element.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed("failed to get music file");
        }
    }
    private static void downloadPicture(String urlList) {

        try {
            URL url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir,  urlList + ".jpg"));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private final HashMap<String, ResourceLocation> artsLocations = new HashMap<>();
    public String getPictureByID(int id){
        try {
            String s = HttpUtils.getRedirectUrl(GET_PICTURE + String.valueOf(id % 100) + "/300_albumpic_" + String.valueOf(id) + "_0.jpg");
            downloadPicture(s.replaceAll("\"",""));
            s.replaceAll("\"","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
    public void loadTexture(String id){
        String idName = id.replaceAll("\"", "") + ".jpg";
        File path = new File(Minecraft.getMinecraft().getFileAssets(),idName);
        new Thread(() -> {
            artsLocations.put(idName, null);
            ResourceLocation rl = new ResourceLocation("musicCache/" + idName);
            IImageBuffer iib = new IImageBuffer() {
                final ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage image) {
                    return image;
                }

                @Override
                public void skinAvailable() {
                    artsLocations.put(idName, rl);
                }
            };

            ThreadDownloadImageData textureArt = new ThreadDownloadImageData(path, null, null, iib);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
        }).start();
    }

}
