package com.echoleaf.frame.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.echoleaf.frame.R;


/**
 * Created by dydyt on 2016/9/1.
 */
public class PermissionUtils {


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    public static boolean requestStoragePermissions(Activity activity) {
        return requestStoragePermissions(activity, activity.getString(R.string.request_permission_prompt));
    }

    public static boolean requestStoragePermissions(Activity activity, String msg) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            boolean allowRequestAgain = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (!allowRequestAgain) {
                ViewUtils.toastMessage(activity, msg);
            }
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }


    public static boolean requestStoragePermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            return grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

}
