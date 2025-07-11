# 相关依赖
## Ollama  安装
官网下载安装：
https://ollama.com/

下载模型

`ollama pull qwen3:1.7b`

以服务方式运行ollama

`ollama serve`

### 常用命令
- serve：启动 ollama 服务。
- create：根据一个 Modelfile 创建一个模型。
- show：显示某个模型的详细信息。
- run：运行一个模型。
- stop：停止一个正在运行的模型。
- pull：从一个模型仓库（registry）拉取一个模型。
- push：将一个模型推送到一个模型仓库。
- list：列出所有模型。
- ps：列出所有正在运行的模型。
- cp：复制一个模型。
- rm：删除一个模型。
- help：获取关于任何命令的帮助信息。

## MCP
官网：https://modelcontextprotocol.io/introduction

中文站：https://mcp-docs.cn/introduction 


## Langchain4j
官网：https://docs.langchain4j.dev/intro/

中文站： https://docs.langchain4j.info/intro

官网示例：https://github.com/langchain4j/langchain4j-examples


# 运行
1. 启动ollama服务
2. build [mcp-person-server](../mcp-person-server)项目为jar包
3. 修改[ai-agent](../ai-agent)项目中[LLM.java](src/main/java/com/nastyair/ai/LLM.java) 的**jar**包路径(JAR_PATH)为你的本地路径
4. 修改[ai-agent](../ai-agent)项目中[LLM.java](src/main/java/com/nastyair/ai/LLM.java) 的**Java**路径(JAVA_PATH)为你的本地路径
5. 运行[ai-agent](../ai-agent)项目中[LLM.java](src/main/java/com/nastyair/ai/LLM.java)