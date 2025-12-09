package com.farmer.chaitrack.domain.usecase.finance

import android.content.Context
import com.farmer.chaitrack.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SaveTranscriptLocallyUseCase @Inject constructor() {

    operator fun invoke(context: Context, pdfFile: File, fileName: String): Flow<Result<File>> = flow {
        try {
            val downloadsDir = context.getExternalFilesDir(null) ?: context.filesDir
            val destinationFile = File(downloadsDir, fileName)

            pdfFile.inputStream().use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }

            emit(Result.Success(destinationFile))
        } catch (e: Exception) {
            emit(Result.Error("Failed to save transcript: ${e.message}"))
        }
    }
}