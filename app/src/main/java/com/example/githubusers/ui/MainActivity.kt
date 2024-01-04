package com.example.githubusers.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

    private var userName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            userName = it.getString(Constants.KEY_USER_NAME)
        }

        initViews()

        subscribeToObservers()

    }

    private fun initViews() {
        if (userName.isNullOrEmpty().not()) {
            binding.textInputLayout.isVisible = false
            viewmodel.getUserByUserName(userName)

        } else {
            binding.textInputLayout.isVisible = true
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

    }

    private fun showLoader() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
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
            .into(binding.layoutUserData.imageAvatar)

        val name = if (user.name.isNullOrEmpty().not()) {
            if (user.login.isNullOrEmpty().not()) {
                "${user.name} (@${user.login})"
            } else {
                "${user.name}"
            }
        } else {
            ""
        }
        binding.layoutUserData.textName.text = name

        binding.layoutUserData.textDescription.isVisible = user.bio.isNullOrEmpty().not()
        binding.layoutUserData.textDescription.text = user.bio

        val followers = if (user.followers != null) {
            "Followers: ${user.followers}"
        } else {
            ""
        }

        binding.layoutUserData.textFollowers.isVisible = followers.isNotEmpty()
        binding.layoutUserData.textFollowers.text = followers

        val following = if (user.following != null) {
            "Following: ${user.following}"
        } else {
            ""
        }

        binding.layoutUserData.textFollowing.isVisible = following.isNotEmpty()
        binding.layoutUserData.textFollowing.text = following

        binding.layoutUserData.textFollowers.setOnClickListener {
            viewmodel.getFollowers(user.login)
        }

        binding.layoutUserData.textFollowing.setOnClickListener {
            viewmodel.getFollowings(user.login)
        }

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

        viewmodel.followers.observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.loader2.isVisible = false
                    it.data?.let {
                        val bottomSheet = UserListBottomSheet(
                            users = it,
                            title = "Followers"
                        ) {
                            navigateToProfile(it)
                        }
                        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    } ?: kotlin.run {
                        Toast.makeText(this, Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
                    }
                }

                Status.ERROR -> {
                    binding.loader2.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                    this.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    binding.loader2.isVisible = true
                }

                Status.NONE -> {
                    binding.loader2.isVisible = false
                }
            }
        }

        viewmodel.followings.observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.loader2.isVisible = false
                    it.data?.let {
                        val bottomSheet = UserListBottomSheet(
                            users = it,
                            title = "Following"
                        ) {
                            navigateToProfile(it)
                        }
                        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                    } ?: kotlin.run {
                        Toast.makeText(this, Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show()
                    }
                }

                Status.ERROR -> {
                    binding.loader2.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                    this.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                    binding.loader2.isVisible = true
                }

                Status.NONE -> {
                    binding.loader2.isVisible = false
                }
            }
        }
    }

    private fun navigateToProfile(userName: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(Constants.KEY_USER_NAME, userName)
        }
        startActivity(intent)
    }
}