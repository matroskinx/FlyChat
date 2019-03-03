package com.example.vladislav.flychat

interface BaseView<T> {
    fun setupPresenter(presenter: T)
}