/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import androidx.recyclerview.widget.DiffUtil

data class ListDiffResult<T : Any>(val list: List<T> = emptyList(), val diffResult: DiffUtil.DiffResult = EmptyResult)

private val EmptyResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = throw AssertionError()

    override fun getOldListSize(): Int = 0

    override fun getNewListSize(): Int = 0

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = throw AssertionError()
})