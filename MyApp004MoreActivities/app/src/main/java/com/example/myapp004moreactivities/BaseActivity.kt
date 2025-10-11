package com.example.myapp004moreactivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp004moreactivities.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {

    protected lateinit var baseBinding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(baseBinding.root)

        // Default behavior for the navigation icon
        baseBinding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /** Inflates the specific activity’s content into the FrameLayout */
    protected fun setContentLayout(layoutResId: Int) {
        layoutInflater.inflate(layoutResId, baseBinding.contentFrame, true)
    }
}
