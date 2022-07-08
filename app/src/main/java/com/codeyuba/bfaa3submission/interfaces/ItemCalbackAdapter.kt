package com.codeyuba.bfaa3submission.interfaces

import android.view.View

interface ItemAdapterCallback<T> {
    fun onItemClicked(view: View?,data: T)
}