package com.example.githubusers.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.githubusers.databinding.ActivityMainBinding
import com.example.githubusers.domain.models.GithubUser
import com.example.githubusers.utils.Constants
import com.example.githubusers.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewmodel by viewModels<GithubUserViewModel>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        subscribeToObservers()

    }

    private fun initViews() {
        binding.textInputLayout.setEndIconOnClickListener {
            this.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            val userName = binding.editText.text?.toString()?.trim()?.lowercase()
            if (userName.isNullOrEmpty().not()) {
                viewmodel.getUserByUserName(userName)
            }
        }
    }

    private fun showLoader() {
        binding.loader.isVisible = true
        binding.textLoadingError.isVisible = true
        binding.textLoadingError.text = Constants.FINDING_USER
        binding.cardUser.isVisible = false
    }

    private fun showError() {
        binding.loader.isVisible = false
        binding.textLoadingError.isVisible = true
        binding.textLoadingError.text = Constants.USER_NOT_FOUND
        binding.cardUser.isVisible = false
    }

    private fun showUser(user: GithubUser) {
        binding.loader.isVisible =  false
        binding.textLoadingError.isVisible =  false
        binding.cardUser.isVisible = true

        Glide.with(this)
            .load(user.avatar_url)
            .into(binding.imageAvatar)

        val name = if (user.name.isNullOrEmpty().not()) {
            if (user.login.isNullOrEmpty().not()) {
                "${user.name} (@${user.login})"
            } else {
                "${user.name}"
            }
        } else {
            ""
        }
        binding.textName.text = name

        binding.textDescription.isVisible = user.bio.isNullOrEmpty().not()
        binding.textDescription.text = user.bio

        val followers = if (user.followers != null) {
            if (user.following != null) {
                "Followers: ${user.followers}    Following: ${user.following}"
            } else {
                "Followers: ${user.followers}"
            }
        } else {
            ""
        }

        binding.textFollowers.isVisible = followers.isNotEmpty()
        binding.textFollowers.text = followers

    }

    private fun subscribeToObservers() {
        viewmodel.githubUserResponse.observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                       showUser(it)
                    } ?: kotlin.run {
                        showError()
                    }
                }

                Status.ERROR -> {
                   showError()
                }

                Status.LOADING -> {
                    showLoader()
                }

                Status.NONE -> {
                    binding.loader.isVisible =  false
                    binding.textLoadingError.isVisible =  false
                    binding.cardUser.isVisible = false
                }
            }
        }
    }
}