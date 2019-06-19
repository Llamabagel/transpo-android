/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.search.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText

class CustomSearchView : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(context, attrs, defStyleAttrs)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
            this.clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }
}