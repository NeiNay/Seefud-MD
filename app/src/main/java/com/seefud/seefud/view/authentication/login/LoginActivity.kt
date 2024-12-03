package com.seefud.seefud.view.authentication.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.seefud.seefud.R
import com.seefud.seefud.data.Result
import com.seefud.seefud.data.pref.UserModel
import com.seefud.seefud.databinding.ActivityLoginBinding
import com.seefud.seefud.view.MainActivity
import com.seefud.seefud.view.authentication.AuthViewModelFactory


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
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
            showLoading(true)
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            login(email, pass)
        }
    }

    private fun login(email: String, pass: String) {
        viewModel.login(email, pass).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    saveSession(result.data.name!!, email, result.data.token)
                }

                is Result.Error -> {
                    showLoading(false)
                    showErrorDialog(result.error)
                }
            }
        }
    }

    private fun saveSession(name: String, email: String, token: String?) {
        val user = UserModel(name = name, email = email, token = token ?: "", isLogin = true)
        viewModel.saveSession(user)
        goToMain()
    }

    private fun goToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(errorMessage)
            setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
            create()
            show()
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