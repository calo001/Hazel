package com.github.calo001.hazel.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    onTextChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var input by rememberSaveable { mutableStateOf("") }
    Row {
        SearchInput(
            input = input,
            placeholder = placeholder,
            onTextChange = { newValue ->
                input = newValue
                onTextChange(newValue)
            },
            keyboardController = keyboardController,
            modifier = modifier
                .weight(1f)
        )
        FloatingActionButton(
            onClick = { /*TODO*/ },
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 16.dp
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@ExperimentalComposeUiApi
@Composable
private fun SearchInput(
    input: String,
    placeholder: String,
    onTextChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier
) {
    TextField(
        value = input,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        label = {
            Text(
                text = placeholder,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.onPrimary,
            focusedLabelColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.3f),
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                onTextChange("")
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
        }),
        onValueChange = { newText ->
            onTextChange(newText)
        },
        modifier = modifier
            .shadow(16.dp, MaterialTheme.shapes.large)
    )
}