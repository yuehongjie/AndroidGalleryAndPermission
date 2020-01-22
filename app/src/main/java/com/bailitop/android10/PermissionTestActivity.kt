package com.bailitop.android10

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bailitop.android10.util.toast
import com.bailitop.mpermission.runWithPermissions
import kotlinx.android.synthetic.main.layout_permission_test.*

class PermissionTestActivity : AppCompatActivity(), View.OnClickListener {

    companion object {

        private const val KEY_TEST_FRAGMENT = "key_test_fragment"
        fun start(activity: AppCompatActivity, testFragment: Boolean) {
            val intent = Intent(activity, PermissionTestActivity::class.java)
            intent.putExtra(KEY_TEST_FRAGMENT, testFragment)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val testFragment = intent.getBooleanExtra(KEY_TEST_FRAGMENT, false)

        if (testFragment) {
            toast("进入 fragment 测试")

            setContentView(R.layout.layout_empty)

            supportFragmentManager.beginTransaction()
                    .add(R.id.fmContainer, PermissionTestFragment())
                    .commitNow()


        }else {

            toast("进入 activity 测试")

            setContentView(R.layout.layout_permission_test)

            //单个权限申请测试
            btnSinglePermission.setOnClickListener(this)

            btnMultiPermissions.setOnClickListener(this)

        }


    }

    override fun onClick(v: View?) {
        when(v) {
            btnSinglePermission -> runWithPermissions(arrayOf(Manifest.permission.CAMERA)){
                toast("Activity 中获取单个权限成功")
            }

            btnMultiPermissions -> runWithPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
            ){
                toast("Activity 中获取多个权限成功")
            }
        }
    }
}
