# WhatToEat2

> WhatToEat 的现代化跨平台重构版本，基于 **Kotlin Multiplatform + Compose Multiplatform** 构建，同时支持 Android 与 iOS。

## 缘起

开发初衷是为了解决"今天吃什么？"这个永恒的难题。不过根据实际体验，即使有了随机选择，人们依然会犹豫不决——所以现在它已不再是一个单纯的随机工具，而是逐步加入更多实用功能。欢迎联系维护者投稿你的想法！

> 目前服务范围限于西电校内，后续可能扩展。

## 功能

| 模块 | 说明 |
|------|------|
| 随机食物 | 从食物库中随机抽取，支持 2 秒滚动动画，解决选择困难 |
| 菜品浏览 | 浏览食物详情（评分、图片、标签、位置），按名称/标签搜索 |
| 菜品点评 | 对食物进行评分与评论 |
| 实用网站 | 校内常用网站快捷入口（如测速、教务等） |
| 设置 | 主题切换等个性化配置 |

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI 框架 | Compose Multiplatform (Material 3) |
| 跨平台 | Kotlin Multiplatform (KMP) |
| 架构 | MVVM + Repository |
| 状态管理 | StateFlow |
| 数据库 | Room 2.8.4 (KMP) |
| 轻量存储 | Multiplatform Settings |
| 图片加载 | Coil3 (Compose Multiplatform) |
| 导航 | Navigation Compose (类型安全路由) |
| 网络 | Ktor |
| 序列化 | Kotlinx Serialization |

## 项目结构

```
WhatToEat2/
├── shared/                          # 跨平台共享代码
│   └── src/
│       ├── commonMain/              # 所有业务逻辑、UI、数据库
│       │   └── kotlin/com/rubp/whattoeat/
│       │       ├── data/
│       │       │   ├── local/       # Room Entity / DAO / Database
│       │       │   └── repository/  # 数据仓库层
│       │       ├── domain/          # 领域模型
│       │       └── ui/
│       │           ├── components/  # 可复用 UI 组件
│       │           ├── screens/     # 页面（Home/Eat/Edit/Settings...）
│       │           ├── theme/       # 主题与色彩
│       │           └── viewmodel/   # ViewModel 层
│       ├── androidMain/             # Android 平台实现 (expect/actual)
│       └── iosMain/                 # iOS 平台实现 (expect/actual)
└── androidApp/                      # Android 宿主壳工程
```

## 构建与运行

### 环境要求

- **Android Studio** (Ladybug 或更新版本)
- **Kotlin** 2.0+
- **Gradle** 8.x (Wrapper 自带)
- **JDK** 11+
- **Xcode** (iOS 构建需要，macOS 上)

### Android

```bash
./gradlew :androidApp:assembleDebug
# 或将项目导入 Android Studio 直接运行
```

### iOS

```bash
# 在 macOS 上
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
# 然后在 Xcode 中打开 iosApp 运行
```

## 架构

```
┌─────────────────────────────────────┐
│  UI Layer (Compose Screens)         │
│  ┌──────────┐ ┌──────────┐          │
│  │ EatScreen│ │HomeScreen│  ...     │
│  └────┬─────┘ └────┬─────┘          │
├───────┼────────────┼────────────────┤
│  ViewModel Layer                    │
│  ┌────┴────────────┴─────┐          │
│  │  StateFlow<UiState>   │          │
│  └───────────┬───────────┘          │
├──────────────┼──────────────────────┤
│  Repository Layer                   │
│  ┌───────────┴───────────┐          │
│  │ ConfigRepo / FoodRepo │          │
│  └───────┬────────┬──────┘          │
├──────────┼────────┼─────────────────┤
│  Data Layer                         │
│  ┌───────┴───┐ ┌──┴───────────┐     │
│  │  Room DB  │ │ Multiplatform│     │
│  │           │ │ Settings     │     │
│  └───────────┘ └──────────────┘     │
└─────────────────────────────────────┘
```
