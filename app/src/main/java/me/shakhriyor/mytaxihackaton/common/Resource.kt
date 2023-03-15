package me.shakhriyor.mytaxihackaton.common

sealed class Resource<out T : Any> {
    object EMPTY : Resource<Nothing>()
    object LOADING : Resource<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : Resource<T>()
    data class ERROR(val error: String) : Resource<Nothing>()

}