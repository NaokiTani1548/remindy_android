package com.example.remindy

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * リマインダー機能の E2E テスト。
 * バックエンド (Render) が起動済みであることを前提とする。
 */
@RunWith(AndroidJUnit4::class)
class ReminderE2ETest : E2ETestBase() {

    /** リマインダーを新規作成し、一覧に表示されることを確認する。 */
    @Test
    fun testCreateReminder_appearsInList() {
        login()
        val title = "E2E_作成_${System.currentTimeMillis()}"

        composeRule.onNodeWithContentDescription("追加").performClick()
        composeRule.onNodeWithTag("input_title").performTextInput(title)
        composeRule.onNodeWithText("保存").performClick()

        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithText(title).fetchSemanticsNodes().isNotEmpty()
            } catch (_: IllegalStateException) {
                false
            }
        }
        composeRule.onNodeWithText(title).assertIsDisplayed()
    }

    /**
     * リマインダーを作成して削除し、一覧から消えることを確認する。
     *
     * 削除ボタンの特定: hasAncestor + useUnmergedTree を使って同じ行内の削除ボタンを特定する。
     * Material3 の ListItem は意味論的にマージするため、unmergedTree を使う必要がある。
     */
    @Test
    fun testDeleteReminder_removedFromList() {
        login()
        val title = "E2E_削除_${System.currentTimeMillis()}"

        // 作成
        composeRule.onNodeWithContentDescription("追加").performClick()
        composeRule.onNodeWithTag("input_title").performTextInput(title)
        composeRule.onNodeWithText("保存").performClick()
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithText(title).fetchSemanticsNodes().isNotEmpty()
            } catch (_: IllegalStateException) {
                false
            }
        }

        // タイトルの Y 座標を取得し、同一行にある削除ボタンをタップ
        // (hasAnyAncestor は LazyColumn に全行がマッチするため不適)
        val titleCenterY = composeRule
            .onAllNodesWithText(title, useUnmergedTree = true)
            .fetchSemanticsNodes()
            .first()
            .let { (it.boundsInRoot.top + it.boundsInRoot.bottom) / 2f }

        val deleteNodes = composeRule
            .onAllNodesWithContentDescription("削除", useUnmergedTree = true)
            .fetchSemanticsNodes()
        val targetIdx = deleteNodes
            .indexOfFirst { node ->
                val cy = (node.boundsInRoot.top + node.boundsInRoot.bottom) / 2f
                kotlin.math.abs(cy - titleCenterY) < 80f
            }
            .coerceAtLeast(0)

        composeRule.onAllNodesWithContentDescription("削除", useUnmergedTree = true)[targetIdx]
            .performClick()

        // 一覧から消えるまで待つ
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithText(title).fetchSemanticsNodes().isEmpty()
            } catch (_: IllegalStateException) {
                false
            }
        }
        assertTrue(
            "削除後もタイトルが残っています",
            composeRule.onAllNodesWithText(title).fetchSemanticsNodes().isEmpty()
        )
    }

    /** 既存リマインダーのタイトルを編集して更新されることを確認する。 */
    @Test
    fun testEditReminder_titleUpdated() {
        login()
        val original = "E2E_編集前_${System.currentTimeMillis()}"
        val updated  = "E2E_編集後_${System.currentTimeMillis()}"

        // 作成
        composeRule.onNodeWithContentDescription("追加").performClick()
        composeRule.onNodeWithTag("input_title").performTextInput(original)
        composeRule.onNodeWithText("保存").performClick()
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithText(original).fetchSemanticsNodes().isNotEmpty()
            } catch (_: IllegalStateException) {
                false
            }
        }

        // 行をタップして編集画面へ
        composeRule.onNodeWithText(original).performClick()

        // タイトルをクリアして更新
        composeRule.onNodeWithTag("input_title").performTextClearance()
        composeRule.onNodeWithTag("input_title").performTextInput(updated)
        composeRule.onNodeWithText("保存").performClick()

        // 更新後タイトルが表示されるまで待つ
        composeRule.waitUntil(TIMEOUT_MS) {
            try {
                composeRule.onAllNodesWithText(updated).fetchSemanticsNodes().isNotEmpty()
            } catch (_: IllegalStateException) {
                false
            }
        }
        composeRule.onNodeWithText(updated).assertIsDisplayed()
    }
}
