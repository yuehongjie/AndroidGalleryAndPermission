package com.bailitop.mpermission

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

class PermissionCheckFragment: Fragment() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 0x123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true //保存实例
    }

     fun requestPermissions(permissions: Array<String>){
         //调用系统方法 开始请求权限
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

        //授予全部的权限
        if (isAllPermissionsGranted(grantResults)) {
            log("授予全部申请的权限")
        }else {

            //先判断 被拒绝，且不再询问的权限
            val neverAskAgainPermissions = getNeverAskAgainPermissions(permissions, grantResults)
            if(neverAskAgainPermissions.isNotEmpty()) {
                log("有权限被拒绝，且不再询问")

                return
            }

            //再判断被拒绝，但允许再次申请的权限
            val deniedPermissions = getDeniedPermissions(permissions, grantResults)
            if (deniedPermissions.isNotEmpty()) {
                log("有权限被拒绝，但允许再次申请，可以提示用户")

                return
            }
        }

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