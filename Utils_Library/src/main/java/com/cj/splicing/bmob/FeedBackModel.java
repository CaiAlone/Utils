package com.cj.splicing.bmob;



import com.cj.splicing.bean.FeedBack;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 反馈数据处理
 * Created By CaiJing On 2022/20/27
 */
public class FeedBackModel extends BaseModel{


    /**
     * 反馈提交
     * @param feedBack
     */
    public void submit(FeedBack feedBack){
        feedBack.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    request.isSucceed(true);//反馈提交成功
                }
                else {
                    request.isSucceed(false);//反馈提交失败
                }
            }
        });
    }
}
