package com.upsaclay.news

import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.presentation.announcement.createannouncement.CreateAnnouncementViewModel
import com.upsaclay.news.presentation.announcement.editannouncement.EditAnnouncementViewModel
import com.upsaclay.news.presentation.announcement.readannouncement.ReadAnnouncementViewModel
import com.upsaclay.news.presentation.news.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val newsModule = module {
    viewModelOf(::NewsViewModel)
    viewModelOf(::CreateAnnouncementViewModel)
    viewModel { (announcementId: String) ->
        ReadAnnouncementViewModel(
            announcementId = announcementId,
            deleteAnnouncementUseCase = get(),
            userRepository = get(),
            announcementRepository = get()
        )
    }
    viewModel { (announcement: Announcement) ->
        EditAnnouncementViewModel(
            announcement = announcement,
            updateAnnouncementUseCase = get()
        )
    }
}