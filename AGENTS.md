# AGENTS.md

本文件面向在本仓库工作的所有 AI Agent，说明项目背景与必须遵守的协作规则。

## 项目结构

| 模块 | 说明 |
| --- | --- |
| `admin-controller` | 后端服务（Spring Boot 3.x + MyBatis-Plus + Sa-Token + Thymeleaf），内含**旧版前后端不分离前端**，开发端口 8801 |
| `admin-common` | 公共模块（实体 / 工具 / 通用配置） |
| `admin-generator` | 代码生成器（独立模块，支持生成新版 web 的 Vue3 页面，见下方「代码生成器」） |
| `web` | **新版前端**（Vue 3 + Vite + TypeScript + Element Plus + Pinia + vue-router），开发端口 5173 |

## 当前任务：旧前端迁移至 web（所有 Agent 必读）

正在将 `admin-controller` 内嵌的旧前端（`src/main/resources/templates/` 下的 Thymeleaf 页面，Vue2 + Element UI 技术栈）迁移重构到 `web`（Vue3 + Vite 前后端分离）。

### 硬性规则

1. **旧前端只读保留**：`admin-controller/src/main/resources/templates/` 与 `src/main/resources/static/` 下的旧前端页面/资源是迁移对照基准，**禁止修改、禁止删除**（迁移完成后是否清理由用户决定）。改造任何 web 页面前，先阅读对应旧页面确认功能。
2. **web 目录自由重构**：`web/` 是新前端，允许自由删除、修改、重构、重写，无需事先批准。
3. **验收标准：功能对齐 + UI 现代化**——旧页面功能需在新前端完整可用、行为一致；界面用 Element Plus 重新组织优化，不要求像素级还原。
4. **后端接口只增不改**：旧页面需保留可运行用于对照，因此现有接口的路径与返回结构必须保持兼容；新前端缺数据/缺能力时，在 `admin-controller` 中**新增**接口。
5. **联调约定**：后端统一返回 `{code, msg, data}`（code=200 成功，401 未登录）；认证用 Sa-Token，token 名 `saToken`（cookie 与请求头同名传递）；web 通过 vite 代理 `/api`（转发时去掉前缀）、`/captcha`、`/show`、`/upload` 访问后端 8801。

### 旧页面 → 新页面对照表

| 旧页面（templates/） | 新页面（web/src/views/） |
| --- | --- |
| login.html | login/index.vue |
| index.html（布局外壳） | layout/ |
| welcome.html | welcome/index.vue |
| system/user.html | system/user/index.vue |
| system/role.html | system/role/index.vue |
| system/menu.html | system/menu/index.vue |
| system/btn.html | system/btn/index.vue |
| page/system/sysLoginInfo_list.html | log/loginInfo.vue |
| page/system/sysOperLog_list.html | log/operLog.vue |
| page/tinymce/tinymceDemo.html | tinymce/demo.vue |
| icon.html | icon/index.vue |
| error/404.html、500.html | error/404.vue、error/500.vue |

## 代码生成器（admin-generator）

运行 `com.lrs.generator.MyGenCode`（交互式），页面模式三选一：
- `0` 仅后端代码（entity/mapper/service/controller）
- `1` 追加旧版页面（Vue2+Thymeleaf，写到 admin-generator 下，对照用）
- `2` 追加新版页面：直接写 `web/src/views/{模块}/{实体}/index.vue`（Vue3 + Element Plus + useCrud + v-permission），并在 `admin-generator/gen-sql/` 产出菜单登记 SQL

模式 2 的使用闭环：生成的 controller 落在 `com.lrs.core.{模块}`（需复制到 admin-controller 编译运行）→ 执行 menu.sql（菜单 URL 形如 `{模块}/{实体}/list`，前端路由解析会自动去掉尾部 `/list` 找到 `index.vue`）→ 角色管理勾选授权。表字段 comment 用作列标题/表单 label；`isDeleted` 与 `*Time` 字段不进表单。

## 常用命令

- 后端启动：运行 `admin-controller` 模块的 `com.lrs.core.Application`（本地需 MySQL `admin` 库；开发端口 8801）
- 前端开发：`cd web && pnpm dev`；构建：`cd web && pnpm build`

## 环境约定

- 后端接口前缀：业务接口直接以 `/system/**`、`/login` 等裸路径提供服务，仅页面路由由旧模板系统承担
- 文件上传与展示：`/upload` 上传、`/show/**` 访问（upload 根目录见 `admin.common.upload.root` 配置）
- 开发代理后端地址通过 `web/.env.development` 的 `VITE_BACKEND_URL` 统一管理
- 菜单图标：`sys_menu.icon` 存 **Element Plus 图标名**（如 `Setting`），全量清单见 web 前端 `/icon` 页与 `web/src/utils/epIcons.ts`；新前端不再引入 Font Awesome。旧前端侧栏仍按 fa class 渲染，故其菜单图标不再显示（旧前端只读保留，清理后此影响消失）
