package com.example.githubusers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.R
import com.example.githubusers.databinding.FragmentUserListBinding
import com.example.githubusers.domain.models.GithubUser
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 05,January,2024
 */
class UserListBottomSheet(
    private val users: List<GithubUser>?,
    private val title: String?,
    private val onUserClicked: (String?) -> Unit
): BottomSheetDialogFragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding: FragmentUserListBinding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.bind(
            LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_user_list, container, false)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textTitle.text = title

        val userListAdapter = UserListAdapter {
            onUserClicked(it)
        }
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        userListAdapter.submitData(users ?: listOf())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}