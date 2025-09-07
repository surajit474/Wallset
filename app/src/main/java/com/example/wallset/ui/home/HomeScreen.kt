package com.example.wallset.ui.home


import android.R
import android.R.attr.value
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.wallset.util.FolderPicker
import com.example.wallset.util.SharedPrefManager
import com.example.wallset.util.SharedPrefManager.getFolderUri

@Composable
fun HomeScreen() {
    Scaffold() { innerPadding ->
        HomeBody(innerPadding = innerPadding)
    }

}


@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues

) {
    val context = LocalContext.current

    var folderPath by remember { mutableStateOf("") }
    var min by remember { mutableStateOf(0) }
    var hour by remember { mutableStateOf(0) }
//    var hour by remember { mutableStateOf(0) }
    var wallpapers by remember { mutableStateOf<List<DocumentFile>>(emptyList()) }
    var wallpapersIndex by remember { mutableStateOf(0) }
    var wallpaperDelay by remember { mutableStateOf("") }

    var isFolder by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val folderUri = getFolderUri(context)
        folderPath = folderUri.toString()
    }


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
                SharedPrefManager.saveFolderUri(context, uri.toString())
                },
                onFolder = { value ->
                    isFolder = value
                }
            )
        }

        Text(folderPath)

        RoundNumberSelector(
            value = min,
            onValueChange = {
                min = it
            },
            range = IntRange(15,60)
        )

        RoundNumberSelector(
            value = hour,
            onValueChange = {
                hour = it
            },
            range = IntRange(15,60)
        )




        Spacer(Modifier.weight(1F))


        Button(
            onClick = {

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