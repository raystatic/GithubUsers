package com.example.githubusers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.githubusers.R
import com.example.githubusers.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewmodel by viewModels<GithubUserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        subscribeToObservers()

        viewmodel.getUserByUserName("raystatic")

    }

    private fun initViews() {

    }

    private fun subscribeToObservers() {
        viewmodel.githubUserResponse.observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(this, "${it.data?.name}", Toast.LENGTH_SHORT).show()
                }

                Status.ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {

                }

                Status.NONE -> {

                }
            }
        }
    }
}