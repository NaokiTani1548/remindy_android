コードの全体像が把握できました。Javaの経験があるとのことなので、それをベースに段階的に説明します。

---

# Remindy 完全理解ロードマップ

## 全体像：このアプリは何をしているか

Remindy は **リマインダー＋学習カード管理アプリ**です。主な機能：

| 機能 | 説明 |
|------|------|
| リマインダー | 一回限り/毎日/毎週/毎月の通知スケジュールを管理 |
| 学習カード | Q&A形式 or 用語形式の暗記カードを管理 |
| サーバー同期 | バックエンドAPIとdelta syncで定期同期 |
| 認証 | ログイン/ログアウト機能 |

---

## Phase 1: Kotlin基礎（Java経験者向け — 1〜2日）

このアプリで頻出するKotlin構文を、Javaとの対比で説明します。

### 1-1. `data class`（= Javaの POJO/Record）

```kotlin
// Reminder.kt
data class Reminder(
    val id: String,
    val title: String,
    val schedule: Schedule,
    val enabled: Boolean,
)
```

Javaで書くと `equals()`, `hashCode()`, `toString()`, `copy()` を全部手書きですが、Kotlinでは `data class` の1行で自動生成されます。`val` は `final` フィールドと同等です。

### 1-2. `val` vs `var`

```kotlin
val x = 10    // final int x = 10;  （再代入不可）
var y = 20    // int y = 20;        （再代入可）
```

このプロジェクトでは基本的に `val`（不変）を使い、状態管理は `MutableStateFlow` に任せています。

### 1-3. Null安全（Javaの最大の違い）

```kotlin
val name: String = "hello"    // nullにできない
val name: String? = null      // null許容型

// 安全呼び出し
name?.length         // nullならnullを返す（NPEにならない）

// エルビス演算子
name ?: "default"    // nullなら"default"を使う

// スマートキャスト
if (name != null) {
    name.length      // ここではnon-nullとして使える
}
```

実例（`ReminderRepository.kt:37`）：
```kotlin
val existing = dao.findById(id) ?: return  // 見つからなければ即return
```

### 1-4. 拡張関数

```kotlin
// EntityMappers.kt にある例
fun ReminderEntity.toDomain(): Reminder = ...
```

Javaで言う `static Reminder toDomain(ReminderEntity entity)` ですが、`entity.toDomain()` のようにメソッドのように呼べます。

### 1-5. ラムダ式とトレイリングラムダ

```kotlin
// Java: list.stream().map(it -> it.toDomain()).collect(...)
// Kotlin:
list.map { it.toDomain() }
```

最後の引数がラムダの場合、`()` の外に `{}` で書けます。引数が1つなら `it` で参照。

### 1-6. `when` 式（= Javaの `switch` の強力版）

```kotlin
val type = when (this) {
    is Schedule.OneTime -> "ONE_TIME"
    is Schedule.Daily   -> "DAILY"
    is Schedule.Weekly  -> "WEEKLY"
    is Schedule.Monthly -> "MONTHLY"
}
```

### 1-7. `sealed class`（= 閉じた型階層）

```kotlin
sealed class Schedule {
    data class OneTime(val date: LocalDate, val time: LocalTime) : Schedule()
    data class Daily(val time: LocalTime) : Schedule()
    // ...
}
```

Javaの `sealed interface`(Java 17+) に近いです。`when` で全パターンを網羅できます。

### 1-8. コルーチン（`suspend`）

```kotlin
suspend fun create(title: String, schedule: Schedule) { ... }
```

`suspend` = 「この関数は非同期で実行できる」という印。Javaの `CompletableFuture` や RxJavaの代替で、通常の同期コードのように書けます。

---

## Phase 2: Androidアプリの起動フロー（2〜3日）

このアプリが起動してから画面が表示されるまでの流れ：

