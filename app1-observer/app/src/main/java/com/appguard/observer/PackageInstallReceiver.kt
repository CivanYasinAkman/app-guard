package com.appguard.observer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PackageInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("PackageInstallReceiver", "TETİKLENDİ! action: ${intent.action}")

        val packageName = intent.data?.schemeSpecificPart ?: return

        val isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
        if (isReplacing) return

        Log.d("PackageInstallReceiver", "Yeni uygulama kuruldu: $packageName")
    }
}