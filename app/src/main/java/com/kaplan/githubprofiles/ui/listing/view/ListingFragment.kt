package com.kaplan.githubprofiles.ui.listing.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kaplan.githubprofiles.data.Result
import com.kaplan.githubprofiles.databinding.FragmentListBinding
import com.kaplan.githubprofiles.di.Injectable
import com.kaplan.githubprofiles.di.injectViewModel
import com.kaplan.githubprofiles.di.observe
import com.kaplan.githubprofiles.ui.listing.data.ListingItem
import com.kaplan.githubprofiles.util.ConnectionLiveData
import com.kaplan.githubprofiles.util.ConnectivityUtil
import com.kaplan.githubprofiles.util.EndlessScrollModel
import com.kaplan.githubprofiles.util.then
import com.kaplan.githubprofiles.util.ui.hide
import com.kaplan.githubprofiles.util.ui.show
import kotlinx.android.synthetic.main.shimmer_container.*
import javax.inject.Inject

class ListingFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var listingViewModel: ListingViewModel

    private lateinit var binding: FragmentListBinding

    @Inject
    lateinit var listAdapter: ListingAdapter

    private val endlessScrollModel = EndlessScrollModel()

    var listofIds = emptyList<Int>()

    var firstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listingViewModel = injectViewModel(viewModelFactory)
        listingViewModel.connectivityAvailable =
            ConnectivityUtil.isNetworkAvailable(requireContext())
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessScrollModel.visibleThreshold = 5
        binding.apply {
            recyclerView.adapter = listAdapter
            binding.viewModel = listingViewModel
            model = endlessScrollModel
        }
        subscribeDb()
        subscribeUi(binding, listAdapter)
        subscribeConnectionChange()
    }

    private fun preparelist(list: List<ListingItem>) {
        list.forEach { listingItem ->
            if (listofIds.any { i: Int -> i == listingItem.id }) {
                listingItem.hasNote = true
            }
        }
        listAdapter.updateData(list)
    }

    private fun subscribeUi(binding: FragmentListBinding, adapter: ListingAdapter) {
        observe(listingViewModel.listMediator)
        { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    if (result.data.isNullOrEmpty().not()) {
                        binding.progressBar.hide()
                        endlessScrollModel.visibleThreshold = result.data!!.size
                        listingViewModel.pageLiveData.value = result.data.last().id
                        endlessScrollModel.currentPage = result.data.last().id
                        preparelist(result.data)
                        binding?.apply {
                            shimmerFrameLayout.stopShimmer()
                            isEmpty = false
                            executePendingBindings()
                        }
                    }
                }
                Result.Status.LOADING -> {
                    binding.progressBar.show()
                    binding?.apply {
                        if (firstLoad) {
                            shimmerFrameLayout.startShimmer()
                            isEmpty = true
                            executePendingBindings()
                        }
                    }
                    firstLoad = false
                }
                Result.Status.ERROR -> {
                    binding?.apply {
                        shimmerFrameLayout.stopShimmer()
                        isEmpty = false
                        executePendingBindings()
                    }

                    binding.progressBar.hide()
                    binding.root?.let {
                        Toast.makeText(requireContext(), result.message!!, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun subscribeDb() {
        observe(listingViewModel.dbItems)
        { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.map { detailItem -> detailItem.id }?.let {
                        listofIds = it
                    }
                }
            }
        }
    }

    private fun subscribeConnectionChange() {
        val connectionLiveData = ConnectionLiveData(requireContext())
        observe(connectionLiveData)
        { isConnected ->
            val message = isConnected then "Connected to the Network now" ?: "Connection Lost"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (isConnected) {
                listingViewModel.onLoadMore()
            }
        }
    }
}