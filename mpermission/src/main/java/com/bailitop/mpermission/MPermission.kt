package com.bailitop.mpermission

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

private val TAG = PermissionCheckFragment::class.java.simpleName

/**
 * 密封类，限定 target 只能是 Fragment 和 AppCompatActivity
 */
sealed class Target
data class ActivityTarget(val activity: AppCompatActivity): Target()
data class FragmentTarget(val fragment: Fragment): Target()

/**
 * 请求权限，并在获得权限后，执行相应的操作
 */
fun AppCompatActivity.runWithPermissions(
    permissions: Array<String>,
    options: MPermissionOptions = MPermissionOptions(),
    callback: () -> Unit
) = handlePermissions(ActivityTarget(this), permissions, options, callback)

/**
 * 请求权限，并在获得权限后，执行相应的操作
 */
fun Fragment.runWithPermissions(
    permissions: Array<String>,
    options: MPermissionOptions = MPermissionOptions(),
    callback: () -> Unit
) = handlePermissions(FragmentTarget(this), permissions, options, callback)


private fun handlePermissions(
    target: Target,
    permissions: Array<String>,
    options: MPermissionOptions,
    callback: () -> Unit
){

    log("准备请求权限")
    log("需要请求的权限有: ${permissions.toList()}")

    val context = when(target) {
        is ActivityTarget -> target.activity
        is FragmentTarget -> target.fragment.context ?: return
    }

    if (hasAllPermissions(context, permissions)) {
        log("已授权，无需重复申请")
        callback()
    }else {
        log("尚无权限，开始申请")

        val fm = when(target) {
            is ActivityTarget -> target.activity.supportFragmentManager
            is FragmentTarget -> target.fragment.childFragmentManager
        }

        var permissionCheckFragment = fm.findFragmentByTag(TAG) as PermissionCheckFragment?
        if (permissionCheckFragment == null) {
            log("create new PermissionCheckFragment")
            permissionCheckFragment = PermissionCheckFragment()
            fm.beginTransaction().add(permissionCheckFragment, TAG).commitNow()
        }

        permissionCheckFragment.requestPermissions(permissions, options, callback)

    }


}

/**
 * 是否已有全部申请的权限
 */
private fun hasAllPermissions(
    context: Context,
    permissions: Array<String>
): Boolean = permissions.all { hasPermission(context, it) }

/**
 * 是否已有单个申请的权限
 */
private fun hasPermission(
    context: Context,
    permission: String
): Boolean = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED


private fun log(msg: String) {
    Log.d("MPermission", msg)
}