package com.seefud.seefud.view.content.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.seefud.seefud.R
import com.seefud.seefud.data.UserRepository
import com.seefud.seefud.data.pref.UserPreference
import com.seefud.seefud.data.pref.dataStore
import com.seefud.seefud.view.authentication.welcome.WelcomeActivity
import com.seefud.seefud.view.content.ViewModelFactory

class SplashFragment : Fragment() {

    private lateinit var splashViewModel: SplashViewModel
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the dataStore and pass it to the UserPreference
        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        // Initialize the repository with the UserPreference instance
        userRepository = UserRepository.getInstance(userPreference)

        splashViewModel = ViewModelProvider(
            this, ViewModelFactory(userRepository)
        )[SplashViewModel::class.java]

        splashViewModel.userSession.observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                // Navigate to HomeFragment if the user is logged in
                findNavController().navigate(R.id.action_splashFragment_to_navigation_home)
            } else {
                // Navigate to WelcomeActivity if the user is not logged in
                startActivity(Intent(requireContext(), WelcomeActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}