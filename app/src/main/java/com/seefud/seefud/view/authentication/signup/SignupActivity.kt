package com.seefud.seefud.view.authentication.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.seefud.seefud.R
import com.seefud.seefud.data.Result
import com.seefud.seefud.databinding.ActivitySignupBinding
import com.seefud.seefud.view.authentication.AuthViewModelFactory
import com.seefud.seefud.view.authentication.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        binding.signupButton.setOnClickListener {
            showLoading(true)
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            register(name, email, pass)
        }
    }

    private fun register(name: String, email: String, pass: String) {
        viewModel.register(name, email, pass).observe(this) { result ->
            showLoading(false)
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    lifecycleScope.launch {
                        Toast.makeText(
                            this@SignupActivity,
                            getString(R.string.success_register_msg),
                            Toast.LENGTH_LONG
                        ).show()

                        delay(2000)
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }

                is Result.Error -> {
                    showErrorDialog(result.error)
                }
            }
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(errorMessage)
            setPositiveButton(getString(R.string.retry)) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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