package com.side.project.air.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

abstract class BaseActivity(@LayoutRes val layoutRes: Int) : AppCompatActivity() {
    companion object {
        const val DEFAULT_LATITUDE = 25.043871531367014
        const val DEFAULT_LONGITUDE = 121.53453374432904
    }

    private var _binding: ViewDataBinding? = null
    val binding: ViewDataBinding?
        get() = _binding

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutRes)

        initialize()
    }

    open fun initialize() {
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