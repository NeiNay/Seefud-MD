package com.seefud.seefud.view.authentication

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import com.seefud.seefud.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val fieldType = tag?.toString() ?: "other"
                val errorMessage: String? = when {
                    s.toString().isEmpty() -> when (fieldType) {
                        "email", "password", "other" -> context.getString(R.string.err_column)
                        else -> null
                    }

                    fieldType == "email" && !isValidEmail(s.toString()) -> context.getString(R.string.err_email)
                    fieldType == "password" && s.toString().length < 8 -> context.getString(R.string.err_pass)
                    else -> null
                }

                val parentLayout = parent.parent as? TextInputLayout
                parentLayout?.error = errorMessage
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}