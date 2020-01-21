package com.bailitop.android10

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bailitop.mpermission.runWithPermissions
import kotlinx.android.synthetic.main.activity_permission_test.*

class PermissionTestActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_test)

        //单个权限申请测试
        btnSinglePermission.setOnClickListener(this)

        btnMultiPermissions.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v) {
            btnSinglePermission -> runWithPermissions(arrayOf(Manifest.permission.CAMERA)){

            }

            btnMultiPermissions -> runWithPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            ){}
        }
    }
}
