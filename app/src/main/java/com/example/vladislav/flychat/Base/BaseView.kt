package com.example.vladislav.flychat.Base

interface BaseView<T> {
    fun setupPresenter(presenter: T)
}