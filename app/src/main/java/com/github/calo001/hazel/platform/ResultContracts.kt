package com.github.calo001.hazel.platform

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract

class RecognizerContract: ActivityResultContract<Int, Uri?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak up")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode != Activity.RESULT_OK) {
            null
        } else {
            val result = intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            Uri.parse(result ?: "")
        }
    }

}
