package com.codeyuba.bfaa3submission

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.codeyuba.bfaa3submission.adapter.ConnectionPageAdapter
import com.codeyuba.bfaa3submission.data.Result
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.databinding.FragmentDetailUserBinding
import com.codeyuba.bfaa3submission.viewmodel.UserViewModel
import com.codeyuba.bfaa3submission.viewmodel.ViewModelFactory

class DetailUserFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!
    private val args: DetailUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = args.username
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.search_menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favorites_menu -> {
                val action = DetailUserFragmentDirections.actionDetailUserFragmentToFavoritesFragment()
                activity?.findNavController(R.id.fragmentContainerView)?.navigate(action)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        val pager = ConnectionPageAdapter(activity, args.username)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }
        binding.vpConnectionList.adapter = pager
        binding.vpConnectionList.visibility = View.INVISIBLE
        binding.tlConnection.visibility = View.INVISIBLE

        val username = args.username
        val favStatus = viewModel.isFav(username)

        viewModel.getDetailUser(username).observe(viewLifecycleOwner) {result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    val data = result.data
                    binding.progressBar.visibility = View.GONE
                    binding.vpConnectionList.visibility = View.VISIBLE
                    binding.tlConnection.visibility = View.VISIBLE

                    binding.apply {
                        tvHeadingFollowers.visibility = View.VISIBLE
                        tvHeadingFollowing.visibility = View.VISIBLE
                        tvHeadingRepositories.visibility = View.VISIBLE
                        favFloatingButton.visibility = View.VISIBLE
                        ibShare.visibility = View.VISIBLE
                        Glide.with(activity)
                            .load(data.avatarUrl)
                            .circleCrop()
                            .into(ivAvatar)
                        tvName.text = data.name
                        tvUsername.text = "@" + data.username
                        tvCompany.text = data.company
                        tvLocation.text = data.location
                        tvFollowersNumber.text = data.followers.toString()
                        tvFollowingNumber.text = data.following.toString()
                        tvRepositoriesNumber.text = data.publicRepos.toString()
                        ibShare.setOnClickListener{
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Hey Look that awesome ${data.name} Github !")
                                putExtra(Intent.EXTRA_TEXT, "Hey Look that awesome ${data.name} Github ! \nhttps://github.com/${data.username.lowercase()}")
                            }

                            startActivity(Intent.createChooser(intent, "Share link!"))
                        }

                        TabLayoutMediator(tlConnection, vpConnectionList) { tab, position ->
                            tab.text = resources.getString(TAB_TITLES[position])
                        }.attach()
                    }

                    favStatus.observe(viewLifecycleOwner) {
                        if (it) {
                            binding.favFloatingButton.setImageResource(R.drawable.ic_baseline_favorite_fill_20)
                        }else{
                            binding.favFloatingButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        }

                        val users = UserWithFavStatusLiveData(
                            data.username,
                            data.avatarUrl,
                            it,
                            favStatus
                        )

                        binding.favFloatingButton.setOnClickListener { _ ->
                            if (it) {
                                viewModel.removeFav(users)
                            }else{
                                viewModel.setFav(users)
                            }
                        }
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan" + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAB_TITLES = arrayListOf(
            R.string.followers_heading,
            R.string.following_heading
        )
    }
}