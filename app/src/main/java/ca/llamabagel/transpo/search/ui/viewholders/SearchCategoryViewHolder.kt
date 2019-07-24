/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchCategoryBinding

const val SEARCH_CATEGORY_HEADER_LAYOUT = R.layout.search_category

class SearchCategoryViewHolder(
    private val binding: SearchCategoryBinding,
    private val searchResultClickListener: (SearchResult) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var categoryHeader: CategoryHeader

    inner class Handler {
        fun onClick() = searchResultClickListener(categoryHeader)
    }

    fun bind(header: CategoryHeader) {
        categoryHeader = header
        binding.category = header
        binding.handler = Handler()
    }
}