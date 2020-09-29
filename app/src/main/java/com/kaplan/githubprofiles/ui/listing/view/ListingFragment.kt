package com.kaplan.githubprofiles.ui.listing.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kaplan.githubprofiles.data.Result
import com.kaplan.githubprofiles.databinding.FragmentListBinding
import com.kaplan.githubprofiles.di.Injectable
import com.kaplan.githubprofiles.di.injectViewModel
import com.kaplan.githubprofiles.di.observe
import com.kaplan.githubprofiles.util.ConnectivityUtil
import com.kaplan.githubprofiles.util.EndlessScrollModel
import com.kaplan.githubprofiles.util.then
import javax.inject.Inject

class ListingFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var listingViewModel: ListingViewModel

    private lateinit var binding: FragmentListBinding

    @Inject
    lateinit var listAdapter: ListingAdapter

    private val endlessScrollModel = EndlessScrollModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listingViewModel = injectViewModel(viewModelFactory)
        listingViewModel.connectivityAvailable = ConnectivityUtil.isConnected(requireContext())
        binding = FragmentListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endlessScrollModel.visibleThreshold = 150
        binding.apply {
            taskListView.adapter = listAdapter
            binding.viewModel = listingViewModel
            model = endlessScrollModel
        }
        subscribeUi(binding, listAdapter)
    }

    private fun subscribeUi(binding: FragmentListBinding, adapter: ListingAdapter) {
        observe(listingViewModel.listMediator)
        { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        it.isNotEmpty() then {
                            listingViewModel.pageLiveData.value =
                                result.data[result.data.size - 1].id
                            endlessScrollModel.currentPage = result.data[result.data.size - 1].id
                            adapter.updateData(result.data)
                        }
                    }
                }
                Result.Status.LOADING -> {
                }
                Result.Status.ERROR -> {
                }
            }
        }
    }
}