package com.appguard.observer

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.appguard.observer.model.AppInfo
import java.security.MessageDigest

class AppInfoExtractor(private val context: Context) {

    fun extract(packageName: String): AppInfo? {
        val packageManager = context.packageManager
        return try {
            val packageInfo = getPackageInfo(packageManager, packageName)
            val appLabel = packageInfo.applicationInfo?.let {
                packageManager.getApplicationLabel(it).toString()
            } ?: packageName

            val signatureBytes = getSignatureBytes(packageInfo)

            AppInfo(
                packageName = packageName,
                appName = appLabel,
                versionName = packageInfo.versionName,
                installerSource = getInstallerSource(packageManager, packageName),
                signatureHashSha256 = signatureBytes?.let { hashBytes(it, "SHA-256") },
                signatureHashMd5 = signatureBytes?.let { hashBytes(it, "MD5") }
            )
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("AppInfoExtractor", "Paket bulunamadi: $packageName", e)
            null
        }
    }

    private fun getPackageInfo(packageManager: PackageManager, packageName: String): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }
    }

    private fun getInstallerSource(packageManager: PackageManager, packageName: String): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                packageManager.getInstallSourceInfo(packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                packageManager.getInstallerPackageName(packageName)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getSignatureBytes(packageInfo: PackageInfo): ByteArray? {
        val signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo = packageInfo.signingInfo ?: return null

            val signatures = if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners
            } else {
                signingInfo.signingCertificateHistory
            }

            signatures.firstOrNull()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.signatures?.firstOrNull()
        } ?: return null

        return signature.toByteArray()
    }

    private fun hashBytes(bytes: ByteArray, algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val hashBytes = digest.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}