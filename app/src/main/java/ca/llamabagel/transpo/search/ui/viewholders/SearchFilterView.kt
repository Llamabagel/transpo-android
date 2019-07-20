package ca.llamabagel.transpo.search.ui.viewholders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ca.llamabagel.transpo.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

data class Filter(val id: String, val text: String = "", var isOn: Boolean = false)

class SearchFilterView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.search_filter_view, this)
    }

    private val chipGroup: ChipGroup = findViewById(R.id.chip_group)
    private val filterList: MutableList<Chip> = mutableListOf()
    private var chipClickListener: (Filter) -> Any = {}

    fun addFilter(vararg filters: Filter) {
        filters.forEach { filter ->
            val chip = Chip(context).apply {
                text = filter.text
                tag = filter.id
                isCheckable = true
                isChecked = filter.isOn

                setOnClickListener {
                    chipClickListener(filter.copy(isOn = (it as Chip).isChecked))
                }
            }

            chipGroup.addView(chip)
            filterList.add(chip)
        }
    }

    fun setOnClickListener(click: (Filter) -> Any) {
        chipClickListener = click
    }
}