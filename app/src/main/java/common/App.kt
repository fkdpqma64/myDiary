package common

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

data class Link(val uri: Uri)

@Singleton
class App @Inject constructor(context: Context) {
    init {
    }

    private var mBackupLink: Link? = null

    fun backupMainIntent(intent: Intent?): Boolean {
        if(mBackupLink != null) {
            return true
        }
        if (intent == null) return false
        val uri = intent.data ?: return false
        mBackupLink = Link(uri = uri)
        return true
    }

    fun getAndRemoveBackupLink(): Link? {
        val link = mBackupLink
        mBackupLink = null
        return link
    }
}
