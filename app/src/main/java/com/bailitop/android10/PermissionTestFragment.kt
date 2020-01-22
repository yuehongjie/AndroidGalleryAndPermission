package com.bailitop.android10

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bailitop.android10.util.toast
import com.bailitop.mpermission.MPermissionOptions
import com.bailitop.mpermission.runWithPermissions
import kotlinx.android.synthetic.main.layout_permission_test.*

class PermissionTestFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.layout_permission_test, null)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //单个权限申请测试
        btnSinglePermission.setOnClickListener(this)

        btnMultiPermissions.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            btnSinglePermission -> runWithPermissions(
                arrayOf(Manifest.permission.CAMERA),
                MPermissionOptions(
                    customNeverAskAgainMethod = {
                        neverAskAgainPermissions ->
                        log("拒绝且不再询问的权限：${neverAskAgainPermissions.toList()}")
                        toast("自定义 neverAskAgainPermissions 处理方法")
                    },
                    customRationalMethod = {
                        log("提示用户需要权限的理由：${it.toList()}")
                        toast("自定义 shouldRational 处理方法")
                    }
                )
            ){
                toast("Fragment 中获取单个权限成功")
            }

            btnMultiPermissions -> runWithPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            ){
                toast("Fragment 中获取多个权限成功")
            }
        }
    }


    private fun log(msg: String) {
        Log.d("MPermission", msg)
    }
}