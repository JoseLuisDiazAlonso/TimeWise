package com.timewise.app.ui.onboarding

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.ui.theme.SuccessColor

/*
Este archivo va a comprobar los permisos de notificación y va a configurar las acciones a llevar
a cabo en caso que sean o no aceptados.
*
**/

@Composable
fun NotificationPermissionPage () {
    val context = LocalContext.current
    var permissionGranted = remember {
        // Verifica si el SDK es mayor o igual a 33 y comprueba si los permisos están concedidos
        // o se aplican por inercia si el SDK es anterior al 33.
        mutableStateOf(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
               ContextCompat.checkSelfPermission(
                   context, Manifest.permission.POST_NOTIFICATIONS
               ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // Permiso concedido automáticamente en versiones anteriores a API 33
            }
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {granted ->
        permissionGranted.value = granted
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(stringResource(R.string.onboarding_notification_title), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.onboarding_notification_desc), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        if (permissionGranted.value) {
            Text(stringResource(R.string.onboarding_notification_granted), style = MaterialTheme.typography.bodyLarge)
        } else {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessColor
            )
        }

    }
}