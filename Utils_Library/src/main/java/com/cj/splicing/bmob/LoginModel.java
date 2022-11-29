package com.cj.splicing.bmob;

import com.cj.splicing.bean.TokenBean;
import com.cj.splicing.bean.User;
import com.cj.splicing.common.base.AppContext;
import com.cj.splicing.utils.DateTimeUtils;
import com.cj.splicing.utils.GsonHelper;
import com.cj.splicing.utils.TestMd5AndSha1;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.toast.ToastUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登录
 */
public class LoginModel extends BaseModel {

    BmobQuery<User> exist;
    public void isLogin(String phone,String password){
        if(exist==null){
            exist=new BmobQuery<User>();
        }
        exist.addWhereEqualTo("Phone",phone);
        exist.addWhereEqualTo("Password",password);
        exist.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null&&list.size()>0){//用户未注册
                    if(list.get(0).getStatus()) {
                        ToastUtil.Show("登录成功!");
                        Tools.setSharedPreferencesValues(AppContext.getContext(),"UId",list.get(0).getUId());
                        request.isSucceed(true);
                    }
                    else {
                        ToastUtil.Show("该账号已注销,请在180天后重新注册！");
                        request.isSucceed(false);
                    }
                }
                else {
                    request.isSucceed(false);
                    ToastUtil.Show("用户名或密码错误!");
                }
            }
        });
    }


    /**
     * 验证账号是否存在
     * @param phone
     */
    public void isExist(String phone) {
        if (exist == null) {
            exist = new BmobQuery<User>();
        }
        exist.addWhereEqualTo("Phone", phone);
        exist.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {//用户已注册
                    if(list.get(0).getStatus()) {
                        ToastUtil.Show("账号已存在,请勿重复注册！");
                        request.isSucceed(false);
                    }
                    else {
                        ToastUtil.Show("该账号已注销,请在180天后重新注册！");
                        request.isSucceed(false);
                    }
                } else {
                    /*try {
                        getToken(phone);
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        noSuchAlgorithmException.printStackTrace();
                        request.isSucceed(false);
                    }*/
                    inLogin(phone,"MyMM5JOVLLsoMDC6DhuHphJzrz7kgejdJUd97MALp3D5SPxnWqa9eQ==@an1m.cn.rongnav.com;an1m.cn.rongcfg.com",DateTimeUtils.getId());
                    //request.isSucceed(false);
                    //ToastUtil.Show("当前账号不存在,请注册后重试!");
                }
            }
        });
    }

    /**
     * 验证账号是否存在
     * @param phone
     */
    public void isExist(String phone,String password) {
        if (exist == null) {
            exist = new BmobQuery<User>();
        }
        exist.addWhereEqualTo("Phone", phone);
        exist.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {//用户已注册
                    if(list.get(0).getStatus()) {
                        ToastUtil.Show("账号已存在,请勿重复注册！");
                        request.isSucceed(false);
                    }
                    else {
                        ToastUtil.Show("该账号已注销,请在180天后重新注册！");
                        request.isSucceed(false);
                    }
                } else {
                    /*try {
                        getToken(phone);
                    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                        noSuchAlgorithmException.printStackTrace();
                        request.isSucceed(false);
                    }*/
                    inLogin(phone,password,"MyMM5JOVLLsoMDC6DhuHphJzrz7kgejdJUd97MALp3D5SPxnWqa9eQ==@an1m.cn.rongnav.com;an1m.cn.rongcfg.com",DateTimeUtils.getId());
                    //request.isSucceed(false);
                    //ToastUtil.Show("当前账号不存在,请注册后重试!");
                }
            }
        });
    }

    /**
     * 自定义密码
     */
    User user;
    public void inLogin(String phone,String password,String token,String UId){
        user=new User();
        user.setIcon("http://juzixiaoyuan.top/user_icon.png");
        user.setPhone(phone);
        user.setUId(UId);
        user.setUserName("用户"+phone.substring(7,11));
        user.setPassword(password);
        user.setStatus(true);
        user.setR_Token(token);
        user.setAge("22");
        user.setSex(2);
        user.setCity("成都市");
        user.setSign("这人懒得啥都没有...");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    ToastUtil.Show("注册成功!");
                    request.isSucceed(true);
                }
                else {
                    ToastUtil.Show("注册失败!");
                    request.isSucceed(false);
                }
            }
        });
    }

    /**
     * 密码默认后六位
     * @param phone
     * @param token
     * @param UId
     */
    public void inLogin(String phone,String token,String UId){
        user=new User();
        user.setIcon("http://juzixiaoyuan.top/user_icon.png");
        user.setPhone(phone);
        user.setUId(UId);
        user.setUserName("用户"+phone.substring(7,11));
        user.setPassword(phone.substring(5,11));
        user.setStatus(true);
        user.setR_Token(token);
        user.setAge("22");
        user.setSex(2);
        user.setCity("成都市");
        user.setSign("这人懒得啥都没有...");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    ToastUtil.Show("注册成功!");
                    request.isSucceed(true);
                }
                else {
                    ToastUtil.Show("注册失败!");
                    request.isSucceed(false);
                }
            }
        });
    }

    /**
     * 新用户Token获取
     */
    private TokenBean tokenBean;
    private  void getToken(String phone) throws NoSuchAlgorithmException {
        String UId=UUID.randomUUID().toString();
        HttpParams httpParams = new HttpParams();
        httpParams.put("userId",UId);
        httpParams.put("name", "用户"+phone.toString().substring(7,11));
        httpParams.put("portraitUri", "http://juzixiaoyuan.top/user_icon.png");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time= DateTimeUtils.getId();
        String va= AppContext.AppSecret+time+timestamp.getTime();
        String Signature= TestMd5AndSha1.sha1(va);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.put("App-Key",AppContext.AppKey);
        httpHeaders.put("Nonce",time);
        httpHeaders.put("Timestamp",timestamp.getTime()+"");
        httpHeaders.put("Signature", Signature);
        OkGo.<String>post(" https://api2-cn.ronghub.com/user/getToken.json").headers(httpHeaders).params(httpParams).execute(new StringCallback(){
            @Override
            public void onSuccess(Response<String> response) {
                Type type = new TypeToken<TokenBean>() {
                }.getType();
                tokenBean = GsonHelper.gson.fromJson(response.body(), type);
                if(tokenBean.getCode()==200){
                    inLogin(phone,tokenBean.getToken(),UId);
                }
                else {
                    inLogin(phone,"5CQlwwMlgWASc68+5IHo3Qhoj28FZ1D97thRZLkkQYY=@an1m.cn.rongnav.com;an1m.cn.rongcfg.com",UId);
                    //request.isSucceed(false);
                    //ToastUtil.Show("融云Token获取失败!");
                }
                Tools.setSharedPreferencesValues(AppContext.getContext(),"UId",UId);
            }
        });
    }

}
