package com.sensorscope;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavType;
import com.sensorscope.core.model.SensorType;
import com.sensorscope.presentation.logs.LogsViewModel;
import com.sensorscope.presentation.navigation.Destination;
import com.sensorscope.presentation.sensor.SensorViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u0003H\u0003\u00a8\u0006\u0005"}, d2 = {"SensorScopeApp", "", "onShareUri", "Lkotlin/Function1;", "Landroid/net/Uri;", "app_debug"})
public final class MainActivityKt {
    
    @androidx.compose.runtime.Composable()
    private static final void SensorScopeApp(kotlin.jvm.functions.Function1<? super android.net.Uri, kotlin.Unit> onShareUri) {
    }
}