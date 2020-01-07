package com.bailitop.gallery.constant

object GalleryConstant {

    /** 请求码：进入预览页面 */
    const val REQUEST_CODE_PREVIEW = 0x11
    /** 结果码：预览选择结果 */
    const val RESULT_CODE_PREVIEW = 0x22

    /** 请求码：进入相册页面 */
    const val REQUEST_CODE_GALLERY = 0x33
    /** 结果码：相册选择结果 */
    const val RESULT_CODE_GALLERY = 0x44

    /** 请求码：进入拍照页面 */
    const val REQUEST_CODE_TAKE_PHOTO = 0x55
    /** 结果码：拍照结果 */
    const val RESULT_CODE_TAKE_PHOTO = 0x66

    /**
     * Key 值：选中的列表
     */
    const val KEY_SELECT_LIST = "key_select_list"

    /**
     * key 值：当前相册图片列表
     */
    const val KEY_GALLERY_LIST = "key_gallery_list"

    /**
     * Key 值：进入预览页 点击的图片的位置
     */
    const val KEY_CURR_POSITION = "key_curr_position"

    /**
     * Key 值：在预览页面选择了确定按钮，需要销毁相册页面（GalleryActivity）
     */
    const val KEY_IS_DONE_FINISH = "key_is_done"

    /**
     * Key 值：选中的列表是否发生了变化，以此判断是否需要更新
     */
    const val KEY_IS_SELECT_LIST_CHANGE = "key_is_select_list_change"


}