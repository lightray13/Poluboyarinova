package com.test.filmlist.util

import android.graphics.Color
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.test.filmlist.R

object SearchHelper {

    fun searchIntoList(view: View) {
        (view as? SearchView)?.apply {
            val editText = findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.setTextColor(Color.BLACK)
            editText.setHintTextColor(Color.GRAY)
            editText.hint = view.context.getString(R.string.search)
        }
    }
}