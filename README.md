# Item Manager — SwiftUI → Jetpack Compose

An Android port of a SwiftUI item-management app, built with **Kotlin** and **Jetpack Compose**.

The app manages a sorted list of items with favorite states, detail screens, creation, deletion, persistence, adaptive layouts, animations, localization, and a custom iOS-inspired experience.

This repository contains **three versions inside one app**, selectable from a launcher screen:

* **Minimal** — a faithful 1:1 SwiftUI-to-Compose port.
* **Enhanced** — a more polished Android version with persistence, search, adaptive UI, undo, animations, and localization.
* **Cupertino** — the same app hand-skinned to feel close to native iOS using Compose primitives.

---

## Demo

### Minimal

A faithful translation of the original SwiftUI brief: Italian strings, in-memory state, sorted list, detail screen, add, delete, and favorite toggle.

https://github.com/user-attachments/assets/91cc59dd-4c7f-4551-85b5-70a15046829f

---

### Enhanced

A more production-flavoured Android version with Room persistence, search, filters, undo, animations, haptics, dark mode, and language switching.

https://github.com/user-attachments/assets/239a7740-6a26-44bd-aa5a-6d5225cb5f3c

---

### Enhanced — Foldable / Wide Screen

The Enhanced version adapts into a two-pane list-detail experience on tablets, foldables, or landscape phones.

https://github.com/user-attachments/assets/4a089bfa-36b1-4cac-a531-789082ea16a6

---

### Cupertino

A Compose-built iOS-inspired version with grouped lists, iOS-style navigation, custom colors, sheets, swipe actions, and native-like interactions.

https://github.com/user-attachments/assets/929e8cd5-899a-445d-90cf-7ec949de285b

---

## Versions at a glance

| Version       | Purpose                          | State            | UI style           | Key features                                                          |
| ------------- | -------------------------------- | ---------------- | ------------------ | --------------------------------------------------------------------- |
| **Minimal**   | 1:1 SwiftUI port                 | In-memory        | Simple Compose     | Sorted list, detail, add, delete, favorite                            |
| **Enhanced**  | Production-style Android version | Room + DataStore | Material 3         | Persistence, search, filters, undo, adaptive layout, i18n, animations |
| **Cupertino** | iOS-inspired Compose experiment  | Shared app logic | Custom iOS-like UI | Large titles, grouped lists, iOS navigation, sheets, swipe delete     |

All three versions are built from a small shared core so the implementations can be compared side by side.

The launcher also includes an **About the developer** screen.

---

## Running it

### Requirements

* Android Studio on a canary/preview channel compatible with:

  * **AGP 9.1.1**
  * **Gradle 9.3.1**
  * **compileSdk 36**
* **JDK 17**
* Android emulator or physical device on **API 28+**

### Steps

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration.
4. On launch, choose one of the three versions:

   * **Minimal**
   * **Enhanced**
   * **Cupertino**
5. To test the adaptive two-pane layout, run the Enhanced version on:

   * a tablet,
   * a foldable,
   * or a phone rotated to landscape.

> This project deliberately targets the stable **compileSdk 36** instead of the preview SDK 37 so it can build on a normally configured machine. See [Toolchain notes](#toolchain-notes-why-it-builds-the-way-it-does).

---

## SwiftUI → Compose mapping

| SwiftUI                           | Android                                                |
| --------------------------------- | ------------------------------------------------------ |
| `@Observable ItemViewModel`       | `Item` data class + `StateFlow` in ViewModels          |
| `ItemsListViewModel`              | `MinimalItemsViewModel` / `EnhancedItemsViewModel`     |
| `NavigationSplitView`             | Navigation 3 stack / custom adaptive list-detail scene |
| `ContentView` list ↔ detail       | `NavDisplay` with list and detail entries              |
| `.sheet(detent: .medium)`         | `ModalBottomSheet`                                     |
| `FavoriteButton`                  | `Star` / `StarBorder` icon toggle                      |
| `localizedCaseInsensitiveCompare` | `java.text.Collator` comparator                        |
| `#Preview`                        | `@Preview` composables                                 |

---

### Navigation

The project uses **Navigation 3** throughout.

The Enhanced adaptive layout is implemented as a custom Navigation 3 `Scene` + `SceneStrategy`, driven by the window size class.

---

## Testing

The project includes unit, UI, and screenshot coverage.

### Unit tests

Located in:

```text
app/src/test
```

Coverage includes:

* sorting comparator,
* `MinimalItemsViewModel`,
* `EnhancedItemsViewModel`,
* delete → restore undo flow,
* search,
* filtering,
* fake in-memory repository behavior.

### UI tests

Located in:

```text
app/src/androidTest
```

Includes a Compose UI test for the Minimal list:

* renders sorted items,
* validates the add flow,
* confirms trimmed item names are reported.

### Screenshot tests

Screenshot tests use:

* Roborazzi,
* Robolectric,
* committed golden images.

Located in:

```text
app/src/test/.../screenshots
app/src/test/screenshots
```

They render and compare:

* launcher,
* Minimal screens,
* Enhanced screens,
* light mode,
* dark mode,
* Italian locale,
* Spanish locale.

No physical device is required for screenshot verification, so they can run in CI.

### Test commands

```bash
./gradlew :app:testDebugUnitTest
```

Runs unit and screenshot tests.

```bash
./gradlew :app:connectedDebugAndroidTest
```

Runs UI tests. Requires a device or emulator.

```bash
./gradlew :app:recordRoborazziDebug
```

Records screenshot goldens.

```bash
./gradlew :app:verifyRoborazziDebug
```

Verifies screens against committed goldens.

---

## Toolchain notes: why it builds the way it does

This project was scaffolded on a bleeding-edge Android toolchain, which required a few deliberate decisions to keep the repository reliably buildable.

### 1. AGP 9 built-in Kotlin + KSP

AGP 9 compiles Kotlin itself.

KSP, used by Room, registers generated sources through the `kotlin.sourceSets` DSL. Built-in Kotlin rejects that by default, so this project sets:

```properties
android.disallowKotlinSourceSets=false
```

in `gradle.properties`.

### 2. compileSdk 36 instead of preview SDK 37

The newest `lifecycle` and `material3-adaptive` versions require **compileSdk 37**, which is still a preview SDK.

To keep the project buildable for anyone who clones it, this repository targets the stable **compileSdk 36** and pins dependencies accordingly.

The adaptive list-detail experience is hand-rolled from the official Navigation 3 list-detail scene recipe using the window size class instead.

### 3. Roborazzi instead of the official screenshot plugin

Google’s `com.android.compose.screenshot` plugin applies and compiles previews on this toolchain, but preview discovery finds zero tests under AGP 9.1.1, even with a trivial `@Preview`.

Roborazzi + Robolectric were used instead because they render screens as JVM tests, making screenshot verification device-free and reliable in CI.

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
