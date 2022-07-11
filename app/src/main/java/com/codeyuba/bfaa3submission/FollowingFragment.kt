package com.codeyuba.bfaa3submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeyuba.bfaa3submission.adapter.UserListAdapter
import com.codeyuba.bfaa3submission.data.Result
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.databinding.FragmentConnectionBinding
import com.codeyuba.bfaa3submission.interfaces.ItemAdapterCallback
import com.codeyuba.bfaa3submission.viewmodel.UserViewModel
import com.codeyuba.bfaa3submission.viewmodel.ViewModelFactory

class FollowingFragment : Fragment() {
    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserWithFavStatusLiveData>()
    private lateinit var rvAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        rvAdapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvUserConnection.setHasFixedSize(true)
            rvUserConnection.layoutManager = LinearLayoutManager(context)
            rvUserConnection.adapter = rvAdapter
        }

        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserWithFavStatusLiveData> {
            override fun onItemClicked(view: View?, data: UserWithFavStatusLiveData) {
                view?.let {
                    val action = DetailUserFragmentDirections.actionDetailUserFragmentSelf(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val username = arguments?.getString("username") ?: ""
        viewModel.getFollowing(username).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbConnection.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbConnection.visibility = View.GONE
                    showList(result.data)
                }
                is Result.Error -> {
                    binding.pbConnection.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan" + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showList(users: List<UserWithFavStatusLiveData>){
        userList.clear()
        userList.addAll(users)
        rvAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}