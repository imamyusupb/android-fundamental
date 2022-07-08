package com.codeyuba.bfaa3submission

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeyuba.bfaa3submission.adapter.UserListAdapter
import com.codeyuba.bfaa3submission.data.Result
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.databinding.FragmentFamousUserBinding
import com.codeyuba.bfaa3submission.interfaces.ItemAdapterCallback
import com.codeyuba.bfaa3submission.viewmodel.UserViewModel
import com.codeyuba.bfaa3submission.viewmodel.ViewModelFactory


class FamousUserFragment : Fragment() {
    private var _binding: FragmentFamousUserBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserWithFavStatusLiveData>()
    private lateinit var adapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamousUserBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        adapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvFamousUserList.setHasFixedSize(true)
            rvFamousUserList.layoutManager = LinearLayoutManager(context)
            rvFamousUserList.adapter = adapter
        }

        adapter.setAdapterItemCallback(object : ItemAdapterCallback<UserWithFavStatusLiveData> {
            override fun onItemClicked(view: View?, data: UserWithFavStatusLiveData) {
                view?.let {
                    val action = FamousUserFragmentDirections.actionFamousUserFragmentToDetailUserActivity(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getFamouseUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.pbFamous.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.pbFamous.visibility = View.GONE
                    showList(result.data)
                }
                is Result.Error -> {
                    binding.pbFamous.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan" + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favorites_menu -> {
                val action = FamousUserFragmentDirections.actionFamousUserFragmentToFavoritesFragment()
                activity?.findNavController(R.id.fragmentContainerView)?.navigate(action)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.app_name)
            show()
        }
    }

    private fun showList(users: List<UserWithFavStatusLiveData>){
        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}