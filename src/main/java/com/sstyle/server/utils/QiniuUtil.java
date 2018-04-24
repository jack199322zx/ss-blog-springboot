package com.sstyle.server.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;


public class QiniuUtil {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = "7xdp9j80ueNoBq0tRkyfdyMzYKnGI8w-GxN_uYK3";
    private static final String SECRET_KEY = "tG0zzWjbF7ArwsfKURGDGYpdr5XQdQTRNTPrgaLA";
    //要上传的空间
    private static final String bucketname = "52nino-qiniu-cloud";
    private static final String bucketdomain = "http://p7mtoq4re.bkt.clouddn.com/";
    //上传到七牛后保存的文件名
    private static final String key = "xxx";
    //上传文件的路径
    private static final String FilePath = "/.../...";

    private static Auth auth;

    private static Zone z;

    private static Configuration c;

    private static UploadManager uploadManager;

    static {
        //密钥配置
        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        // 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
        z = Zone.autoZone();
        c = new Configuration(z);
        //创建上传对象
        uploadManager = new UploadManager(c);
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    //固定格式上传，需设置凭证过期时间
    public static String getUpToken(long expireSeconds) {
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        String upToken = auth.uploadToken(bucketname, null, expireSeconds, putPolicy);
        return null;
    }

    /**
     * 本地上传，只需设定filePath
     * @Param filePath
     * @Return
     * @Date 2018/4/24 10:10
     */
    public static void upload(String filePath) throws IOException {
        try {
            //
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            Response res = uploadManager.put(filePath, null, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }

    /**
     * 字节数组上传
     * @Param byte[] bytes
     * @Return
     * @Date 2018/4/24 10:11
     */
    public static String uploadBytes(byte[] bytes) throws QiniuException {
        String upToken = getUpToken();
        DefaultPutRet putRet = new DefaultPutRet();
        try {
            Response response = uploadManager.put(bytes, null, upToken);
            //解析上传成功的结果
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
        }
        return putRet.hash;
    }

    /**
     * 数据流上传
     * @Param  byte[]
     * @Return
     * @Date 2018/4/24 10:12
     */
    public static String uploadInputStream(byte[] bytes) throws QiniuException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        DefaultPutRet putRet = new DefaultPutRet();
        String upToken = getUpToken();
        try {
            Response response = uploadManager.put(byteInputStream, null, upToken, null, null);
            //解析上传成功的结果
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
        }
        return putRet.hash;
    }

    // 文件下载
    public static String download(String fileName) throws IOException{
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String publicUrl = String.format("%s/%s", bucketdomain, encodedFileName);
        return publicUrl;
    }

    //删除文件
    public static boolean delete(String fileName) throws IOException{
        BucketManager bucketManager = new BucketManager(auth, c);
        try {
            bucketManager.delete(bucketname, fileName);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return true;
    }


    public static String uploadBase64(String data64){
        String key = renamePic(".png");
        //服务端http://up-z2.qiniup.com
        String url = "http://upload-z2.qiniu.com/putb64/key"+ UrlSafeBase64.encodeToString(key);
        RequestBody rb = RequestBody.create(null, data64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bucketdomain + key;
    }

    public static String base64Data(String data){

        if(data==null||data.isEmpty()){
            return "";
        }
        String base64 =data.substring(data.lastIndexOf(",")+1);
        return base64;
    }

    /**
     * 以UUID重命名
     * @param fileName
     * @return
     */
    public static String renamePic(String fileName){
        String extName = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString().replace("-","")+extName;
    }




    public static void main(String args[]) throws IOException {
//        new QiniuUtil().upload();
    }

}
