@file:OptIn(ExperimentalMaterial3Api::class)

package com.kabi.composecomponents.navBar.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kabi.composecomponents.R
import com.kabi.composecomponents.ui.theme.CodingGreen
import com.kabi.composecomponents.ui.theme.CodingGrey
import com.kabi.composecomponents.ui.theme.CodingLightGrey
import kotlinx.coroutines.launch

/*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
*/

@Composable
fun BottomSheetScreen() {
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        val scaffoldState = rememberBottomSheetScaffoldState()
        val scope = rememberCoroutineScope()

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(CodingGreen),
                    modifier = Modifier
                        .size(250.dp)
                )
            },
            modifier = Modifier
                .padding(paddingValues),
            sheetPeekHeight = 0.dp,
            containerColor = CodingLightGrey,
            contentColor = CodingGreen,
            sheetContainerColor = CodingGrey,
            sheetContentColor = CodingGreen,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodingGreen,
                        contentColor = CodingLightGrey
                    )
                ) {
                    Text(text = "Open Sheet")
                }
            }
        }


        /*
        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen by rememberSaveable {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {
                    isSheetOpen = true
                }
            ) {
                Text(text = "Open Sheet")
            }
        }
        if (isSheetOpen){
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
            }
        }
        */

    }
}

@Preview
@Composable
private fun BottomSheetScreenPreview() {
    BottomSheetScreen()
}