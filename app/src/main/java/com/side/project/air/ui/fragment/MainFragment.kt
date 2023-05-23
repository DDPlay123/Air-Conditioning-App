package com.side.project.air.ui.fragment

import android.os.Bundle
import android.view.View
import com.side.project.air.R
import com.side.project.air.databinding.FragmentMainBinding
import com.side.project.air.ui.base.BaseFragment
import com.side.project.air.ui.viewModel.MainViewModel
import com.side.project.air.utils.Contracts
import com.side.project.air.utils.widget.CircularSeekBar
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.math.roundToInt

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val vm: MainViewModel by activityViewModel()

    override fun FragmentMainBinding.initialize() {
        vm.apply {
            binding.viewModel = this
            getCurrentLocation()
            connectMQTT()
        }
    }

    override fun FragmentMainBinding.destroy() {
        vm.disconnectMQTT()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    private fun setListener() {
        binding.apply {
            sbAutoModeTemp.setOnSeekBarChangeListener(object : CircularSeekBar.OnCircularSeekBarChangeListener {
                override fun onProgressChanged(circularSeekBar: CircularSeekBar?, progress: Float, fromUser: Boolean) {
                    val temp = (progress / 100) * (Contracts.MAX_TEMPERATURE - Contracts.MIN_TEMPERATURE) + Contracts.MIN_TEMPERATURE
                    tvAutoModeTemp.text = String.format(getString(R.string.text_temp), temp.roundToInt())
                }

                override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                    val progress = seekBar?.progress ?: 0f
                    val temp = (progress / 100) * (Contracts.MAX_TEMPERATURE - Contracts.MIN_TEMPERATURE) + Contracts.MIN_TEMPERATURE
                    tvAutoModeTemp.text = String.format(getString(R.string.text_temp), temp.roundToInt())
                    vm.publishMQTT(Contracts.SET_TEMPERATURE_TOPIC, temp.roundToInt().toString(), true)
                }

                override fun onStartTrackingTouch(seekBar: CircularSeekBar?) { }
            })
        }
    }
}