package eltex.test.core

sealed class ViewState<out DATA> {
    class Loading : ViewState<Nothing>()
    class Loaded<out DATA>(val data: DATA) : ViewState<DATA>()
}