package com.bailitop.gallery.scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

/**
 * 拍照完成后，扫描文件，否则在“自定义的相册”中不显示该照片
 * 销毁时记得调用 disconnect()
 */
class SingleMediaScanner (
    context: Context,
    private val path: String,
    private val listener: SingleScannerListener?
): MediaScannerConnection.MediaScannerConnectionClient {

    interface SingleScannerListener{
        fun onScanStart()
        fun onScanCompleted(path: String?, uri: Uri?)
    }

    private val connection: MediaScannerConnection = MediaScannerConnection(context.applicationContext, this)

    init {
        connection.connect()
        listener?.onScanStart()
    }
    override fun onMediaScannerConnected() {
        connection.scanFile(path, null)
    }

    override fun onScanCompleted(path: String?, uri: Uri?) {
        disconnect()
        listener?.onScanCompleted(path, uri)
    }

    fun disconnect() {
        connection.disconnect()
    }

}