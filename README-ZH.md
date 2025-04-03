# Nyx: 专门破坏GTNH平衡

![Version](https://img.shields.io/badge/GTNH_Version-2.7.3-blue)

[English](README.md) | 中文

WIP重构阶段。

神人模组之胡乱改，最早是受23年TST(GTCM)启发，想整点更抽象的自己玩
2.5.0摸到星门之后对NH没兴趣了，现在弃坑一年后用Kotlin重构了现在这个Mod

技术力和平衡是不存在的，自己玩的爽了就行。

## 功能

功能可能需要在`nyx.cfg`配置文件中启用。

- 添加了IV~UMV无线激光，以及所有无线能源仓的简单配方。启用配置: `RECIPE_EASY_WIRELESS`

![easy_wireless](img/easy_wireless.png)

### 注意：关于ID冲突

我不知道别的私货是怎么占MTE的ID的，大概我只能保证和官方ID不冲突，和我常用的不冲突，你可以在冲突的时候，到`config/nyx.cfg`里调整ID偏移量，
重启游戏。我设置的日志也会显示ID冲突的具体目标。

上面表格中的Mod与对应版本在我更新2.7.3版本的时候没有冲突

| Mod                                                                              | Version        |
|:---------------------------------------------------------------------------------|----------------|
| [Twist-Space-Technology-Mod](https://github.com/Nxer/Twist-Space-Technology-Mod) | 0.6.14         |
| [BoxPlusPlus](https://github.com/RealSilverMoon/BoxPlusPlus)                     | 1.9.3          |
| [Programmable-Hatches-Mod](https://github.com/reobf/Programmable-Hatches-Mod)    | v0.1.2p28-beta |
| [AE2Things](https://github.com/asdflj/AE2Things)                                 | 1.1.8          |

