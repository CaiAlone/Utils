package com.cj.splicing.bmob;

import com.cj.splicing.common.base.BaseBean;
import com.cj.splicing.utils.GsonHelper;
import com.cj.splicing.utils.toast.ToastUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.Type;

/**
 * 验证码登录注册
 * Created By CaiJing On 2022/11/7
 */
public final class PsdModel {

    /**
     * 获取登录验证码接口
     */
    public static void getLoginCode(String phone) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("phone", phone);
        httpParams.put("name", "");
        httpParams.put("key", "smsid5");
        OkGo.<String>post("").params(httpParams).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                Type type = new TypeToken<BaseBean>() {
                }.getType();
                BaseBean baseBean = GsonHelper.gson.fromJson(response.body(), type);

                //返回码为成功时的处理
                if (baseBean.isSuccess()) {
                    //获取解析后的的数据为getData里面的数据
                    ToastUtil.Show(baseBean.getMsg());
                } else {
                    ToastUtil.Show(baseBean.getMsg());
                }
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                //显示loading框
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtil.Show(response.message());
            }
        });
    }
}
