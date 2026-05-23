# Rewards - 高性能Minecraft奖励插件

## 特性
- ✅ 高性能内存缓存 + 异步批量写入
- ✅ 支持 SQLite (单服) 和 MySQL (多服)
- ✅ 完全兼容 Folia 核心
- ✅ 防重复领取机制 (跨服原子操作)
- ✅ 玩家独立调度器，无全局锁
- ✅ 懒重置机制 (登录/GUI打开时重置)
- ✅ 支持 Vault 经济系统
- ✅ 友好的 GUI 界面

## 安装
1. 将 `Rewards.jar` 放入服务器的 `plugins` 文件夹
2. 启动服务器生成配置文件
3. 根据需要编辑 `plugins/Rewards/config.yml`
4. 重新加载插件: `/rewardsreload`

## 配置
- **数据库类型**: 支持 `sqlite` (默认) 和 `mysql`
- **奖励类型**: 
  - `money:金额` - 给予玩家金钱
  - `item:物品ID:数量` - 给予玩家物品
  - `command:命令` - 执行服务器命令
- **时间单位**: 所有时间以秒为单位

## 命令
- `/rewards` - 打开奖励GUI
- `/rewards reload` - 重新加载配置 (OP)
- `/rewards reset [daily|weekly|all]` - 重置玩家数据 (OP)

## 权限
- `rewards.use` - 使用奖励系统 (默认 true)
- `rewards.reload` - 重新加载配置 (OP)
- `rewards.reset` - 重置玩家数据 (OP)

## 依赖
- **必需**: Vault (经济支持)
- **可选**: PlaceholderAPI, Redis (跨服缓存)

## 兼容性
- Paper 1.20+
- Folia 1.20.4+
- 支持 500+ 玩家同时在线

## 性能优化
- 无全局循环，每个玩家独立计时
- 异步数据库操作，不影响游戏性能
- 内存缓存减少数据库查询
- 批量写入降低 I/O 负载
- 索引优化提高查询速度
