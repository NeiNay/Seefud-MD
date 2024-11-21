package com.seefud.seefud.view.authentication.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.seefud.seefud.R
import com.seefud.seefud.databinding.ActivitySignupBinding
import com.seefud.seefud.view.authentication.AuthViewModelFactory
import kotlinx.coroutines.launch
import com.seefud.seefud.data.Result

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val factory: AuthViewModelFactory = AuthViewModelFactory.getInstanceUser(this)
    private val viewModel: SignupViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            if (name.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length >= 8){
                binding.buttonError.visibility = View.GONE

                lifecycleScope.launch {
                    viewModel.register(name, email, pass).observe(this@SignupActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this@SignupActivity).apply {
                                        setTitle(getString(R.string.ad_title))
                                        setMessage(result.data)
                                        setPositiveButton(getString(R.string.ad_button)) { _, _ ->
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this@SignupActivity).apply {
                                        setTitle(getString(R.string.ad_title))
                                        setMessage(result.error)
                                        setPositiveButton(getString(R.string.ad_button)) { _, _ ->
                                        }
                                        create()
                                        show()
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                binding.buttonError.visibility = View.VISIBLE
            }
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}