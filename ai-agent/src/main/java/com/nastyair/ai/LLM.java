package com.nastyair.ai;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

import java.util.List;

public class LLM {
    // Ollama 配置
    private static final String OLLAMA_BASE_URL = "http://localhost:11434";
    private static final String OLLAMA_MODEL_NAME = "qwen3:1.7b";
    private static final String JAR_PATH = "/Volumes/Seagate1T/javaProject/ai/mcp-person-server/target/mcp-person-server-0.0.1-SNAPSHOT.jar";
    private static final String JAVA_PATH = "/Volumes/Seagate1T/sdk/jdk-24.0.1.jdk/Contents/Home/bin/java";

    public static void main(String[] args) throws Exception {
        ChatModel model = OllamaChatModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(OLLAMA_MODEL_NAME)
                .temperature(0.7)
//                .logRequests(true)
//                .logResponses(true)
                .build();
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of(
                        JAVA_PATH,
                        "-Dspring.ai.mcp.server.stdio=true",
                        "-Dlogging.pattern.console=",
                        "-jar",
                        JAR_PATH))
                .logEvents(true)
                .build();
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
//                .clientName("person-mcp")
//                .protocolVersion("2024-11-05") // 明确指定协议版本
//                .clientInfo(Map.of(
//                        "name", "ai-agent",
//                        "version", "0.0.1"
//                ))
                .build();

        // 添加工具列表检查
        try {
            List<ToolSpecification> tools = mcpClient.listTools();
            if (tools==null|| tools.isEmpty()) {
                System.err.println("警告: MCP服务端未返回任何工具");
            }
        } catch (Exception e) {
            System.err.println("获取工具列表失败: " + e.getMessage());
            e.printStackTrace();
        }

        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        Bot bot = AiServices.builder(Bot.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();

        try {
            String response = bot.chat("给Bob发欢迎加入公司的邮件,可以调用tool获取Bob的信息，然后再用tool发送邮件");
            System.out.println("RESPONSE: " + response);
        } finally {
            mcpClient.close();
        }
    }
}
