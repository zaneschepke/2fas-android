package com.twofasapp.feature.home.ui.services.add.scan

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.twofasapp.data.services.domain.RecentlyAddedService
import com.twofasapp.designsystem.TwIcons
import com.twofasapp.designsystem.TwTheme
import com.twofasapp.designsystem.common.RequestPermission
import com.twofasapp.designsystem.common.TwCenterTopAppBar
import com.twofasapp.designsystem.common.TwTextButton
import com.twofasapp.designsystem.dialog.ConfirmDialog
import com.twofasapp.designsystem.dialog.InfoDialog
import com.twofasapp.designsystem.ktx.settingsIntent
import com.twofasapp.designsystem.settings.SettingsLink
import com.twofasapp.feature.qrscan.QrScan
import com.twofasapp.locale.TwLocale
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AddServiceScanScreen(
    viewModel: AddServiceScanViewModel = koinViewModel(),
    openManual: () -> Unit,
    openGuides: () -> Unit,
    onAddedSuccessfully: (RecentlyAddedService) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showQrScanner by remember {
        mutableStateOf(false)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { viewModel.onLoadFromGallery(it) } }
    )

    var askForPermission by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect {
            when (it) {
                is AddServiceScanUiEvent.AddedSuccessfully -> onAddedSuccessfully(it.recentlyAddedService)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        TwCenterTopAppBar(
            titleText = TwLocale.strings.addTitle,
            containerColor = Color.Transparent,
            showBackButton = false,
        )

        if(showQrScanner) {
            QrScan(
                onScanned = {
                    showQrScanner = false
                    viewModel.onScanned(it)
                }
            )
        }

        SettingsLink(
            title = TwLocale.strings.scanQr,
            icon = TwIcons.Qr
        ) { showQrScanner = true }

        SettingsLink(
            title = TwLocale.strings.addEnterManual,
            icon = TwIcons.Keyboard
        ) { openManual() }

        SettingsLink(
            title = TwLocale.strings.addFromGallery,
            icon = TwIcons.Panorama
        ) {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        SettingsLink(
            title = TwLocale.strings.addWithGuide,
            icon = TwIcons.Guide
        ) { openGuides() }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (uiState.showInvalidQrDialog) {
        ConfirmDialog(
            onDismissRequest = { viewModel.resetScanner() },
            title = TwLocale.strings.addScanInvalidQrTitle,
            body = TwLocale.strings.addScanInvalidQrBody,
            positive = TwLocale.strings.addScanInvalidQrCta,
            negative = null,
        )
    }

    if (uiState.showServiceExistsDialog) {
        ConfirmDialog(
            onDismissRequest = { viewModel.resetScanner() },
            title = TwLocale.strings.addScanServiceExistsTitle,
            body = TwLocale.strings.addScanServiceExistsBody,
            positive = TwLocale.strings.addScanServiceExistsPositiveCta,
            negative = TwLocale.strings.addScanServiceExistsNegativeCta,
            onPositive = { viewModel.saveService(uiState.scanned, uiState.source) },
            onNegative = { viewModel.resetScanner() },
        )
    }

    if (uiState.showErrorDialog) {
        InfoDialog(
            onDismissRequest = { viewModel.resetScanner() },
            title = TwLocale.strings.addScanErrorTitle,
            body = TwLocale.strings.addScanErrorBody,
            positive = TwLocale.strings.addScanErrorPositiveCta,
        )
    }

    if (uiState.showGalleryErrorDialog) {
        InfoDialog(
            onDismissRequest = { viewModel.resetScanner() },
            title = TwLocale.strings.addGalleryErrorTitle,
            body = TwLocale.strings.addGalleryErrorBody,
            positive = TwLocale.strings.addGalleryErrorPositiveCta,
            onPositive = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        )
    }

    if (askForPermission) {
        RequestPermission(
            permission = Manifest.permission.CAMERA,
            rationaleEnabled = false,
            onGranted = { askForPermission = false },
            onDismissRequest = { askForPermission = false },
        )
    }
}