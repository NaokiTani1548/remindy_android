package com.example.remindy

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import java.net.HttpURLConnection
import java.net.URL

/**
 * E2Eテストの基底クラス。
 * 各テスト前にトークンを消去してログアウト状態にリセットする。
 *
 * 前提: TEST_EMAIL/TEST_PASSWORD のアカウントが Render バックエンドに登録済みであること。
 * 未登録の場合は AuthE2ETest.testRegister を先に実行してアカウントを作成すること。
 */
abstract class E2ETestBase {

    // order=0: composeRule より先に実行し、通知許可ダイアログを事前に抑制する
    @get:Rule(order = 0)
    val grantPermissionRule: GrantPermissionRule =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
        else
            GrantPermissionRule.grant()

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    companion object {
        const val TEST_EMAIL = "alice@example.com"
        const val TEST_PASSWORD = "devpass"
        const val BACKEND_BASE_URL = "https://remindy-backend-zpvi.onrender.com"

        /**
         * OkHttp のタイムアウト (60s) より長く設定する。
         * OkHttp がタイムアウトしてエラーを返した後、UI が更新されるまでの余裕を持たせるため。
         */
        const val TIMEOUT_MS = 75_000L

        /**
         * Render 無料プランはコールドスタートに最大3分かかる場合がある。
         * テストクラスごとに1回だけ呼ばれ、バックエンドが応答するまで最大180秒待つ。
         */
        @BeforeClass
        @JvmStatic
        fun warmUpBackend() {
            val deadline = System.currentTimeMillis() + 180_000L
            while (System.currentTimeMillis() < deadline) {
                try {
                    val conn = URL("$BACKEND_BASE_URL/").openConnection() as HttpURLConnection
                    conn.connectTimeout = 15_000
                    conn.readTimeout = 15_000
                    val code = conn.responseCode
                    conn.disconnect()
                    if (code < 500) return // サーバーが起動していれば OK (404 でも可)
                } catch (_: Exception) {
                    // 接続失敗は無視してリトライ
                }
                Thread.sleep(5_000)
            }
            // 180秒後も起動しなければそのままテストを続行（テスト自体がタイムアウトする）
        }
    }

    /**
     * DataStore のトークンを消去してログイン画面が表示されるまで待つ。
     * RemindyApp は isLoggedIn Flow を監視しているため、
     * トークン消去後に自動でログイン画面へ遷移する。
     *
     * waitUntil のラムダ内で IllegalStateException (No compose hierarchies found) が
     * 発生した場合は false を返してリトライする。これは画面遷移中の瞬間的な
     * ヒエラルキー不在を安全に処理するためのものである。
     */
    @Before
    fun resetToLoggedOut() {
        val app = ApplicationProvider.getApplicationContext<RemindyApplication>()
        runBlocking { app.container.tokenStore.clear() }
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithText("ログイン").fetchSemanticsNodes().isNotEmpty()
            } catch (_: IllegalStateException) {
                false // ヒエラルキーがまだ準備できていない場合はリトライ
            }
        }
    }

    /** ログインヘルパー。完了後はリマインダー一覧画面が表示されている状態になる。 */
    fun login(email: String = TEST_EMAIL, password: String = TEST_PASSWORD) {
        composeRule.onNodeWithTag("input_email").performTextInput(email)
        composeRule.onNodeWithTag("input_password").performTextInput(password)
        composeRule.onNodeWithText("ログイン").performClick()
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                // ボトムナビの「リマインダー」タブが出たらログイン完了
                composeRule.onAllNodesWithText("リマインダー").fetchSemanticsNodes().size >= 1
            } catch (_: IllegalStateException) {
                false
            }
        }
    }
}
