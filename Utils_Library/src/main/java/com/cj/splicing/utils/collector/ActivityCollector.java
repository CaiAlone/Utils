package com.cj.splicing.utils.collector;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

//活动管理器

/**
 * Activity 活动管理器
 * Created by CaiJing on 2022/1/6
 */

public class ActivityCollector {
    public  static List<Activity> activities=new ArrayList<Activity>();

    //添加Activity到活动管理器
    public static  void addActivity(Activity activity){
        activities.add(activity);
    }

    //从活动管理器移除Activity
    public static  void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static  Activity getActivity(){
        return activities.get(activities.size()-1);
    }


    //销毁活动管理器中所有Activity
    public static  void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
