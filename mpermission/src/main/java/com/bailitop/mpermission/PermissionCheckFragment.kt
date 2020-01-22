package com.bailitop.mpermission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class PermissionCheckFragment: Fragment() {

    private lateinit var mPermissionOptions: MPermissionOptions
    private lateinit var mCallback: () -> Unit

    companion object {
        private const val PERMISSION_REQUEST_CODE = 0x123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true //保存实例
    }

     fun requestPermissions(permissions: Array<String>, options: MPermissionOptions, callback: ()->Unit){
         // 其他的配置
         mPermissionOptions = options
         // 同意权限的回调
         mCallback = callback
         // 调用系统方法 开始请求权限
         requestPermissions(permissions, PERMISSION_REQUEST_CODE)
     }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSION_REQUEST_CODE) return

        log("处理权限请求结果回调")

        handlePermissionsResult(permissions, grantResults)

    }

    /**
     * 处理权限申请结果
     */
    private fun handlePermissionsResult(permissions: Array<out String>, grantResults: IntArray){

        //1.首先，判断是否授予全部的权限
        if (isAllPermissionsGranted(grantResults)) {
            log("授予全部申请的权限")
            mCallback()
            return
        }

        //2.其次，判断被拒绝，且不再询问的权限（需要弹窗去设置页面）
        val neverAskAgainPermissions = getNeverAskAgainPermissions(permissions, grantResults)
        if(neverAskAgainPermissions.isNotEmpty() && mPermissionOptions.handleNeverAskAgain) {
            log("权限 ${neverAskAgainPermissions.toList()} 被拒绝，且不再询问")

            // 使用自定义的方法处理不再询问
            if (mPermissionOptions.customNeverAskAgainMethod != null) {
                mPermissionOptions.customNeverAskAgainMethod?.invoke(neverAskAgainPermissions)
            }else {
                //使用默认的处理方法
                defaultNeverAskAgainMethod()
            }

            return
        }

        //3.最后，再判断被拒绝，但允许再次申请的权限（需要提示用户：吐司也好、弹窗也行等）
        val deniedPermissions = getDeniedPermissions(permissions, grantResults)
        if (deniedPermissions.isNotEmpty() && mPermissionOptions.handleRational) {
            log("权限 ${deniedPermissions.toList()} 被拒绝，但允许再次申请，可以提示用户")

            //需要提示用户请求权限的理由
            if (mPermissionOptions.customRationalMethod != null) {
                mPermissionOptions.customRationalMethod?.invoke(deniedPermissions)
            }else {
                //使用默认的处理方法
                defaultRationalMethod()
            }

            return
        }

    }

    /**
     * 提供默认的权限被拒绝后，提示用户的方法
     */
    private fun defaultRationalMethod(){

        log("提示用户需要权限的理由")

        activity?.run {
            Toast.makeText(applicationContext, mPermissionOptions.rationalMessage, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 提供默认的处理不再询问的方法
     */
    private fun defaultNeverAskAgainMethod(){

        log("弹窗去设置页面，让用户修改权限")

        activity?.run {
            AlertDialog.Builder(this)
                .setTitle("授权请求")
                .setMessage(mPermissionOptions.neverAskAgainMessage)
                .setPositiveButton("去设置") { _, _ ->
                    go2AppSetting()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    /**
     * 打开应用设置页面
     */
    private fun go2AppSetting(){
        val settingIntent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        settingIntent.data = Uri.parse("package:${activity?.packageName}")
        startActivity(settingIntent)
    }


    /**
     * 判断是否授予了所有权限
     */
    private fun isAllPermissionsGranted(grantResults: IntArray): Boolean = grantResults.all { it == PackageManager.PERMISSION_GRANTED }



    /**
     * 获取被拒绝 且不再询问的权限
     */
    private fun getNeverAskAgainPermissions(permissions: Array<out String>, grantResults: IntArray): Array<String> {
        return permissions.filterIndexed { index, permission ->
            //被拒绝 且 不再提示用户
            grantResults[index] == PackageManager.PERMISSION_DENIED && !shouldShowRequestPermissionRationale(permission)
        }.toTypedArray()
    }


    /**
     * 获取被拒绝的权限
     */
    private fun getDeniedPermissions(permissions: Array<out String>, grantResults: IntArray): Array<String> {
        return permissions.filterIndexed { index, _ ->
            grantResults[index] == PackageManager.PERMISSION_DENIED
        }.toTypedArray()
    }

    private fun log(msg: String) {
        Log.d("PermissionCheckFragment", msg)
    }
}