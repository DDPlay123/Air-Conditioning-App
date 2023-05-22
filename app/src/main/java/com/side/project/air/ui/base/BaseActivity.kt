package com.side.project.air.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

open class BaseActivity<VB: ViewDataBinding>(@LayoutRes val layoutRes: Int) : AppCompatActivity() {
    private var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutRes)

        binding.initialize()
    }

    open fun VB.initialize() {
        // 清空ViewModel，避免記憶體洩漏。
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_STOP) {
                    window?.let {
                        if (window.peekDecorView() != null)
                            window.peekDecorView().cancelPendingInputEvents()
                    }
                }
            }
        })

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    if (!isChangingConfigurations)
                        viewModelStore.clear()
                }
            }
        })
    }
}