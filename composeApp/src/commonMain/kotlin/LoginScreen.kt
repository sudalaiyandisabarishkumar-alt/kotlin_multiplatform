package com.example.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import cmp_koin_di.composeapp.generated.resources.Res
import cmp_koin_di.composeapp.generated.resources.vgro_logo

// ─── Brand colours ────────────────────────────────────────────────────────────
private val Teal        = Color(0xFF0D4F4F)
private val White       = Color.White
private val FieldBg     = Color(0xFFF0F0F0)
private val TextPrimary = Color(0xFF2B2B2B)
private val TextHint    = Color(0xFF9E9E9E)
private val LinkBlue    = Color(0xFF1565C0)
private val ErrorRed    = Color(0xFFD32F2F)
private val BorderGray  = Color(0xFFBDBDBD)

// ─── LoginScreen ──────────────────────────────────────────────────────────────
@Composable
fun LoginScreen(
    onLoginSuccess:   (employeeId: String, password: String) -> Unit,
    onForgotPassword: (employeeId: String) -> Unit,
    onTermsTap:       () -> Unit = {},
    isLoading:        Boolean    = false,
    errorMessage:     String?    = null,
) {
    var employeeId      by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var acceptedTandC   by remember { mutableStateOf(false) }

    var employeeIdError by remember { mutableStateOf<String?>(null) }
    var passwordError   by remember { mutableStateOf<String?>(null) }
    var tandCError      by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize().background(Teal)) {

        // ── Header with logo ──────────────────────────────────────────────────
        Box(
            modifier         = Modifier.fillMaxWidth().height(180.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter            = painterResource(Res.drawable.vgro_logo),
                contentDescription = "VGro logo",
                modifier           = Modifier.height(90.dp),
                contentScale       = ContentScale.Fit,
            )
        }

        // ── White card ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 160.dp)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .background(White),
        ) {

            // ── Scrollable form fields ────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 230.dp), // space reserved for fixed bottom bar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    Spacer(Modifier.height(36.dp))

                    Text(
                        text       = "Login to VGro",
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary,
                    )

                    Spacer(Modifier.height(32.dp))

                    // ── Employee ID ───────────────────────────────────────────
                    FieldLabel("Employee ID")
                    Spacer(Modifier.height(8.dp))
                    FilledField(
                        value         = employeeId,
                        onValueChange = {
                            employeeId      = it.filter { c -> c.isLetterOrDigit() }
                            employeeIdError = null
                        },
                        placeholder   = "Employee ID",
                        isError       = employeeIdError != null,
                        imeAction     = ImeAction.Next,
                        onImeAction   = { focusManager.moveFocus(FocusDirection.Down) },
                    )
                    if (employeeIdError != null) FieldError(employeeIdError!!)

                    Spacer(Modifier.height(20.dp))

                    // ── Password ──────────────────────────────────────────────
                    FieldLabel("Password")
                    Spacer(Modifier.height(8.dp))
                    FilledField(
                        value                = password,
                        onValueChange        = {
                            password      = it
                            passwordError = null
                        },
                        placeholder          = "Password",
                        isError              = passwordError != null,
                        imeAction            = ImeAction.Done,
                        onImeAction          = { focusManager.clearFocus() },
                        keyboardType         = KeyboardType.Password,
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon         = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    modifier           = Modifier.size(18.dp),
                                    imageVector        = if (passwordVisible) IconVisibility
                                    else IconVisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide" else "Show",
                                    tint               = Teal,
                                )
                            }
                        },
                    )
                    if (passwordError != null) FieldError(passwordError!!)

                    // Server-side error
                    if (errorMessage != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(errorMessage, color = ErrorRed, fontSize = 13.sp)
                    }
                }
            }

            // ── Fixed bottom bar ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 24.dp),
            ) {

                // Thin top divider
//                Divider(color = BorderGray.copy(alpha = 0.4f), thickness = 0.5.dp)

                Spacer(Modifier.height(12.dp))

                // T&C checkbox row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.fillMaxWidth(),
                ) {
                    Checkbox(
                        checked         = acceptedTandC,
                        onCheckedChange = {
                            acceptedTandC = it
                            if (it) tandCError = false
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor   = Teal,
                            uncheckedColor = BorderGray,
                            checkmarkColor = White,
                        ),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = TextPrimary, fontSize = 13.sp)) {
                                append("By clicking, you agree to our ")
                            }
                            withStyle(
                                SpanStyle(
                                    color          = LinkBlue,
                                    fontSize       = 13.sp,
                                    textDecoration = TextDecoration.Underline,
                                )
                            ) { append("Terms & Conditions") }
                        },
                        modifier = Modifier.clickable(
                            indication        = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { onTermsTap() },
                    )
                }

                if (tandCError) {
                    Text(
                        text     = "Please accept Terms & Conditions",
                        color    = ErrorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                    )
                }

                Spacer(Modifier.height(12.dp))

                // LOGIN button
                Button(
                    onClick = {
                        var valid = true
                        if (employeeId.isBlank()) {
                            employeeIdError = "Enter employee id to continue"
                            valid = false
                        }
                        if (password.isBlank()) {
                            passwordError = "Enter password to continue"
                            valid = false
                        }
                        if (!acceptedTandC) {
                            tandCError = true
                            valid = false
                        }
                        if (valid) onLoginSuccess(employeeId.trim(), password.trim())
                    },
                    enabled   = !isLoading,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape     = RoundedCornerShape(50),
                    colors    = ButtonDefaults.buttonColors(
                        backgroundColor         = Teal,
                        contentColor            = White,
                        disabledBackgroundColor = Teal.copy(alpha = 0.5f),
                        disabledContentColor    = White,
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 2.dp),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color       = White,
                            modifier    = Modifier.size(22.dp),
                            strokeWidth = 2.5.dp,
                        )
                    } else {
                        Text(
                            text          = "LOGIN",
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontSize      = 15.sp,
                        )
                    }
                }

                // FORGOT PASSWORD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication        = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ) { onForgotPassword(employeeId.trim()) }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text          = "FORGOT PASSWORD?",
                        color         = TextPrimary,
                        fontWeight    = FontWeight.Bold,
                        fontSize      = 13.sp,
                        letterSpacing = 1.sp,
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ─── FilledField ──────────────────────────────────────────────────────────────
@Composable
private fun FilledField(
    value:                String,
    onValueChange:        (String) -> Unit,
    placeholder:          String,
    isError:              Boolean                    = false,
    imeAction:            ImeAction                  = ImeAction.Default,
    onImeAction:          () -> Unit                 = {},
    keyboardType:         KeyboardType               = KeyboardType.Text,
    visualTransformation: VisualTransformation       = VisualTransformation.None,
    trailingIcon:         (@Composable () -> Unit)?  = null,
) {
    TextField(
        value                = value,
        onValueChange        = onValueChange,
        modifier             = Modifier.fillMaxWidth(),
        placeholder          = { Text(placeholder, color = TextHint) },
        singleLine           = true,
        isError              = isError,
        visualTransformation = visualTransformation,
        keyboardOptions      = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction    = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() },
            onDone = { onImeAction() },
        ),
        trailingIcon = trailingIcon,
        shape        = RoundedCornerShape(12.dp),
        colors       = TextFieldDefaults.textFieldColors(
            backgroundColor         = if (isError) ErrorRed.copy(alpha = 0.06f) else FieldBg,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor  = Color.Transparent,
            errorIndicatorColor     = Color.Transparent,
            textColor               = TextPrimary,
            cursorColor             = Teal,
            errorCursorColor        = ErrorRed,
        ),
    )
}

