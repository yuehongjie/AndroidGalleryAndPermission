package com.bailitop.mpermission

data class MPermissionOptions(
    var handleRational: Boolean = true,
    var rationalMessage: String = "",
    var handlePermissionDenied: Boolean = true,
    var neverAskAgainMessage: String = ""
)