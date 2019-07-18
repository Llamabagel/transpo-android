package ca.llamabagel.transpo.search.ui.viewholders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ca.llamabagel.transpo.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

data class Filter(val id: String, val text: String, var isOn: Boolean = false)

class SearchFilterView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.search_filter_view, this)
    }

    private val clearButton: Chip = findViewById(R.id.clear_filter_chip)
    private val chipGroup: ChipGroup = findViewById(R.id.chip_group)
    private val filterList: MutableList<Chip> = mutableListOf()
    private var chipClickListener: (List<Filter>) -> Any = {}

    fun addFilter(vararg filters: Filter) {
        filters.forEach { filter ->
            val chip = Chip(context).apply {
                text = filter.text
                tag = filter.id
                isCheckable = true
                isChecked = filter.isOn

                setOnClickListener {
                    clearButton.visibility = if (filterList.any { it.isChecked }) View.VISIBLE else View.GONE
                    chipClickListener(filterList.map { Filter(it.tag.toString(), it.text.toString(), it.isChecked) })
                }
            }

            chipGroup.addView(chip)
            filterList.add(chip)
        }

        clearButton.setOnClickListener { clearButton ->
            filterList.forEach { it.isChecked = false }
            clearButton.visibility = View.GONE

            chipClickListener(filterList.map { Filter(it.tag.toString(), it.text.toString(), false) })
        }
    }

    fun setOnClickListener(click: (List<Filter>) -> Any) {
        chipClickListener = click
    }
}