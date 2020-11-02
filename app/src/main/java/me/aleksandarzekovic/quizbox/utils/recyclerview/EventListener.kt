package me.aleksandarzekovic.quizbox.utils.recyclerview

interface EventListener<T> {
    fun onItemClick(t: T)
}