package com.twofasapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.twofasapp.base.lifecycle.AuthAware
import com.twofasapp.base.lifecycle.AuthLifecycle
import com.twofasapp.data.services.ServicesRepository
import com.twofasapp.data.session.SessionRepository
import com.twofasapp.data.session.SettingsRepository
import com.twofasapp.designsystem.AppThemeState
import com.twofasapp.designsystem.ktx.makeWindowSecure
import com.twofasapp.designsystem.ktx.toastLong
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), AuthAware {

    companion object {
        private const val UPDATE_REQUEST_CODE = 43513
    }

    private val settingsRepository: SettingsRepository by inject()
    private val sessionRepository: SessionRepository by inject()
    private val servicesRepository: ServicesRepository by inject()
    private var recalculateTimeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppThemeState.applyTheme(settingsRepository.getAppSettings().selectedTheme)
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            settingsRepository.observeAppSettings().collect {
                makeWindowSecure(allow = it.allowScreenshots)
            }
        }

        setContent { MainScreen() }

        attachAuthLifecycleObserver()
    }

    override fun onResume() {
        super.onResume()
        servicesRepository.setTickerEnabled(true)

        recalculateTimeJob = lifecycleScope.launch {
            sessionRepository.recalculateTimeDelta()
        }
    }

    override fun onAuthenticated() = Unit

    override fun onPause() {
        super.onPause()
        servicesRepository.setTickerEnabled(false)
        recalculateTimeJob?.cancel()
        recalculateTimeJob = null
    }

    private fun attachAuthLifecycleObserver() {
        lifecycle.addObserver(
            AuthLifecycle(
                authTracker = get(),
                navigator = get { parametersOf(this) },
                authAware = this as? AuthAware
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return

        if (requestCode == UPDATE_REQUEST_CODE) {
            toastLong("Updating. Please wait...")
            return
        }
    }
}