```
┌─────────────────────────────────────────────────────────┐
│ 1. RemindyApplication.onCreate()                        │
│    ├─ AppContainer を生成（全依存オブジェクトの組み立て）    │
│    ├─ 通知チャネル作成                                    │
│    └─ WorkManager で定期同期をスケジュール                  │
│                                                         │
│ 2. MainActivity.onCreate()                              │
│    ├─ 通知権限のリクエスト                                 │
│    ├─ AppContainer から ViewModelFactory 生成             │
│    └─ setContent { RemindyTheme { RemindyApp() } }      │
│                                                         │
│ 3. RemindyApp()                                         │
│    ├─ ログイン状態チェック                                 │
│    │   ├─ 未ログイン → LoginScreen                       │
│    │   └─ ログイン済 → MainScaffold                      │
│    └─ MainScaffold（Scaffold + NavHost）                 │
│        ├─ 下部タブ: リマインダー / 学習 / 設定              │
│        └─ 各画面への遷移                                  │
└─────────────────────────────────────────────────────────┘
```

### 2-1. Application クラス（`RemindyApplication.kt`）

Javaの `Application` と同じ。アプリ全体で1つだけ存在するシングルトン。ここで **DI（依存性注入）コンテナ** を初期化しています。

### 2-2. DI コンテナ（`AppContainer.kt`）

Dagger/Hiltを使わず**手動DI**を採用しています。読みやすくてシンプルです：

```
AppContainer
├── tokenStore     → トークン保存（DataStore）
├── api            → Retrofit HTTPクライアント
├── db             → Room データベース
├── authRepository → 認証処理
├── reminderRepository → リマインダーCRUD
├── studyRepository    → 学習カードCRUD
├── syncRepository     → サーバー同期
├── reminderAlarmScheduler → アラーム管理
└── studyAlarmScheduler    → 学習通知管理
```

### 2-3. Activity（`MainActivity.kt`）

従来のAndroidでは複数のActivityやFragmentを使いますが、このアプリは **Single Activity + Jetpack Compose** 構成です。`setContent {}` でComposeのUIツリーを開始します。

---

## Phase 3: アーキテクチャパターン — MVVM（3〜5日）

このアプリは **MVVM（Model-View-ViewModel）** パターンを使っています：

```
┌──────────────────────────────────────────┐
│           View (Compose画面)              │
│  ReminderListScreen, StudyEditScreen等   │
│  → UIの描画とユーザー入力の受付            │
└──────────┬───────────────────────────────┘
           │ 状態を監視 (collectAsState)
           │ イベント発行 (vm.create(), vm.delete())
┌──────────▼───────────────────────────────┐
│         ViewModel                        │
│  ReminderViewModel, StudyViewModel等     │
│  → UIの状態管理、ビジネスロジック            │
└──────────┬───────────────────────────────┘
           │ データ操作 (suspend関数)
┌──────────▼───────────────────────────────┐
│         Repository                       │
│  ReminderRepository, StudyRepository等   │
│  → データアクセスの抽象化                    │
└──────────┬───────────────────────────────┘
           │
┌──────────▼───────────────────────────────┐
│    Data Source (Room DB / Retrofit API)   │
│  RemindyDatabase, RemindyApi             │
└──────────────────────────────────────────┘
```

### 実例：リマインダーの一覧表示の流れ

1. **DB** (`ReminderDao`) が `Flow<List<ReminderEntity>>` を返す
2. **Repository** が `Entity → Domain Model` に変換
3. **ViewModel** が `StateFlow<List<Reminder>>` として保持
4. **Screen** が `collectAsState()` で購読し、変更があれば自動で再描画

```kotlin
// ViewModel (ReminderViewModel.kt:20-22)
val reminders: StateFlow<List<Reminder>> =
    repository.observeReminders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
```

---

## Phase 4: Jetpack Compose — UIの書き方（5〜7日）

従来のAndroidはXMLでレイアウトを書きましたが、Composeでは **Kotlinコードで直接UI** を書きます。

### 基本概念

```kotlin
@Composable   // この関数はUI部品であることを示す
fun Greeting(name: String) {
    Text("Hello, $name!")  // ← これがUI要素
}
```

- `@Composable` 関数 = UIコンポーネント（Reactのコンポーネントと同概念）
- **状態が変わると自動で再描画**（リコンポジション）される

### 状態管理

```kotlin
// remember: この値をリコンポジション間で保持
var text by remember { mutableStateOf("") }

// ViewModel の状態を Compose で監視
val reminders by viewModel.reminders.collectAsState()
```

