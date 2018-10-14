package com.example.vicky.yinji.base;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    private static ActivityManager mInstance = null;
    //采用弱引用，若activity被回收，弱引用指针返回空
    private List<WeakReference<BaseAppCompatActivity>> activityList = new ArrayList<WeakReference<BaseAppCompatActivity>>();

    public static ActivityManager getInstance() {
        if (mInstance == null) {
            mInstance = new ActivityManager();
        }
        return mInstance;
    }

    void addActivity(BaseAppCompatActivity activity) {
        if (activityList == null || activity == null)
            return;

        boolean needDoClean = false;
        boolean found = false;
        for(WeakReference<BaseAppCompatActivity> weakPtr:activityList) {
            BaseAppCompatActivity act = weakPtr.get();
            if(act == null) {
                needDoClean = true;
            } else if(act == activity) {
                found = true;//already in list, should not add again
                break;
            }
        }
        if(needDoClean) {
            recycleEmpty();
        }
        if(!found) {
            activityList.add(new WeakReference<BaseAppCompatActivity>(activity));
        }
    }

    /**
     * 移除activity
     * 在BaseFragmentActivity自动调用，程序中不应该调用此方法
     * 如果有多个同一class的Activity实例，则移除最近压入的那个
     *
     * @param activity
     */
    void removeActivity(BaseAppCompatActivity activity) {
        boolean needToClean = false;
        WeakReference<BaseAppCompatActivity> wrf;
        for (int i = activityList.size() - 1; i >= 0; i--) {
            wrf = activityList.get(i);
            BaseAppCompatActivity act = wrf.get();
            if (act == null) {//判断是否为失效的弱引用
                needToClean = true;
            } else if (act == activity) {
                activityList.remove(wrf);//找到匹配并移除
                break;//important!! 如果不break掉,由于list执行了remove操作，会导致concurrent exception
            }
        }
        if (needToClean) {
            recycleEmpty();
        }
    }

    /**
     * 在列表中删除已经失效的弱索引
     */
    private void recycleEmpty() {
        boolean needToClean = true;
        do {
            needToClean = false;//记得设置，否则会变成死循环
            for (WeakReference<BaseAppCompatActivity> wrf : activityList) {
                BaseAppCompatActivity act = wrf.get();
                if (act == null) {
                    needToClean = true;
                    activityList.remove(act);
                    break;
                }
            }
        } while (needToClean);
    }

    /**
     * 判断activity是否可见
     *
     * @param cls
     * @return
     */
//    public boolean isActivityVisible(Class<? extends BaseAppCompatActivity> cls) {
//        for (WeakReference<BaseAppCompatActivity> wrf : activityList) {
//            BaseAppCompatActivity act = wrf.get();
//            if (act != null) {
//                String actName = act.getClass().getName();
//                if (actName.equals(cls.getName())) {
//                    return act.isActivityVisible();
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 获取栈顶activity
     *
     * @return
     */
    public BaseAppCompatActivity getTopAty() {
        WeakReference<BaseAppCompatActivity> weakPtr;
        int size = activityList.size();
        //如果app运行在前台,通常最后加进去的Activity都是当前可见的Activity,因此逆序可以加快命中速度
        for (int i = size - 1; i >= 0; i--) {
            weakPtr = activityList.get(i);
            BaseAppCompatActivity act = weakPtr.get();
            if (act != null) {
                return act;
            }
        }
        return null;
    }

    /**
     * 判断app是否在前台运行
     *
     * @return
     */
//    public boolean isAppInFront() {
//        WeakReference<BaseAppCompatActivity> weakPtr;
//        int size = activityList.size();
//        //如果app运行在前台,通常最后加进去的Activity都是当前可见的Activity,因此逆序可以加快命中速度
//        for (int i = size - 1; i >= 0; i--) {
//            weakPtr = activityList.get(i);
//            BaseAppCompatActivity act = weakPtr.get();
//            if (act != null) {
//                if (act.isActivityVisible()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 结束指定Activity，如果有多个同一class的Activity实例，则结束最近打开的那个
     *
     * @param cls
     */
    public void finishActivity(Class<? extends BaseAppCompatActivity> cls) {
        int size = activityList.size();
        //逆序以结束最近push的一个activity实例
        for (int i = size - 1; i >= 0; i--) {
            BaseAppCompatActivity act = activityList.get(i).get();
            if (act != null) {
                String actName = act.getClass().getName();
                if (actName.equals(cls.getName())) {
                    act.finish();
                    break;
                }
            }
        }
    }

    /**
     * 退出所有的activity
     */
    public void exitApplication() {
        //若在非UI中调用,UI线程和当前线程可能并发交错执行，activity finish后会在UI线程执行onDestroy，进而执行removeActivity,
        //此时listActivity列表内容会发生变化,使用for循环可能出现concurrent exception
        //另一种情况: 若finish同步执行到onDestroy,也会执行到removeActivity，此时也可能导致for循环concurrent exception
        List<WeakReference<BaseAppCompatActivity>> lists = new ArrayList<WeakReference<BaseAppCompatActivity>>();
        lists.addAll(activityList);
        for (WeakReference<BaseAppCompatActivity> weakPtr : lists) {
            BaseAppCompatActivity a = weakPtr.get();
            if (a != null) {
                a.finish();
            }
        }
    }

    /**
     * 结束当前activity之前的所有activity
     */
    public void finishAtyBeforeThis() {
        for (int i = activityList.size() - 2; i >= 0; i--) {
            BaseAppCompatActivity a = activityList.get(i).get();
            if (a != null) {
                a.finish();
            }
        }
    }
}
