package com.side.project.air.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.side.project.air.R
import com.side.project.air.databinding.ActivityMainBinding
import com.side.project.air.ui.base.BaseActivity
import com.side.project.air.ui.viewModel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}