package com.cj.splicing.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cj.splicing.R;
import com.cj.splicing.bmob.UserModel;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.toast.ToastUtil;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 设置
 * Created By CaiJing On 2022/11/7
 */
public class SetActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        darkImmerseFontColor();
        tv_title.setText("设置");
    }
    @Override
    public int addContentView() {
        return R.layout.activity_set;
    }

    MessageDialog messageDialog;
    @OnClick({R.id.iv_quit,R.id.tv_about,R.id.tv_admin,R.id.tv_login,R.id.tv_zx,R.id.tv_xyBtn,R.id.tv_ysBtn})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.iv_quit://
                finish();
                break;
            case R.id.tv_about://关于我们
                intent=new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_admin://在线客服
                /**
                 * 启动单聊界面。
                 *
                 * @param context      应用上下文。
                 * @param targetUserId 要与之聊天的用户 Id。
                 * @param title        聊天的标题，开发者需要在聊天界面通过 intent.getData().getQueryParameter("title")
                 *                     获取该值, 再手动设置为聊天界面的标题。
                 */
                RongIM.getInstance().refreshUserInfoCache(new UserInfo("admin","在线客服", Uri.parse("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0903%252Fbf50999dj00qyutom000fd200dw00eng00dw00en.jpg%26thumbnail%3D660x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1670638320&t=ecbe4dfbcb56143b7c5fe927d8129458")));
                RongIM.getInstance ().startConversation (this, Conversation.ConversationType.PRIVATE,
                        "admin", "在线客服");
                /**
                 * 设置消息体内是否携带用户信息。
                 *
                 * @param state 是否携带用户信息，true 携带，false 不携带。
                 */
                RongIM.getInstance().setMessageAttachedUserInfo(true);
                break;
            case R.id.tv_login://退出登录
                RongIM.getInstance().disconnect();
                Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",false);
                intent=new Intent(getApplicationContext(),LoginCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_zx://注销
                messageDialog=MessageDialog.show("提示","当前正在进行账号注销，账号注销后将在180天内无法重新注册。\n如您确认请点击确认按钮。","确认","取消");
                messageDialog.setCancelable(false);
                messageDialog.setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog dialog, View v) {
                        editInfo("status","false");
                        dialog.dismiss();
                        return false;
                    }
                });
                messageDialog.setCancelButton(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog dialog, View v) {
                        dialog.dismiss();
                        return false;
                    }
                });
                break;
            case R.id.tv_xyBtn://用户协议
                startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/用户协议.html"));
                break;
            case R.id.tv_ysBtn://隐私政策
                startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/隐私政策.html"));
                break;
            default:
                break;
        }
    }



    /**
     * 修改资料
     */
    UserModel model_edit;
    private void editInfo(String key,String value){
        model_edit=new UserModel();
        model_edit.editUserInfo(key,value);
        LoadingShow("正在注销...");
        model_edit.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    ToastUtil.Show("注销成功!");
                    RongIM.getInstance().disconnect();
                    Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",false);
                    startActivity(new Intent(getApplicationContext(),LoginCodeActivity.class));
                }
                LoadingStop();
            }
        });
    }
}