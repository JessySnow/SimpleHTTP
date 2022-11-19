# Simple HTTP
> A simple but not that elegant HTTP server

# 文件结构

- client 模块
- server模块
  - [Bootstrap](server/src/main/java/org/simplehttp/server/Bootstrap.java) -- 启动文件示例
  - [Echo](server/src/main/java/org/simplehttp/server/handler/impl/EchoHandler.java) -- 框架使用示例
  - [ServerContext](server/src/main/java/org/simplehttp/server/core/context/ServerContext.java) -- 自定义上下文功能示例
- common 模块