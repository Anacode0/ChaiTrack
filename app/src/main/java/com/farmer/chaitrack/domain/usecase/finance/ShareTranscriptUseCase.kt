package com.farmer.chaitrack.domain.usecase.finance

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class ShareTranscriptUseCase @Inject constructor() {

    operator fun invoke(context: Context, pdfFile: File): Flow<Result<Boolean>> = flow {
        try {
            val uri = Uri.fromFile(pdfFile)
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "application/pdf"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share Transcript with Bank"))
            emit(Result.Success(true))
        } catch (e: Exception) {
            emit(Result.Error("Failed to share transcript: ${e.message}"))
        }
    }
}