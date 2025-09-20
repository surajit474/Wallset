package com.example.wallset.ui.home


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.wallset.R
import com.example.wallset.util.FolderPicker

@Composable
fun HomeScreen( homeViewModel: HomeViewModel = viewModel()) {
    Log.d("HomeScreen", "Entering Homescreen")

    val context = LocalContext.current
    homeViewModel.getSetting(context)



    Scaffold() { innerPadding ->
        HomeBody(innerPadding = innerPadding)
    }

}


@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel = viewModel()

) {

    val context = LocalContext.current



    val settings by homeViewModel.settings.collectAsState()

    var min by remember { mutableStateOf(0) }
    var hour by remember { mutableStateOf( "") }
    var isHome by remember { mutableStateOf(true) }
    var isLock by remember { mutableStateOf(true) }
    var isDarkFolder by remember { mutableStateOf(false) }
    var isLightFolder by remember { mutableStateOf(false) }
    var isSave by remember { mutableStateOf(false) }
    var darkFolderPath by remember { mutableStateOf("") }
    var lightFolderPath by remember { mutableStateOf("") }


    Log.d("HomeScreen", "settings : $settings")

    if (settings != null && darkFolderPath.isEmpty() && hour.isEmpty()){
        hour = settings!!.interval.toString()
        isHome = settings!!.isHome
        isLock = settings!!.isLock
        darkFolderPath = settings!!.darkFolderLocation
        lightFolderPath = settings!!.lightFolderLocation

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 30.dp, bottom = 38.dp, start = 16.dp, end = 16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

       Button(
           onClick = {
               isDarkFolder = true
               },
       ) {
           Text(text = "Select Dark Wallpapers Folder")
       }
        if (isDarkFolder) {
            FolderPicker(
                onFolderPicked = { uri ->
               darkFolderPath = uri.toString()
                },
                onFolder = { value ->
                    isDarkFolder = value
                }
            )
        }


            val decodedDark = Uri.decode(darkFolderPath)
            val  darkPath = decodedDark.substringAfter("primary:")
            Text(
                "Dark Wallpapers : $darkPath",
                color = MaterialTheme.colorScheme.secondary,

            )



        Button(
            onClick = {
                isLightFolder = true
            },
        ) {
            Text(text = "Select Light Wallpapers Folder")
        }
        if (isLightFolder) {
            FolderPicker(
                onFolderPicked = { uri ->
                    lightFolderPath = uri.toString()
                },
                onFolder = { value ->
                    isLightFolder = value
                }
            )
        }


        val decodedLight = Uri.decode(lightFolderPath)
        val  lightPath = decodedLight.substringAfter("primary:")
        Text(
            "Light Wallpapers: $lightPath",
            color = MaterialTheme.colorScheme.secondary,

            )



        Spacer(Modifier.padding(vertical = 16.dp))

        Text(
            "Interval",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { hour = (hour.toInt() - 1).toString() },
                enabled = if (hour.isNotEmpty()) hour.toInt()>0 else  true
            ) {
                Icon(painter = painterResource(R.drawable.remove_24px), contentDescription = "Decrease")
            }

            OutlinedTextField(
                value = "$hour hr",
                onValueChange = {
                        hour = it
                                },
//                readOnly = true,
                modifier = Modifier.width(120.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )

            IconButton(onClick = { hour = (hour.toInt() + 1).toString()  }) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }

        Spacer(Modifier.padding(vertical = 16.dp))

        Text(
            "Screen",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
//                .padding(4.dp)
        ) {
            RadioButton(
                selected = isHome,
                onClick = {isHome = !isHome }
            )
            Text("HomeScreen")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
//                .padding(4.dp)
        ) {
            RadioButton(
                selected = isLock,
                onClick = { isLock = !isLock }
            )
            Text(" LockScreen")
        }


        Spacer(Modifier.padding(vertical = 16.dp))

        Text(
            "Status",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        WallpaperWorkerStatus(context)

        Spacer(Modifier.weight(1F))

        Button(
            onClick = {
                homeViewModel.saveSetting(
                    context = context,
                    isHome = isHome,
                    isLock = isLock,
                    interval = hour.toLong(),
                    darkFolderPath = darkFolderPath,
                    lightFolderPath = lightFolderPath
                )

                Log.d("HomeScreen", "settings : $settings")
                isSave = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save", style = MaterialTheme.typography.titleMedium)
        }


        Button(
            onClick = {
                homeViewModel.startWallpaperWorkManager(context)
        },
            enabled = isSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start")
        }
    }
}

@Composable
fun WallpaperWorkerStatus(context: Context) {
    val workManager = WorkManager.getInstance(context)
    val workInfos by workManager
        .getWorkInfosForUniqueWorkLiveData("WallpaperChanger")
        .observeAsState()

    workInfos?.firstOrNull()?.let { info ->
        when (info.state) {
            WorkInfo.State.RUNNING -> Text("Work is running…")
            WorkInfo.State.ENQUEUED -> Text("Work scheduled")
            WorkInfo.State.SUCCEEDED -> Text("Work finished")
            else -> Text("Idle")
        }
    }
}

@Preview
@Composable
fun HomePreview() {
HomeBody(innerPadding = PaddingValues(0.dp))
}
