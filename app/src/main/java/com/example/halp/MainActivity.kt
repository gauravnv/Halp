package com.example.halp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.halp.resultView.ResultViewFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().run {
                replace(R.id.result_view_fragment, ResultViewFragment())
                commit()
            }
        }
    }
}
