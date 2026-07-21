package com.example.remindy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Primary: インディゴ（青寄りの深い紫）────────────────────────────────────
//   Material Indigo 900: 青みが強く、深みのある紫
val Indigo900          = Color(0xFF1A237E)
val Indigo100          = Color(0xFFC5CAE9)   // バッジ・コンテナ
val OnIndigo900        = Color(0xFFFFFFFF)
val OnIndigo100        = Color(0xFF0B1156)

// ── Secondary: ディープパープル（赤みより、対比用）─────────────────────────
val DeepPurple700      = Color(0xFF512DA8)
val DeepPurple100      = Color(0xFFD1C4E9)
val OnDeepPurple700    = Color(0xFFFFFFFF)
val OnDeepPurple100    = Color(0xFF1F0060)

// ── Tertiary: スレートバイオレット ────────────────────────────────────────
val Violet700          = Color(0xFF4527A0)
val Violet50           = Color(0xFFEDE7F6)
val OnViolet700        = Color(0xFFFFFFFF)
val OnViolet50         = Color(0xFF1A0066)

// ── Error ───────────────────────────────────────────────────────────────────
val ErrorRed           = Color(0xFFBA1A1A)
val ErrorRedLight      = Color(0xFFFFDAD6)
val OnErrorRed         = Color(0xFFFFFFFF)
val OnErrorRedLight    = Color(0xFF410002)

// ── アンバー（月次バッジ・アクセント）──────────────────────────────────────
val Amber100           = Color(0xFFFFECB3)
val Amber900           = Color(0xFFE65100)

private val LightColorScheme = lightColorScheme(
    primary            = Indigo900,
    onPrimary          = OnIndigo900,
    primaryContainer   = Indigo100,
    onPrimaryContainer = OnIndigo100,

    secondary            = DeepPurple700,
    onSecondary          = OnDeepPurple700,
    secondaryContainer   = DeepPurple100,
    onSecondaryContainer = OnDeepPurple100,

    tertiary            = Violet700,
    onTertiary          = OnViolet700,
    tertiaryContainer   = Violet50,
    onTertiaryContainer = OnViolet50,

    error            = ErrorRed,
    onError          = OnErrorRed,
    errorContainer   = ErrorRedLight,
    onErrorContainer = OnErrorRedLight,

    // リスト背景: 明確に見えるラベンダー
    background       = Color(0xFFD5CBF0),
    onBackground     = Color(0xFF0D0C2B),

    // カード背景: 白に近いが、背景との対比で浮き出る
    surface          = Color(0xFFF7F5FF),
    onSurface        = Color(0xFF0D0C2B),

    surfaceVariant   = Color(0xFFBDB5D8),
    onSurfaceVariant = Color(0xFF3A3556),
    outline          = Color(0xFF6D6887),
    outlineVariant   = Color(0xFFBDB5D8),
)

@Composable
fun RemindyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content,
    )
}
