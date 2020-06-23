package common.lib

import android.content.Context
import android.content.Intent
import com.example.mydairy.MainActivity

object Activities {

    fun startLaunchActivityFromSplash(context: Context) {
        val intent = MainActivity.createIntentFromSplash(context).also {
            it.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        or Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        }
        context.startActivity(intent)
    }

}
