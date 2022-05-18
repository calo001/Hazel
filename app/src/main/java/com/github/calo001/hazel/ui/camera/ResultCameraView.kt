package com.github.calo001.hazel.ui.camera

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.calo001.hazel.ui.common.HazelToolbarCameraResult
import com.github.calo001.hazel.ui.common.SurfaceToolbar
import com.github.calo001.hazel.ui.main.SearchResults
import com.github.calo001.hazel.ui.main.SearchStatus
import com.github.calo001.hazel.util.PainterIdentifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalFoundationApi
@Composable
fun ResultCameraView(
    result: String,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit,
    onCopy: () -> Unit,
    painterIdentifier: PainterIdentifier,
    searchStatus: SearchStatus,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SearchResults(
            querySearch = result,
            searchStatus = searchStatus,
            onNavigate = onNavigate,
            painterIdentifier = painterIdentifier,
            spacerTop = 160,
            header = {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    SelectionContainer {
                        Text(
                            text = result,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        SurfaceToolbar(modifier = Modifier.align(Alignment.TopCenter)) {
            HazelToolbarCameraResult(
                onNavBack = onBackClick,
                onCopy = onCopy,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}