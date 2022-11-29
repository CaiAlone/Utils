package com.cj.splicing.utils.upload;

import android.os.Environment;
import android.util.Log;

import com.cj.splicing.bmob.BaseModel;
import com.cj.splicing.bmob.UserModel;
import com.cj.splicing.common.base.AppContext;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.utils.toast.ToastUtil;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 图片上传
 * Created By CaiJing On 2022/20/27
 */
public class ImageUploadUtils extends BaseModel {

    private UserModel model;
    public ImageUploadUtils(){
        model=new UserModel();
    }

    /**
     * 图片压缩
     * @param photos
     */
    List<String> photo;
    private int maxsize=0;
    public void LuBan(List<String> photos){
        maxsize=photos.size();
        photo=new ArrayList<String>();
        Luban.with(AppContext.getContext())
                .load(photos)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        Log.i("鲁班压缩","压缩开始!");
                    }
                    @Override
                    public void onSuccess(File file) {
                        File dir= Environment.getExternalStorageDirectory();
                        String path=dir.getAbsolutePath();
                        Posts(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        request.isSucceed(false);
                        ToastUtil.Show("压缩失败!");
                        Log.i("鲁班压缩","压缩失败：+"+e);
                    }
                }).launch();    //启动压缩
    }

    public void LuBan(String photo){
        Luban.with(AppContext.getContext())
                .load(photo)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        Log.i("鲁班压缩","压缩开始!");
                    }
                    @Override
                    public void onSuccess(File file) {
                        File dir= Environment.getExternalStorageDirectory();
                        String path=dir.getAbsolutePath();
                        Post_QN(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.Show("压缩失败!");
                        Log.i("鲁班压缩","压缩失败：+"+e);
                    }
                }).launch();    //启动压缩
    }


    /**
     * 七牛上传图片
     * @param imageurl
     */
    String key = getKey();
    String Icon_Uri;
    public void Post_QN(String imageurl){
        Configuration config = new Configuration.Builder()
                .connectTimeout(90)              // 链接超时。默认90秒
                .useHttps(true)                  // 是否使用https上传域名
                .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
                .concurrentTaskCount(3)          // 并发上传线程数量为3
                .responseTimeout(90)             // 服务器响应超时。默认90秒
                .zone(FixedZone.zone2)           // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        String token=getUpToken();
        uploadManager.put(imageurl, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Icon_Uri="http://juzixiaoyuan.top/" + key;
                            Log.i("七牛云", "上传成功!");
                            model.editUserInfo("icon",Icon_Uri);
                            ToastUtil.Show("上传成功!");
                        } else {
                            request.isSucceed(false);
                            ToastUtil.Show("上传失败!");
                            Log.i("七牛云", "上传失败");
                            //如果失败，这里可以把 info 信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
        Log.i("七牛云", getUpToken());

        model.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    request.isSucceed(true);
                }
                else {
                    request.isSucceed(false);
                }
            }
        });
    }


    /**
     * 多图片上传
     * @param imageurl
     */
    public void Posts(String imageurl){
        Configuration config = new Configuration.Builder()
                .connectTimeout(90)              // 链接超时。默认90秒
                .useHttps(true)                  // 是否使用https上传域名
                .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
                .concurrentTaskCount(3)          // 并发上传线程数量为3
                .responseTimeout(90)             // 服务器响应超时。默认90秒
                .zone(FixedZone.zone2)           // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadManager uploadManager = new UploadManager(config);
        String token=getUpToken();
        uploadManager.put(imageurl, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Icon_Uri="http://juzixiaoyuan.top/" + key;
                            Log.i("qiniu", "上传成功!");
                            photo.add(Icon_Uri);
                            ToastUtil.Show("上传成功!");
                            if(photo.size()==maxsize){
                                requestData.isSucceed(true,"images",photo);
                            }
                        } else {
                            requestData.isSucceed(false,"images",null);
                            ToastUtil.Show("上传失败!");
                            Log.i("qiniu", "上传失败");
                            //如果失败，这里可以把 info 信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("七牛云", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
        Log.i("qiniu created token", getUpToken());
    }


    /**
     * 七牛上传文件名
     * @return
     */
    private String getKey(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String key = simpleDateFormat.format(new Date()).toString();
        Random random=new Random();
        key=System.currentTimeMillis()+""+ UUID.randomUUID().toString();
        return key+".png";
    }

    /**
     * 覆盖上传
     * @return
     */
    Auth auth = Auth.create(AppContext.ACCESS_KEY, AppContext.SECRET_KEY);
    //要上传的空间
    String bucketname = "cj-images";
    public String getUpToken(){
        //<bucket>:<key>，表示只允许用户上传指定key的文件。在这种格式下文件默认允许“修改”，已存在同名资源则会被本次覆盖。
        key=getKey();
        return auth.uploadToken(bucketname, key);
    }



}
