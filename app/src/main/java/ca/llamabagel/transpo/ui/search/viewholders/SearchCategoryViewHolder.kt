/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.ui.search.viewholders

import androidx.recyclerview.widget.RecyclerView
import ca.llamabagel.transpo.R
import ca.llamabagel.transpo.databinding.SearchCategoryBinding

const val SEARCH_CATEGORY_HEADER_LAYOUT = R.layout.search_category

class SearchCategoryViewHolder(private val binding: SearchCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(header: SearchResult.CategoryHeader) {
        binding.category = header
    }
}