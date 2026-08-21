package com.appguard.observer

import android.content.Context
import android.content.Intent
import com.appguard.observer.model.AppInfo

object IpcSender {

    private const val ACTION_APP_DATA = "com.appguard.ACTION_APP_DATA"
    private const val TARGET_PACKAGE = "com.appguard.guard"


    const val EXTRA_PACKAGE_NAME = "extra_package_name"
    const val EXTRA_APP_NAME = "extra_app_name"
    const val EXTRA_VERSION_NAME = "extra_version_name"
    const val EXTRA_INSTALLER_SOURCE = "extra_installer_source"
    const val EXTRA_SHA256 = "extra_sha256"
    const val EXTRA_MD5 = "extra_md5"

    fun send(context: Context, appInfo: AppInfo) {
        val intent = Intent(ACTION_APP_DATA).apply {
            setClassName(TARGET_PACKAGE, "$TARGET_PACKAGE.DataReceiver")
            putExtra(EXTRA_PACKAGE_NAME, appInfo.packageName)
            putExtra(EXTRA_APP_NAME, appInfo.appName)
            putExtra(EXTRA_VERSION_NAME, appInfo.versionName)
            putExtra(EXTRA_INSTALLER_SOURCE, appInfo.installerSource)
            putExtra(EXTRA_SHA256, appInfo.signatureHashSha256)
            putExtra(EXTRA_MD5, appInfo.signatureHashMd5)
        }
        context.sendBroadcast(intent)
    }
}