package com.echoleaf.frame.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by dydyt on 2016/6/29.
 */
public class SystemUtils {

    /**
     * PUBLIC STATIC FUNCTION
     **/
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static NetworkType getNetworkType(Context context) {
        NetworkType netType = NetworkType.NONE;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NetworkType.CMNET;
                } else {
                    netType = NetworkType.CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NetworkType.WIFI;
        }
        return netType;
    }

    /**
     * 获取Mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取App安装包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 检测当前系统声音是否为正常模式
     *
     * @param context
     * @return
     */
    public static boolean isAudioNormal(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 应用程序是否发出提示音
     *
     * @param context
     * @return
     */
    public static boolean isAppSound(Context context) {
        return isAudioNormal(context);
    }

    /**
     * 退出应用程序
     */
    @TargetApi(3)
    public static void exit(Context context) {
        try {
            Application application = (Application) context.getApplicationContext();
            application.onTerminate();
        } catch (Exception e) {
        }
    }

    /**
     * 重启应用程序
     *
     * @param context
     */
    public static void restart(Context context) {
        try {
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 系统重启
     */
    public static void reboot() {
        try {
            String cmd = "su -c reboot";
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * root权限执行程序
     *
     * @param progs
     * @return
     */
    public static boolean rootExecuteProgram(String... progs) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            for (String prog : progs)
                os.writeBytes(prog + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static boolean executeProgram(String prog) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(prog);
            process.waitFor();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 写入系统级APK
     *
     * @param filePath    apk文件路径
     * @param newFileName 命名新的文件名
     * @return
     */
    public static boolean writeSystemApk(String filePath, String newFileName) {
        /*String rw = "mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system";
        String cat = String.format ( "cat %s > /system/app/%s", filePath, newFileName );
		String ro = "mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system";*/
        String rw = "mount -o remount,rw /system";
        String cat = String.format("cat %s > /system/app/%s", filePath, newFileName);
        String ro = "mount -o remount,ro /system";
        return rootExecuteProgram(rw, cat, ro);
    }

    /**
     * 强制停止一个应用程序
     *
     * @param packageName 应用程序包名
     */
    public static void forceStopApplication(String packageName) {
        try {
            String cmd = String.format("am force-stop %s", packageName);
            executeProgram(cmd);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * 判断Activity是否正在运行
     *
     * @param context
     * @param cls
     * @return
     */
    public static boolean activityRunning(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        if (cmpName != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断应用是否正在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean applicationRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : list) {
            String processName = appProcess.processName;
            if (processName != null && processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 网络类型
     */
    public enum NetworkType {
        /**
         * 没有网络
         */
        NONE(0),
        /**
         * wifi
         */
        WIFI(1),
        /**
         * wap网络
         */
        CMWAP(2),
        /**
         * net网络
         */
        CMNET(3);
        private int type;

        NetworkType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
