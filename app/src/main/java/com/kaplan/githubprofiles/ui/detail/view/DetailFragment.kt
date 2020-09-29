package com.kaplan.githubprofiles.ui.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kaplan.githubprofiles.databinding.FragmentDetailBinding
import com.kaplan.githubprofiles.databinding.FragmentListBinding
import com.kaplan.githubprofiles.di.Injectable
import com.kaplan.githubprofiles.di.injectViewModel
import com.kaplan.githubprofiles.di.observe
import javax.inject.Inject

class DetailFragment : Fragment(), Injectable {

    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = injectViewModel(viewModelFactory)
            viewModel.username = args.userName!!
            subscribeUi(binding)
        }
    }

    private fun subscribeUi(binding: FragmentDetailBinding) {
        observe(viewModel.user)
        { result ->
            when (result.status) {
                com.kaplan.githubprofiles.data.Result.Status.SUCCESS -> {
                    binding.apply {
                        detailItem = result.data
                        executePendingBindings()
                    }
                }
                com.kaplan.githubprofiles.data.Result.Status.ERROR -> {

                }
                com.kaplan.githubprofiles.data.Result.Status.LOADING -> {}
            }
        }
    }
}