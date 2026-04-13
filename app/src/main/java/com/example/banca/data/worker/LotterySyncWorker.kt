package com.example.banca.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.remote.LotteryRemoteDataSource
import com.example.banca.data.repository.LotteryResultsRepository

class LotterySyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val db = DatabaseProvider.getDatabase(applicationContext)

            val repository = LotteryResultsRepository(
                dao = db.resultDao(),
                remoteDataSource = LotteryRemoteDataSource()
            )

            repository.syncLatestResults("pick3")
            repository.syncLatestResults("pick4")

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}