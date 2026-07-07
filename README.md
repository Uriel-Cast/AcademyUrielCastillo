# Item Manager — SwiftUI → Jetpack Compose

An Android port of the SwiftUI item-management app, written in Kotlin with
Jetpack Compose. The app displays a sorted list of items (each with a name and a
favorite state) plus a detail screen, and every change is reflected everywhere
automatically.

The repository ships **two versions in a single app**, selected from a launcher
screen:

| | What it is |
|---|---|
| **Minimal** | A faithful 1:1 port of the original SwiftUI app — exactly what the brief asks for, in Italian, in-memory. |
| **Enhanced** | Our take: the same app, persisted and considerably more polished (adaptive panes, undo, search, animations, i18n, …). |

Both are built from a small shared core, so they can be compared side by side.

---

## Running it

Requirements:

- **Android Studio** on the canary/preview channel that matches this toolchain
  (**AGP 9.1.1 / Gradle 9.3.1 / compileSdk 36**), and **JDK 17**.
- An emulator or device on **API 28+**.

Steps:

1. Open the project and let Gradle sync.
2. Run the `app` configuration.
3. On launch, pick **Minimal** or **Enhanced**.
4. To see the **adaptive two-pane** layout, run on a tablet/foldable **or rotate a
   phone to landscape** — the Enhanced version splits into list + detail.

