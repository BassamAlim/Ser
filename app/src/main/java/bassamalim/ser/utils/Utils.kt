package bassamalim.ser.utils

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

object Utils {

    fun copyToClipboard(
        app: Application,
        text: String,
        label: String = "Text"
    ) {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(
            app,
            "$label copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

}