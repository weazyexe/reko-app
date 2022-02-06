package dev.weazyexe.reko.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.weazyexe.core.utils.EMPTY_STRING
import dev.weazyexe.reko.R
import dev.weazyexe.reko.ui.theme.Typography

@Composable
fun RekoTextField(
    modifier: Modifier = Modifier,
    value: String = EMPTY_STRING,
    hint: String = EMPTY_STRING,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String = EMPTY_STRING,
    hasTogglePasswordButton: Boolean = false
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            label = { Text(hint) },

            singleLine = singleLine,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
            ),
            onValueChange = { onValueChange(it) },
            visualTransformation = if (hasTogglePasswordButton) {
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            } else {
                visualTransformation
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = errorMessage.isNotBlank(),
            trailingIcon = {
                if (hasTogglePasswordButton) {
                    val image = if (isPasswordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = stringResource(
                                if (isPasswordVisible) {
                                    R.string.content_description_password_eye_visible
                                } else {
                                    R.string.content_description_password_eye_hidden
                                }
                            )
                        )
                    }
                }
            }
        )
        if (errorMessage.isNotBlank()) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = Typography.labelMedium
            )
        }
    }
}