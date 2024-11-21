package com.seefud.seefud.view.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
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
import com.seefud.seefud.data.pref.UserModel
import com.seefud.seefud.databinding.ActivityLoginBinding
import com.seefud.seefud.view.MainActivity
import com.seefud.seefud.view.authentication.AuthViewModelFactory
import kotlinx.coroutines.launch
import com.seefud.seefud.data.Result


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val factory: AuthViewModelFactory = AuthViewModelFactory.getInstanceUser(this)
    private val viewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length >= 8) {
                binding.buttonError.visibility = View.GONE

                lifecycleScope.launch {
                    viewModel.login(email, pass).observe(this@LoginActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }

                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE

                                    viewModel.saveSession(UserModel(email, result.data.token!!))

                                    AlertDialog.Builder(this@LoginActivity).apply {
                                        setTitle(getString(R.string.ad_title))
                                        setMessage(result.data.name)
                                        setPositiveButton(getString(R.string.ad_button)) { _, _ ->
                                            val intent = Intent(context, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }

                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    AlertDialog.Builder(this@LoginActivity).apply {
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
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}