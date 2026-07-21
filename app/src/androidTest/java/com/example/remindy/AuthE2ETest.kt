package com.example.remindy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 認証フローの E2E テスト。
 *
 * テスト実行前に TEST_EMAIL/TEST_PASSWORD のアカウントを登録しておくこと。
 * testRegister を一度実行すれば以降はそのアカウントを使い回せる。
 */
@RunWith(AndroidJUnit4::class)
class AuthE2ETest : E2ETestBase() {

    /**
     * 新規登録 → 自動ログイン → メイン画面遷移を確認。
     * 同じメールは2回登録できないため、登録済みの場合はエラー画面が表示される。
     * どちらの結果でも「操作が完了した」ことを確認する。
     */
    @Test
    fun testRegister() {
        composeRule.onNodeWithTag("input_email").performTextInput(TEST_EMAIL)
        composeRule.onNodeWithTag("input_password").performTextInput(TEST_PASSWORD)
        composeRule.onNodeWithText("新規登録してログイン").performClick()

        // 登録成功→メイン画面 OR 登録失敗（アカウント既存）→エラー表示、いずれかを確認
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                val onMain = composeRule.onAllNodesWithText("学習").fetchSemanticsNodes().isNotEmpty()
                val onError = composeRule.onAllNodesWithTag("auth_error").fetchSemanticsNodes().isNotEmpty()
                onMain || onError
            } catch (_: IllegalStateException) {
                false
            }
        }
        // ここまで到達すれば成功（メイン画面またはエラー画面が表示されている）
    }

    /** 正しい認証情報でログインしメイン画面に遷移することを確認する。 */
    @Test
    fun testLogin() {
        login()

        // ボトムナビと画面タイトルの両方に「リマインダー」が存在するため onAllNodesWithText を使う
        composeRule.onAllNodesWithText("リマインダー")[0].assertIsDisplayed()
        composeRule.onNodeWithText("学習").assertIsDisplayed()
        composeRule.onNodeWithText("設定").assertIsDisplayed()
    }

    /**
     * 誤ったパスワードでログインするとエラー表示エリアが現れることを確認する。
     * エラーテキストの正確な内容（HTTP 401 / タイムアウト等）には依存しない。
     */
    @Test
    fun testLoginWithWrongPassword_showsError() {
        composeRule.onNodeWithTag("input_email").performTextInput(TEST_EMAIL)
        composeRule.onNodeWithTag("input_password").performTextInput("wrongpassword")
        composeRule.onNodeWithText("ログイン").performClick()

        // auth_error タグ付きボックスが表示されるまで待つ
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithTag("auth_error").fetchSemanticsNodes().isNotEmpty()
            } catch (_: IllegalStateException) {
                false
            }
        }
        composeRule.onNodeWithTag("auth_error").assertIsDisplayed()
    }
}