### このアプリの画面構成

```
RemindyApp
├── LoginScreen         — ログイン画面
└── MainScaffold        — メイン画面
    ├── NavigationBar    — 下部タブ
    ├── ConnectionStatusBar — サーバー接続状態
    └── NavHost          — 画面遷移コンテナ
        ├── ReminderListScreen  → ReminderEditScreen
        ├── StudyListScreen     → StudyEditScreen
        └── SettingsScreen
```

---

## Phase 5: データ層の理解（7〜10日）

### 5-1. Room（SQLiteの抽象化レイヤー）

```
Entity（テーブル定義）   → data/local/entity/
DAO（クエリ定義）        → data/local/dao/
Database（DB定義）       → data/local/RemindyDatabase.kt
```

JavaのJPAに似ていますが、アノテーションベースでSQLを書きます。

### 5-2. Retrofit（HTTPクライアント）

```
API定義     → data/remote/RemindyApi.kt
DTOクラス   → data/remote/dto/
認証        → data/remote/AuthInterceptor.kt
```

JavaのRetrofitと全く同じ使い方です。

### 5-3. パッケージ構成まとめ

```
com.example.remindy/
├── domain/model/       ← ドメインモデル（純粋なデータクラス）
├── data/
│   ├── local/          ← Room DB（Entity, DAO, Database）
│   ├── remote/         ← Retrofit API（API定義, DTO）
│   ├── repository/     ← Repository（データ操作の窓口）
│   └── mapper/         ← Entity ↔ Domain の変換
├── ui/
│   ├── reminder/       ← リマインダー画面（Screen + ViewModel）
│   ├── study/          ← 学習カード画面
│   ├── settings/       ← 設定画面
│   ├── auth/           ← ログイン画面
│   ├── connection/     ← 接続状態管理
│   ├── common/         ← 共通UIコンポーネント
│   └── theme/          ← テーマ定義
├── notification/       ← 通知・アラーム管理
├── sync/               ← バックグラウンド同期（WorkManager）
├── di/                 ← DIコンテナ
└── util/               ← ユーティリティ
```

---

## Phase 6: 通知・同期の仕組み（10〜12日）

### 通知

- `AlarmManager` で指定時刻にアラーム設定
- `BroadcastReceiver` でアラーム受信 → 通知表示
- `BootReceiver` で端末再起動時にアラーム再設定

### バックグラウンド同期

- `WorkManager` で15分ごとに `SyncWorker` を実行
- アプリ起動時にも即時同期
- delta sync：最後の同期以降の変更分のみ送受信

---

## 推奨する読む順番

| 順番 | ファイル | 学べること |
|------|---------|-----------|
| 1 | `domain/model/Reminder.kt`, `Schedule.kt`, `StudyItem.kt` | data class, sealed class |
| 2 | `RemindyApplication.kt` | Applicationの役割 |
| 3 | `di/AppContainer.kt` | 手動DI、依存関係の全体像 |
| 4 | `MainActivity.kt` | Single Activity構成 |
| 5 | `ui/RemindyApp.kt` | Compose, Navigation, 画面遷移 |
| 6 | `ui/reminder/ReminderViewModel.kt` | MVVM, コルーチン, StateFlow |
| 7 | `ui/reminder/ReminderListScreen.kt` | Compose UI の書き方 |
| 8 | `data/local/entity/ReminderEntity.kt` | Room Entity |
| 9 | `data/local/dao/ReminderDao.kt` | Room DAO, SQL |
| 10 | `data/repository/ReminderRepository.kt` | Repository パターン |
| 11 | `data/remote/RemindyApi.kt` | Retrofit API定義 |
| 12 | `notification/ReminderAlarmScheduler.kt` | Android通知 |
| 13 | `sync/SyncWorker.kt` | WorkManager, 同期処理 |

---

## 具体的にどこから始めればよいか

**今日やること**: まずPhase 1（Kotlin構文）をざっと理解した上で、上の表の1〜5番のファイルを実際に開いて読んでみてください。

何か特定のファイルやコンセプトについて深掘りしたい場合は、「〇〇について詳しく教えて」と聞いてもらえれば、そのファイルのコードを読みながら1行ずつ解説します。