> This project deliberately targets the **stable compileSdk 36** rather than the
> preview SDK 37, so it builds on a normally-configured machine. See
> [Toolchain notes](#toolchain-notes-why-it-builds-the-way-it-does).

---

## The two versions

### Minimal — `com.urielcast.academytest2026.minimal`

A direct translation of the SwiftUI app, kept intentionally small:

- Sorted list (locale-aware, case-insensitive name + stable `creationIndex`
  tie-break), detail screen, add via a bottom sheet, favorite toggle, delete.
- Italian strings, matching the SwiftUI source (`Oggetti`, `Aggiungi`, `Preferito`…).
- **In-memory** state (`ViewModel` + `StateFlow`) — no persistence, exactly like
  the original.
- A `@Preview` on every composable.

### Enhanced — `com.urielcast.academytest2026.enhanced`

Everything Minimal does, made persistent and production-flavoured:

- **Room** persistence — data survives process death; Room is the single source
  of truth, exposed as a `Flow`.
- **Adaptive list-detail** — a two-pane layout on wide screens (the Android form
  of SwiftUI's `NavigationSplitView`), collapsing to single-pane on a phone.
- **Swipe-to-delete with Undo** (snackbar), shared with the detail screen's delete.
- **Search** and **All / Favorites** filters.
- **Rename** an item from the detail screen.
- **Animated favorite** star (spring + colour crossfade) with **haptics**.
- **Shared-element transition** — the item name morphs between the row and the
  detail title.
- **Predictive back** (via Navigation 3 transition specs).
- **Dark theme** toggle and **language switch** (Italian / English / Spanish),
  persisted with DataStore.

---

## SwiftUI → Compose mapping

| SwiftUI | Android (this project) |
|---|---|
| `@Observable ItemViewModel` | `Item` data class + `StateFlow` in the ViewModels |
| `ItemsListViewModel` | `MinimalItemsViewModel` / `EnhancedItemsViewModel` |
| `NavigationSplitView` | Navigation 3 stack (Minimal) / adaptive list-detail Scene (Enhanced) |
| `ContentView` list ↔ detail | `NavDisplay` with list/detail entries |
| `.sheet(detent: .medium)` | `ModalBottomSheet` |
| `FavoriteButton` (`star.fill`/`star`) | `Star` / `StarBorder` icon toggle |
| `localizedCaseInsensitiveCompare` | `java.text.Collator` comparator |
| `#Preview` | `@Preview` composables |

---

## Architecture

- **`core/`** — the shared domain: `Item`, the display comparator, and the seed
  data. No Android dependencies; unit-tested.
- **Minimal** — a `ViewModel` holding a `MutableStateFlow<List<Item>>`; the list
  and detail observe the same state, mirroring SwiftUI's `@Observable`.
- **Enhanced** — MVI-lite: a single `StateFlow<EnhancedUiState>` combines the
  persisted items with the transient search/filter state. Room is the source of
  truth behind an `ItemsRepository` (`Flow`-based). Dependencies are wired by a
  small manual `AppContainer` on the `Application` — Hilt's seams without the
  weight (swapping in Hilt later would only touch the providers). Settings live
  in **DataStore**.
- **Navigation 3** throughout; the Enhanced adaptive layout is a custom Nav3
  `Scene` + `SceneStrategy` driven by the window size class.

---

## Testing

- **Unit tests** (`app/src/test`): the sorting comparator, `MinimalItemsViewModel`,
  and `EnhancedItemsViewModel` — including **delete → restore (undo)**, search,
  and filtering, against an in-memory fake repository.
- **UI test** (`app/src/androidTest`): a Compose test for the Minimal list
  (renders sorted items; the add flow reports the trimmed name).
- **Screenshot tests** (`app/src/test/.../screenshots`, goldens in
  `app/src/test/screenshots`): Roborazzi + Robolectric render the launcher and the
  Minimal/Enhanced screens in the JVM and compare them against committed golden
  images — including light/dark and the Italian/Spanish locales. No device needed,
  so they run in CI.
- Every screen and interaction was additionally verified by hand on an emulator
  (phone + landscape two-pane, dark mode, Spanish).

```bash
./gradlew :app:testDebugUnitTest            # unit + screenshot tests
./gradlew :app:connectedDebugAndroidTest    # UI tests (needs a device/emulator)
./gradlew :app:recordRoborazziDebug         # (re)record the screenshot goldens
./gradlew :app:verifyRoborazziDebug         # verify screens against the goldens
```

---

## Toolchain notes (why it builds the way it does)

This project was scaffolded on a bleeding-edge toolchain, which needed two
deliberate decisions to stay reliably buildable:

1. **AGP 9 built-in Kotlin + KSP.** AGP 9 compiles Kotlin itself. KSP (used by
   Room) registers its generated sources through the `kotlin.sourceSets` DSL,
   which built-in Kotlin rejects by default, so
   `android.disallowKotlinSourceSets=false` is set in `gradle.properties`.
2. **compileSdk 36, not 37.** The newest `lifecycle` (2.11) and
   `material3-adaptive` (1.3, which contains `adaptive-navigation3` and its
   `ListDetailSceneStrategy`) require **compileSdk 37 — an unreleased preview
   SDK**. To keep the project building for anyone who clones it, dependencies are
   pinned to the versions that target the stable **compileSdk 36**, and the
   adaptive list-detail is hand-rolled from the official Nav3 *scenes-listdetail*
   recipe using the window size class instead.
3. **Screenshot tests via Roborazzi, not the official plugin.** Google's
   `com.android.compose.screenshot` plugin applies and compiles the previews on
   this toolchain, but its preview *discovery* finds zero tests under AGP 9.1.1
   (even a trivial `@Preview`), so golden capture never runs. Roborazzi +
   Robolectric render the screens as plain JVM tests instead — device-free and
   unaffected by that discovery bug.

---

## Using AI

AI (Claude Code) was used throughout, as a driven pair rather than an autopilot:

- **Planning first.** The work was broken into phases (shared core → Minimal →
  launcher → Enhanced persistence → UX → adaptive/animations → tests/docs), each
  landing as its own commit so the history reads as a progression.
- **Grounded in official guidance.** It pulled in Google's official
  [`android/skills`](https://github.com/android/skills) (Navigation 3, adaptive,
  Material 3 styles, edge-to-edge, testing) and followed their recipes rather
  than inventing APIs.
- **Verified, not assumed.** Every phase was built and exercised on a real
  emulator (including the tablet two-pane, dark mode and localisation) before
  moving on; dependency versions were checked against Maven metadata rather than
  guessed.
- **Steered on judgement calls.** The interesting decisions — dodging the preview
  SDK, the AGP-9/KSP interaction, choosing manual DI over Hilt, and verifying
  *undo* with a deterministic unit test instead of a flaky UI tap — were made and
  documented explicitly rather than left implicit.
