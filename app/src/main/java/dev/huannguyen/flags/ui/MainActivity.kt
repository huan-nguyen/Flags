package dev.huannguyen.flags.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.huannguyen.flags.R
import dev.huannguyen.flags.di.ServiceLocator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        ServiceLocator.dataSyncManager.syncFlagData()
    }
}
