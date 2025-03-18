package com.gotneb.lied.music_player.di

import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import com.gotneb.lied.music_player.data.local.MusicRepositoryImpl
import com.gotneb.lied.music_player.domain.local.MusicRepository
import com.gotneb.lied.music_player.presentation.music_list.MusicListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { androidContext().contentResolver }
    single {
        ExoPlayer
            .Builder(androidContext())
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .build()
    }
    singleOf(::MusicRepositoryImpl).bind<MusicRepository>()

    viewModelOf(::MusicListViewModel)
}