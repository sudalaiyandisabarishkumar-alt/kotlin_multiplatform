package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppColors

// ═══════════════════════════════════════════════════════════════════════════════
// Form components  (used by LoginScreen and any future form screens)
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun FilledField(
    value:                String,
    onValueChange:        (String) -> Unit,
    placeholder:          String,
    modifier:             Modifier                   = Modifier,
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
        modifier             = modifier.fillMaxWidth(),
        placeholder          = { Text(placeholder, color = AppColors.TextHint) },
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
            backgroundColor         = if (isError) AppColors.ErrorRed.copy(alpha = 0.06f) else AppColors.FieldBg,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor  = Color.Transparent,
            errorIndicatorColor     = Color.Transparent,
            textColor               = AppColors.TextPrimary,
            cursorColor             = AppColors.Teal,
            errorCursorColor        = AppColors.ErrorRed,
        ),
    )
}

@Composable
fun FieldLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text       = text,
        fontSize   = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color      = AppColors.TextPrimary,
        modifier   = modifier,
    )
}

@Composable
fun FieldError(message: String, modifier: Modifier = Modifier) {
    Text(
        text     = message,
        color    = AppColors.ErrorRed,
        fontSize = 12.sp,
        modifier = modifier.padding(start = 4.dp, top = 4.dp),
    )
}

// ═══════════════════════════════════════════════════════════════════════════════
// Metric components  (used by HomeScreen and any future dashboard screens)
// ═══════════════════════════════════════════════════════════════════════════════

/** Holds the display value and label for a single metric tile. */
data class MetricItem(val value: String, val label: String)

/** Renders [items] as a 3-column grid of [MetricCard]s. */
@Composable
fun MetricGrid(items: List<MetricItem>, modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.chunked(3).forEach { row ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { item ->
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        value    = item.value,
                        label    = item.label,
                    )
                }
                // Fill empty slots in an incomplete last row
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

/** A single dark-teal card showing a metric value above its label. */
@Composable
fun MetricCard(
    value:    String,
    label:    String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier         = modifier
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(18.dp))
            .background(AppColors.TealCard),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier            = Modifier.padding(8.dp),
        ) {
            Text(
                text       = value,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = AppColors.White,
                textAlign  = TextAlign.Center,
                maxLines   = 1,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text      = label,
                fontSize  = 13.sp,
                color     = AppColors.WhiteA60,
                textAlign = TextAlign.Center,
            )
        }
    }
}
