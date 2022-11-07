package com.hardiksachan.core_ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.None
import arrow.core.Option
import com.hardiksachan.core_ui.resources.Icons
import com.hardiksachan.core_ui.theme.DocketTheme

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChanged: (String) -> Unit,
    errorMessage: Option<String>
) {
    var obfuscated by remember {
        mutableStateOf(true)
    }
    TextFieldWithError(
        value = password,
        onValueChange = onPasswordChanged,
        placeholder = {
            Text("at least 8 characters")
        },
        label = {
            Text("Password")
        },
        modifier = Modifier
            .fillMaxWidth(),
        errorMessage = errorMessage,
        trailingIcon = {
            Icon(
                painter = painterResource(id = if (obfuscated) Icons.Eye.off else Icons.Eye.on),
                contentDescription = "visibility switch",
                modifier = Modifier
                    .clickable { obfuscated = !obfuscated }
                    .padding(8.dp)
            )
        },
        visualTransformation = if (obfuscated) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview
@Composable
fun PasswordTextFieldPreview() {
    DocketTheme {
        Surface {
            PasswordTextField(password = "lolol", onPasswordChanged = {}, errorMessage = None)
        }
    }
}