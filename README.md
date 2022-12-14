# Simple HTTP
> A simple but not that elegant HTTP server

## 文件结构

- client 模块
- server模块
  - [Bootstrap](server/src/main/java/org/simplehttp/server/Bootstrap.java) -- 启动文件示例
  - [EchoHandler](server/src/main/java/org/simplehttp/server/handler/impl/EchoHandler.java) -- 框架 GET 请求处理示例
  - [PathParamHandler](server/src/main/java/org/simplehttp/server/handler/impl/PathParamHandler.java) -- 框架路径参数请求处理示例
  - [ServerContext](server/src/main/java/org/simplehttp/server/core/context/ServerContext.java) -- 自定义上下文功能示例
- common 模块

## ToDoList
- CGI 风格的 URL 解析 ☑️
- GET 请求、路径参数处理 ☑️
- 日志框架集成 ☑️
- POST 请求、Multi Part 请求处理
- 重构异常捕获代码、重构日志输出代码
- 扩展 BaseContext，支持内置的 Session 管理
- 代码重构
  - 请求处理逻辑移动到 Context 中而不是在工作线程处理，工作线程只负责进行 IO 相关的操作
  - 异常框架和日志框架的完善，日志和异常没有得到集中处理，分散在各处