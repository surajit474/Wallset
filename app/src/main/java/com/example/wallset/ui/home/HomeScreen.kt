package com.example.wallset.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallset.util.FolderPicker

@Composable
fun HomeScreen() {
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

    homeViewModel.getSetting(context)


    var folderPath by remember { mutableStateOf("") }
    var min by remember { mutableStateOf(0) }
    var hour by remember { mutableStateOf(0) }
    var isHome by remember { mutableStateOf(true) }
    var isLock by remember { mutableStateOf(true) }

    var wallpapers by remember { mutableStateOf<List<DocumentFile>>(emptyList()) }
    var wallpapersIndex by remember { mutableStateOf(0) }
    var wallpaperDelay by remember { mutableStateOf("") }

    var isFolder by remember { mutableStateOf(false) }

    val settings by homeViewModel.settings.collectAsState()

//    folderPath = settings.folderLocation
//    isHome = settings.isHome
//    isLock = settings.isLock
//    hour = settings.interval.toInt()
//    min = settings.interval.toString().substringAfter(".").toInt()


    Column(
        modifier = modifier
        .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 30.dp, bottom = 38.dp, start = 16.dp, end = 16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

       Button(
           onClick = {
               isFolder = true
               },
       ) {
           Text(text = "Select Wallpapers Folder")
       }
        if (isFolder) {
            FolderPicker(
                onFolderPicked = { uri ->
               folderPath = uri.toString()
                },
                onFolder = { value ->
                    isFolder = value
                }
            )
        }


        val f: List<String> = folderPath.split("%")
        if (f.isNotEmpty()){
            Text(
                "${f.last()}"
            )
        }


        RoundNumberSelector(
            value = min,
            onValueChange = {
                min = it
            },
            range = IntRange(15,60)
        )
        Text("Minutes")

        RoundNumberSelector(
            value = hour,
            onValueChange = {
                hour = it
            },
            range = IntRange(0,25)
        )
        Text("Hour")


        TextButton(
            onClick = {
                homeViewModel.saveSetting(
                    context = context,
                    isHome = isHome,
                    isLock = isLock,
                    interval = "$hour.$min".toFloat(),
                    folderLocation = folderPath
                )
            }
        ) {
            Text("Save")
        }


        Spacer(Modifier.weight(1F))


        Button(
            onClick = {
                homeViewModel.startWallpaperWorkManager(context)
        }
        ) {
            Text("Start")
        }
    }
}


@Composable
fun RoundNumberSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange = 0..100
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, CircleShape)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { if (value > range.first) onValueChange(value - 1) }) {
//            Icon(painterResource( R.drawable.btn_minus), contentDescription = "Decrease")
        }

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(onClick = { if (value < range.last) onValueChange(value + 1) }) {
            Icon(Icons.Default.Add, contentDescription = "Increase")
        }
    }
}



@Preview
@Composable
fun HomePreview(){
    HomeBody(innerPadding = PaddingValues(0.dp))
}