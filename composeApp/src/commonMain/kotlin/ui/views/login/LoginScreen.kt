package ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
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
import cmp_koin_di.composeapp.generated.resources.Res
import cmp_koin_di.composeapp.generated.resources.vgro_logo
import org.jetbrains.compose.resources.painterResource
import ui.components.FieldError
import ui.components.FieldLabel
import ui.components.FilledField
import ui.theme.AppColors
import ui.theme.IconVisibility
import ui.theme.IconVisibilityOff

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

    Box(modifier = Modifier.fillMaxSize().background(AppColors.Teal)) {

        // ── Logo header ──────────────────────────────────────────────────────
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

        // ── White card ───────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 160.dp)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .background(AppColors.White),
        ) {

            // ── Scrollable form area ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 240.dp),  // reserves space for fixed bottom bar
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
                        color      = AppColors.TextPrimary,
                    )

                    Spacer(Modifier.height(32.dp))

                    // ── Employee ID ──────────────────────────────────────────
                    FieldLabel("Employee ID")
                    Spacer(Modifier.height(8.dp))
                    FilledField(
                        value         = employeeId,
                        onValueChange = {
                            employeeId      = it.filter { c -> c.isLetterOrDigit() }
                            employeeIdError = null
                        },
                        placeholder = "Employee ID",
                        isError     = employeeIdError != null,
                        imeAction   = ImeAction.Next,
                        onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
                    )
                    if (employeeIdError != null) FieldError(employeeIdError!!)

                    Spacer(Modifier.height(20.dp))

                    // ── Password ─────────────────────────────────────────────
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
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    modifier           = Modifier.size(18.dp),
                                    imageVector        = if (passwordVisible) IconVisibility
                                    else IconVisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide" else "Show",
                                    tint               = AppColors.Teal,
                                )
                            }
                        },
                    )
                    if (passwordError != null) FieldError(passwordError!!)

                    // Server-side error
                    if (errorMessage != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(errorMessage, color = AppColors.ErrorRed, fontSize = 13.sp)
                    }
                }
            }

            // ── Fixed bottom bar ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(AppColors.White)
                    .padding(horizontal = 24.dp),
            ) {



                Spacer(Modifier.height(8.dp))

                // T&C checkbox
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
                            checkedColor   = AppColors.Teal,
                            uncheckedColor = AppColors.BorderGray,
                            checkmarkColor = AppColors.White,
                        ),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = AppColors.TextPrimary, fontSize = 13.sp)) {
                                append("By clicking, you agree to our ")
                            }
                            withStyle(SpanStyle(
                                color          = AppColors.LinkBlue,
                                fontSize       = 13.sp,
                                textDecoration = TextDecoration.Underline,
                            )) { append("Terms & Conditions") }
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
                        color    = AppColors.ErrorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                    )
                }

                Spacer(Modifier.height(12.dp))

                // LOGIN button
                Button(
                    onClick = {
                        var valid = true
                        if (employeeId.isBlank()) { employeeIdError = "Enter employee id to continue"; valid = false }
                        if (password.isBlank())   { passwordError   = "Enter password to continue";    valid = false }
                        if (!acceptedTandC)        { tandCError      = true;                            valid = false }
                        if (valid) onLoginSuccess(employeeId.trim(), password.trim())
                    },
                    enabled   = !isLoading,
                    modifier  = Modifier.fillMaxWidth().height(56.dp),
                    shape     = RoundedCornerShape(50),
                    colors    = ButtonDefaults.buttonColors(
                        backgroundColor         = AppColors.Teal,
                        contentColor            = AppColors.White,
                        disabledBackgroundColor = AppColors.Teal.copy(alpha = 0.5f),
                        disabledContentColor    = AppColors.White,
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 2.dp),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color       = AppColors.White,
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
                        color         = AppColors.TextPrimary,
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

// FilledField, FieldLabel, FieldError are in ui/components/UiComponents.kt