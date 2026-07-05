这是一个 Kotlin Multiplatform（Kotlin 多平台）项目，支持 Android 和 iOS 目标平台。

- [/iosApp](https://www.google.com/search?q=./iosApp/iosApp) 包含一个 iOS 应用程序。即使你正在使用 Compose Multiplatform 共享 UI，你也需要这个专属于 iOS 应用的入口点。这也是你在项目中添加 SwiftUI 代码的地方。
- [/shared](https://www.google.com/search?q=./shared/src) 用于存放在 Compose Multiplatform 应用程序之间共享的代码。它包含几个子文件夹：
  - [commonMain](https://www.google.com/search?q=./shared/src/commonMain/kotlin) 用于存放适用于所有目标平台的通用代码。
  - 其他文件夹用于存放仅针对特定平台进行编译的 Kotlin 代码。例如，如果你想在 Kotlin 应用的 iOS 部分使用苹果的 CoreCrypto，那么 [iosMain](https://www.google.com/search?q=./shared/src/iosMain/kotlin) 文件夹就是编写此类调用的正确位置。同样地，如果你想修改桌面端（JVM）的特定部分，[jvmMain](https://www.google.com/search?q=./shared/src/jvmMain/kotlin) 文件夹就是合适的位置。

### 运行应用程序

请使用 IDE 工具栏中运行挂件（run widget）所提供的运行配置。你也可以使用以下命令和选项：

- Android 应用：`./gradlew :androidApp:assembleDebug`
- iOS 应用：在 Xcode 中打开 [/iosApp](https://www.google.com/search?q=./iosApp) 目录并在其中运行。

### 运行测试

使用 IDE 编辑器边栏（gutter）中的运行按钮，或者使用 Gradle 任务来运行测试：

- Android 测试：`./gradlew :shared:testAndroidHostTest`
- iOS 测试：`./gradlew :shared:iosSimulatorArm64Test`

了解更多关于 [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) 的信息…