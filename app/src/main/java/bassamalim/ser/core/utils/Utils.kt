package bassamalim.ser.core.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

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

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun writeToDownloads(filename: String, bytes: ByteArray) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            filename
        )
        val outputStream = FileOutputStream(file)
        outputStream.write(bytes)
        outputStream.close()
    }

}