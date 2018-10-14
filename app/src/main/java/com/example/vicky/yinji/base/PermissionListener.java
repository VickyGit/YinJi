package com.example.vicky.yinji.base;

import java.util.List;

/**
 * 权限Listner
 */
public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermission);
}
