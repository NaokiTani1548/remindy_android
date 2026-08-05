package com.example.remindy.ui.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.HealthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ConnectionStatus {
    data object Connected : ConnectionStatus
    data object Disconnected : ConnectionStatus
    data object Connecting : ConnectionStatus
    data class Error(val message: String) : ConnectionStatus
}

class ConnectionViewModel(private val healthRepository: HealthRepository) : ViewModel() {

    private val _status = MutableStateFlow<ConnectionStatus>(ConnectionStatus.Disconnected)
    val status: StateFlow<ConnectionStatus> = _status.asStateFlow()

    init { startPolling() }

    /**
     * ポーリングループは1本のみ。
     * - 通常時: 30 秒間隔で /health を確認してランプを更新
     * - Connecting 時: 5 秒間隔に切り替え、200 が返った瞬間に Connected へ遷移
     *
     * connect() / cancelConnect() はステータスを書き換えるだけで、
     * 別コルーチンを立てない。ループが唯一の書き換え源なので競合しない。
     */
    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                val wasConnecting = _status.value is ConnectionStatus.Connecting
                val isHealthy = healthRepository.checkHealth()

                // _status.update は現在値を見て原子的に書き換えるため、
                // cancelConnect() との競合を防げる
                _status.update { current ->
                    when {
                        isHealthy -> ConnectionStatus.Connected
                        current is ConnectionStatus.Connecting -> ConnectionStatus.Connecting // 待機継続
                        else -> ConnectionStatus.Disconnected
                    }
                }

                // Connecting 中は高頻度ポーリング、それ以外は低頻度
                delay(if (wasConnecting) 5_000L else 30_000L)
            }
        }
    }

    /**
     * 接続ボタン押下。Connecting にするだけ — ポーリングループが検知して遷移する。
     * 5 分経っても繋がらなければタイムアウトエラー。
     */
    fun connect() {
        if (_status.value is ConnectionStatus.Connecting) return
        _status.value = ConnectionStatus.Connecting
        viewModelScope.launch {
            delay(5 * 60 * 1_000L)
            if (_status.value is ConnectionStatus.Connecting) {
                _status.value = ConnectionStatus.Error("サーバーへの接続に失敗しました")
            }
        }
    }

    /** キャンセルボタン押下。ステータスをリセットするだけ。 */
    fun cancelConnect() {
        _status.value = ConnectionStatus.Disconnected
    }
}
