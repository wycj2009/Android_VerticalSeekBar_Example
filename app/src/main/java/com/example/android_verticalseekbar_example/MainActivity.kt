package com.example.android_verticalseekbar_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.android_verticalseekbar_example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.verticalSeekBar.onSeekBarChangeListener = object : VerticalSeekBar.OnSeekBarChangeListener {
            override fun preProgressChange(progress: Int): Boolean {
                Log.d("myLog", "preProgressChange - progress=${progress}")
                return true
            }

            override fun onProgressChange(progress: Int) {
                Log.d("myLog", "onProgressChange - progress=${progress}")
            }

            override fun onStartTrackingTouch() {
                Log.d("myLog", "onStartTrackingTouch")
            }

            override fun onStopTrackingTouch() {
                Log.d("myLog", "onStopTrackingTouch")
            }
        }
    }
}
