package com.codeyuba.bfaa3submission

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.codeyuba.bfaa3submission.data.Result
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeyuba.bfaa3submission.adapter.UserListAdapter
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.databinding.FragmentSearchUserBinding
import com.codeyuba.bfaa3submission.interfaces.ItemAdapterCallback
import com.codeyuba.bfaa3submission.viewmodel.UserViewModel
import com.codeyuba.bfaa3submission.viewmodel.ViewModelFactory
import java.util.*
import kotlin.collections.ArrayList


class SearchUserFragment : Fragment() {
    private lateinit var rvAdapter: UserListAdapter
    private var _binding: FragmentSearchUserBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserWithFavStatusLiveData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()

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
            rvSearchUserList.setHasFixedSize(true)
            rvSearchUserList.layoutManager = LinearLayoutManager(context)
            rvSearchUserList.adapter = rvAdapter
        }

        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserWithFavStatusLiveData> {
            override fun onItemClicked(view: View?, data: UserWithFavStatusLiveData) {
                view?.let {
                    val action = SearchUserFragmentDirections.actionSearchUserFragmentToDetailUserActivity(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.resetSearchUser()
        viewModel.searchRes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbSearch.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbSearch.visibility = View.GONE
                    userList.clear()
                    userList.addAll(result.data)
                    rvAdapter.notifyDataSetChanged()
                }
                is Result.Error -> {
                    binding.pbSearch.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan" + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.itSearch.addTextChangedListener(
            object: TextWatcher {
                var timer = Timer()
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(text: Editable?) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(object: TimerTask(){
                        override fun run() {
                            activity?.runOnUiThread { viewModel.searchUser(text.toString()) }
                        }
                    }, 1500)
                }

            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

