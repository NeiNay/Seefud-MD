package com.seefud.seefud.view.authentication

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.seefud.seefud.R

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener  {
    private var iconImage: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_24) as Drawable

    init {
        iconImage
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val fieldType = tag?.toString() ?: "other"

                error = when {
                    s.toString().isEmpty() -> when (fieldType) {
                        "email", "password", "other" -> "Kolom tidak boleh kosong"
                        else -> null
                    }

                    fieldType == "email" && !isValidEmail(s.toString()) -> context.getString(R.string.email_error)
                    fieldType == "password" && s.toString().length < 8 -> context.getString(R.string.password_error)
                    else -> null
                }
                setError(error, null)
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }
}