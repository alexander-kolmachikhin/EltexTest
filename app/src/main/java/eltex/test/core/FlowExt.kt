package eltex.test.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map

fun <T, R> Flow<List<T>>.mapItems(transform: (T) -> R) = map { list ->
    list.map { transform(it) }
}

fun <T> Flow<T>.dropFirstIf(predication: () -> Boolean) = drop(if (predication()) 1 else 0)