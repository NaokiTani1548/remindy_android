package com.example.remindy.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ワードマーク画像のグラデーション配色を Compose テキストで再現。
 * ネイビー → パープル → テラコッタ
 */
private val WordmarkGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF1B1F3B),
        Color(0xFF5B2E6E),
        Color(0xFFC07855),
    ),
)

/** タイトルテキストの色：ベージュ背景に対して視認性の高いダークネイビー */
private val TitleColor = Color(0xFF1B1F3B)

/**
 * 各メイン画面共通のヘッダー。
 * - 画面固有のイラスト背景画像をフルブリードで表示
 * - 上部に "Remindy" グラデーションワードマーク
 * - その下に画面タイトル
 * - 下端をアプリ背景色へフェードしてリスト領域と自然につなぐ
 */
@Composable
fun ScreenHeader(
    @DrawableRes imageRes: Int,
    screenTitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp),
    ) {
        // ── 背景イラスト ─────────────────────────────────
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        // ── 下端フェード（背景色へ自然にブレンド）──────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background,
                        ),
                    ),
                ),
        )

        // ── テキストオーバーレイ ─────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Remindy ワードマーク（グラデーションテキスト）
            Text(
                text = "Remindy",
                style = TextStyle(
                    brush = WordmarkGradient,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
            // 画面固有タイトル
            Text(
                text = screenTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TitleColor,
            )
        }
    }
}