// ─── Small helpers ────────────────────────────────────────────────────────────
@Composable
private fun FieldLabel(text: String) {
    Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
}

@Composable
private fun FieldError(message: String) {
    Text(
        message,
        color    = ErrorRed,
        fontSize = 12.sp,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp),
    )
}

// ─── Eye icons ────────────────────────────────────────────────────────────────
private val IconVisibility: ImageVector
    get() = ImageVector.Builder(
        name          = "Visibility",
        defaultWidth  = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth  = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(12f, 4.5f)
            curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
            curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
            curveTo(17f, 19.5f, 21.27f, 16.39f, 23f, 12f)
            curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
            close()
            moveTo(12f, 17f)
            curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
            curveTo(7f, 9.24f, 9.24f, 7f, 12f, 7f)
            curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f)
            curveTo(17f, 14.76f, 14.76f, 17f, 12f, 17f)
            close()
            moveTo(12f, 9f)
            curveTo(10.34f, 9f, 9f, 10.34f, 9f, 12f)
            curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
            curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
            curveTo(15f, 10.34f, 13.66f, 9f, 12f, 9f)
            close()
        }
    }.build()

private val IconVisibilityOff: ImageVector
    get() = ImageVector.Builder(
        name          = "VisibilityOff",
        defaultWidth  = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth  = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(2f, 4.27f); lineTo(3.27f, 3f); lineTo(21f, 20.73f)
            lineTo(19.73f, 22f); lineTo(16.06f, 18.33f)
            curveTo(14.77f, 18.75f, 13.41f, 19f, 12f, 19f)
            curveTo(7f, 19f, 2.73f, 15.9f, 1f, 11.5f)
            curveTo(1.69f, 9.76f, 2.79f, 8.24f, 4.19f, 7.04f)
            lineTo(2f, 4.27f); close()
            moveTo(12f, 6f)
            curveTo(14.76f, 6f, 17f, 8.24f, 17f, 11f)
            curveTo(17f, 11.64f, 16.87f, 12.26f, 16.64f, 12.82f)
            lineTo(19.57f, 15.75f)
            curveTo(21.07f, 14.49f, 22.27f, 12.86f, 23f, 11f)
            curveTo(21.27f, 6.61f, 17f, 3.5f, 12f, 3.5f)
            curveTo(10.59f, 3.5f, 9.24f, 3.75f, 7.97f, 4.2f)
            lineTo(10.16f, 6.38f)
            curveTo(10.74f, 6.14f, 11.35f, 6f, 12f, 6f); close()
            moveTo(10.93f, 7.14f); lineTo(13f, 9.21f)
            curveTo(13.57f, 9.53f, 14f, 10.21f, 14f, 11f)
            curveTo(14f, 12.1f, 13.1f, 13f, 12f, 13f)
            curveTo(11.21f, 13f, 10.53f, 12.57f, 10.21f, 12f)
            lineTo(8.15f, 9.94f)
            curveTo(8.06f, 10.27f, 8f, 10.63f, 8f, 11f)
            curveTo(8f, 13.76f, 10.24f, 16f, 13f, 16f)
            curveTo(13.37f, 16f, 13.72f, 15.94f, 14.05f, 15.85f)
            lineTo(16.53f, 18.32f)
            curveTo(15.11f, 18.75f, 13.59f, 19f, 12f, 19f)
            curveTo(7f, 19f, 2.73f, 15.9f, 1f, 11.5f)
            curveTo(1.85f, 9.28f, 3.35f, 7.38f, 5.27f, 6.1f)
            lineTo(7.32f, 8.14f); close()
        }
    }.build()