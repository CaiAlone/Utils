package com.cj.splicing.bmob;


import android.net.Uri;

import com.cj.splicing.bean.User;
import com.cj.splicing.common.base.AppContext;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.toast.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.userinfo.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

/**
 * 用户信息修改
 * Created By CaiJing On 2022/10/20
 */
public class UserModel extends BaseModel{


    User user;
    public void editUserInfo(String key,String values){
        user=new User();
        switch (key){
            case "icon"://修改头像
                user.setIcon(values);
                user.setStatus(Tools.getSharedPreferencesValues(AppContext.getContext(), "Status", true));
                user.setSex(Tools.getSharedPreferencesValues(AppContext.getContext(), "Sex", 1));
                break;
            case "name"://修改用户名
                user.setUserName(values);
                user.setStatus(Tools.getSharedPreferencesValues(AppContext.getContext(), "Status", true));
                user.setSex(Tools.getSharedPreferencesValues(AppContext.getContext(), "Sex", 1));
                break;
            case "sign"://修改签名
                user.setSign(values);
                user.setStatus(Tools.getSharedPreferencesValues(AppContext.getContext(), "Status", true));
                user.setSex(Tools.getSharedPreferencesValues(AppContext.getContext(), "Sex", 1));
                break;
            case "city"://城市
                user.setCity(values);
                user.setStatus(Tools.getSharedPreferencesValues(AppContext.getContext(), "Status", true));
                user.setSex(Tools.getSharedPreferencesValues(AppContext.getContext(), "Sex", 1));
                break;
            case "sex"://性别
                if(values.contains("男")) {
                    user.setSex(1);
                }
                else {
                    user.setSex(2);
                }
                user.setStatus(Tools.getSharedPreferencesValues(AppContext.getContext(), "Status", true));
                break;
            case "age"://年龄
                user.setAge(values);
                user.setStatus(Tools.getSharedPreferencesValues(AppContext.getContext(), "Status", true));
                user.setSex(Tools.getSharedPreferencesValues(AppContext.getContext(), "Sex", 1));
                break;
            case "status"://注销
                if(values.contains("false")){
                    user.setStatus(false);
                    user.setSex(Tools.getSharedPreferencesValues(AppContext.getContext(),"Sex",1));
                }
                break;
            default:
                break;
        }
        user.update(Tools.getSharedPreferencesValues(AppContext.getContext(), "objectId", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    if(!key.equals("status")) {
                        ToastUtil.Show("修改成功!");
                    }
                    request.isSucceed(true);
                }
                else {
                    ToastUtil.Show("修改失败,请稍后重试!");
                    request.isSucceed(false);
                }
            }
        });

    }


    /**
     * 获取用户信息
     */
    BmobQuery<User> userBmobQuery;
    public void getUserInfo(){
        userBmobQuery=new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo("UId",Tools.getSharedPreferencesValues(AppContext.getContext(),"UId",""));
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null&&list.size()>0){
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"Icon",list.get(0).getIcon());
                    if(Tools.isEmpty(list.get(0).getR_Token())){
                        Tools.setSharedPreferencesValues(AppContext.getContext(), "R_Token", "MyMM5JOVLLsoMDC6DhuHphJzrz7kgejdJUd97MALp3D5SPxnWqa9eQ==@an1m.cn.rongnav.com;an1m.cn.rongcfg.com");
                    }
                    else {
                        Tools.setSharedPreferencesValues(AppContext.getContext(), "R_Token", list.get(0).getR_Token());
                    }
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"Icon",list.get(0).getIcon());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"UserName",list.get(0).getUserName());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"Age",list.get(0).getAge());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"Sex",list.get(0).getSex());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"Sign",list.get(0).getSign());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"Status",list.get(0).getStatus());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"objectId",list.get(0).getObjectId());
                    Tools.setSharedPreferencesValues(AppContext.getContext(),"City",list.get(0).getCity());


                    UserInfo userInfo = new UserInfo(list.get(0).getUId(), list.get(0).getUserName(), Uri.parse(list.get(0).getIcon()));
                    RongUserInfoManager.getInstance().refreshUserInfoCache(userInfo);

                    request.isSucceed(true);
                }
                else {
                    request.isSucceed(false);
                }
            }
        });
    }


    /**
     * 获取指定用户信息
     * @param UId
     */
    public void getUserInfo(String UId){
        userBmobQuery=new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo("UId",UId);
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null&&list.size()>0){
                    requestData.isSucceed(true,"data",list.get(0));
                }
                else {
                    requestData.isSucceed(false,"data",null);
                }
            }
        });
    }


    /**
     * 验证账号是否存在
     * @param phone
     */
    BmobQuery<User> exist;
    public void isExist(String phone,String Password) {
        if (exist == null) {
            exist = new BmobQuery<User>();
        }
        exist.addWhereEqualTo("Phone", phone);
        exist.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {//用户已注册
                    if(list.get(0).getStatus()) {
                        Tools.setSharedPreferencesValues(AppContext.getContext(), "UId", list.get(0).getUId());
                        upPassword(list.get(0).getObjectId(),Password,list.get(0).getSex());
                    }
                    else {
                        ToastUtil.Show("该账号已注销,请在180天后重新注册！");
                        request.isSucceed(false);
                    }
                } else {
                        request.isSucceed(false);
                }
            }
        });
    }


    User up_user;
    private void upPassword(String objId,String Password,int sex){
        up_user=new User();
        up_user.setPassword(Password);
        up_user.setStatus(true);
        up_user.setSex(sex);
        up_user.update(objId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    request.isSucceed(true);
                    ToastUtil.Show("修改成功!");
                }
                else {
                    request.isSucceed(false);
                }
            }
        });
    }


}
