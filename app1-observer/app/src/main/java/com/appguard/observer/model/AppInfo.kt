package com.appguard.observer.model

data class AppInfo(
    val packageName: String,
    val appName: String,
    val versionName: String?,
    val installerSource: String?,
    val signatureHashSha256: String?,
    val signatureHashMd5: String?
)