package com.example.musicapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.musicapp.data.local.AppDatabase
import com.example.musicapp.data.local.entity.SongCategory
import com.example.musicapp.data.local.entity.SongEntity
import com.example.musicapp.data.local.entity.SongRemoteKey
import com.example.musicapp.data.local.mapper.SongMapper.toEntity
import com.example.musicapp.data.remote.MusicApi
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SongRemoteMediator(
    private val api: MusicApi,
    private val db: AppDatabase,
    private val category: SongCategory,
    private val order: String
): RemoteMediator<Int, SongEntity>() {



    override suspend fun initialize(): InitializeAction {
        val firstSong = db.songDao().getFirstSong(category)
        return if (firstSong != null && !isCacheExpired(firstSong.cachedAt)) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SongEntity>,
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }

                val remoteKey = db.songRemoteKeyDao().getKey(lastItem.id, category)
                if (remoteKey?.nextOffset == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
                remoteKey.nextOffset
            }
        }

        return try {
            val response = api.getSongs(
                order = order,
                limit = state.config.pageSize,
                offset = offset
            )
            val endOfPagination = response.results.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.songDao().deleteByCategory(category)
                    db.songRemoteKeyDao().clearByCategory(category)
                }
                val keys = response.results.map { song->
                    SongRemoteKey(
                        songId = song.id,
                        category = category,
                        prevOffset = if (offset == 0) null else offset - state.config.pageSize,
                        nextOffset = if (endOfPagination) null else offset + state.config.pageSize
                    )
                }
                db.songRemoteKeyDao().insertAll(keys)
                db.songDao().insertAll(
                    response.results.mapIndexed { index, song ->
                        song.toEntity(category).copy(position = offset + index)
                    }
                )
            }
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun isCacheExpired(cachedAt: Long): Boolean {
        val ttl = 15 * 60 * 1000L
        return System.currentTimeMillis() - cachedAt > ttl
    }
}