package com.appguard.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PackageInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_PACKAGE_ADDED) {
            return
        }

        val packageName = intent.data?.schemeSpecificPart ?: return

        val isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
        if (isReplacing) {
            return
        }

        val extractor = AppInfoExtractor(context)
        val appInfo = extractor.extract(packageName)

        if (appInfo != null) {
            Log.d("PackageInstallReceiver", "Yeni uygulama kuruldu: ${appInfo.appName}")
            Log.d("PackageInstallReceiver", "Paket adi: ${appInfo.packageName}")
            Log.d("PackageInstallReceiver", "Versiyon: ${appInfo.versionName}")
            Log.d("PackageInstallReceiver", "Kaynak: ${appInfo.installerSource}")
            Log.d("PackageInstallReceiver", "Imza Hash (SHA-256): ${appInfo.signatureHash}")
        } else {
            Log.e("PackageInstallReceiver", "Uygulama bilgisi cikarilamadi: $packageName")
        }
    }
}