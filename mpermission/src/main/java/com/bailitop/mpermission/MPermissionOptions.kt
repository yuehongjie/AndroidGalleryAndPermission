package com.bailitop.mpermission

data class MPermissionOptions(
    /** 是否处理被拒绝，但需要提示用户理由的权限 */
    var handleRational: Boolean = true,
    /** 提示用户的理由 */
    var rationalMessage: String = "需要您同意权限申请，才能正常使用相关功能",

    /** 是否处理被拒绝 且不再询问的权限（去设置页面） */
    var handleNeverAskAgain: Boolean = true,
    /** 不再询问的权限，需要提示用户去设置页面的理由 */
    var neverAskAgainMessage: String = "需要您到应用设置中授予权限，才能正常使用相关功能",

    /**
     * 使用者自定义 提示用户需要权限的理由
     */
    var customRationalMethod: ((deniedPermissions: Array<String>) -> Unit)? = null,

    /**
     * 使用者自定义 不再询问的权限的处理方法
     */
    var customNeverAskAgainMethod: ((neverAskAgainPermissions: Array<String>) -> Unit)? = null
)