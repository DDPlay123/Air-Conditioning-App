package com.side.project.air.ui.fragment

import com.side.project.air.R
import com.side.project.air.databinding.FragmentMainBinding
import com.side.project.air.ui.base.BaseFragment
import com.side.project.air.ui.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val viewModel: MainViewModel by activityViewModel()

    override fun FragmentMainBinding.initialize() {
        this@MainFragment.viewModel.let { vm ->
            binding.viewModel = vm
            vm.getCurrentLocation()
        }
    }
}