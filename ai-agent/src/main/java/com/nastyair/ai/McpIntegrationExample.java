//package com.nastyair.ai;
//
//
//import dev.langchain4j.model.ollama.OllamaChatModel;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//
//public class McpIntegrationExample {
//
//    private static final Logger logger = LoggerFactory.getLogger(McpIntegrationExample.class);
//    // MCP 服务配置
//    private static final String MCP_SSE_ENDPOINT = "http://localhost:3001/sse";
//    private static final String MCP_API_KEY = "your-api-key"; // 替换为实际API密钥
//
//    // Ollama 配置
//    private static final String OLLAMA_BASE_URL = "http://localhost:11434";
//    private static final String OLLAMA_MODEL_NAME = "qwen3:1.7b";
//
//    public static void main(String[] args) {
//        // 1. 初始化 MCP 传输层
////        McpTransport transport = new HttpClientSseClientTransport
////                .baseUrl(MCP_SSE_ENDPOINT)
////                .apiKey(MCP_API_KEY)
////                .sseUrl("http://localhost:3001/sse")
////                .logRequests(true) // 如果你想在日志中查看流量
////                .logResponses(true)
////                .build();
//        WebClient.Builder webClientBuilder = WebClient.builder()
//                .baseUrl(MCP_SSE_ENDPOINT);
//        McpClientTransport transport = new WebFluxSseClientTransport(webClientBuilder);
//        // Create an async client with custom configuration
//
//        McpSchema.Role role = McpSchema.Role.ASSISTANT;
//        McpAsyncClient client = McpClient.async(transport)
//                .requestTimeout(Duration.ofSeconds(10))
//                .capabilities(McpSchema.ClientCapabilities.builder()
//                        .roots(true)
//                        .sampling()
//                        .build())
//                .sampling(request -> Mono.just(new McpSchema.CreateMessageResult(
//                        role,
//                        new McpSchema.TextContent("hello"),
//                        OLLAMA_MODEL_NAME,
//                        McpSchema.CreateMessageResult.StopReason.END_TURN
//                )))
//                .toolsChangeConsumer(tools -> Mono.fromRunnable(() -> {
//                    logger.info("Tools updated: {}", tools);
//                }))
//                .resourcesChangeConsumer(resources -> Mono.fromRunnable(() -> {
//                    logger.info("Resources updated: {}", resources);
//                }))
//                .promptsChangeConsumer(prompts -> Mono.fromRunnable(() -> {
//                    logger.info("Prompts updated: {}", prompts);
//                }))
//                .build();
//// Initialize connection and use features
//        client.initialize()
//                .flatMap(initResult -> client.listTools())
//                .flatMap(tools -> {
//                    return client.callTool(new McpSchema.CallToolRequest(
//                            "getPerson",
//                            Map.of("name", "Bob")
//                    ));
//                })
//                .flatMap(result -> {
//                    return client.listResources()
//                            .flatMap(resources ->
//                                    client.readResource(new McpSchema.ReadResourceRequest("resource://uri"))
//                            );
//                })
//                .flatMap(resource -> {
//                    return client.listPrompts()
//                            .flatMap(prompts ->
//                                    client.getPrompt(new McpSchema.GetPromptRequest(
//                                            "greeting",
//                                            Map.of("name", "Spring")
//                                    ))
//                            );
//                })
////                .flatMap(prompt -> {
////                    return client.addRoot(new Root("file:///path", "description"))
////                            .then(client.removeRoot("file:///path"));
////                })
//                .doFinally(signalType -> {
//                    client.closeGracefully().subscribe();
//                })
//                .subscribe();
//        // 2. 初始化 Ollama 模型
//        OllamaChatModel ollama = OllamaChatModel.builder()
//                .baseUrl(OLLAMA_BASE_URL)
//                .modelName(OLLAMA_MODEL_NAME)
//                .temperature(0.7)
//                .build();
//
//        try {
//            // 3. 示例1: 从 MCP 获取数据
//            String personName = "Bob";
//
//            McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("",Map.of(
//                    "name", personName
//            ));
//// Execute a tool asynchronously
////            CompletableFuture<PersonInfo> personFuture=client.callTool(request).subscribe();
//            CompletableFuture<PersonInfo> personFuture = client.callTool(request)
//                    .map(response -> (PersonInfo) response.content()) // 假设返回内容是 PersonInfo
//                    .toFuture();
//
////            CompletableFuture<PersonInfo> personFuture = getPersonInfo(transport, personName);
//
//            personFuture.thenAccept(person -> {
//                System.out.println("获取到人员信息: " + person);
//
//                // 4. 示例2: 使用 Ollama 处理 MCP 数据
//                String prompt = String.format("请为%s生成一段介绍，职位：%s，部门：%s",
//                        person.getName(), person.getPosition(), person.getDepartment());
//
//                String generatedText = ollama.chat(prompt);
//                System.out.println("生成的介绍:\n" + generatedText);
//
//                // 5. 示例3: 将结果存回 MCP
////                saveAnalysisResult(transport, person.getId(), generatedText)
////                        .thenAccept(success -> {
////                            if (success) {
////                                System.out.println("分析结果保存成功");
////                            } else {
////                                System.out.println("分析结果保存失败");
////                            }
////                        });
//            }).join();
//
//        } finally {
//            // 6. 关闭传输层
//            transport.close();
//        }
//    }
//
//    // 从 MCP 获取人员信息
////    private static CompletableFuture<PersonInfo> getPersonInfo(McpTransport transport, String name) {
////        // List available tools asynchronously
////        client.listTools()
////                .doOnNext(tools -> tools.forEach(tool ->
////                        System.out.println(tool.getName())))
////                .subscribe();
////
////// Execute a tool asynchronously
////        client.callTool("calculator", Map.of(
////                        "operation", "add",
////                        "a", 1,
////                        "b", 2
////                ))
////                .subscribe();
////
////        // 列出可用工具及其名称
////        var tools = client.listTools();
////        tools.forEach(tool -> System.out.println(tool.getName()));
////
//
//    /// / 使用参数执行工具
////        var result = client.callTool("calculator", Map.of(
////                "operation", "add",
////                "a", 1,
////                "b", 2
////        ));
////
////        McpRequest request = McpRequest.newBuilder()
////                .method("GET")
////                .path("/api/v1/persons")
////                .queryParam("name", name)
////                .header("X-Request-ID", generateRequestId())
////                .build();
////
////        return transport.send(request)
////                .thenApply(response -> {
////                    if (response.getStatus() == 200) {
////                        return response.getBodyAs(PersonInfo.class);
////                    } else {
////                        throw new RuntimeException("获取人员信息失败: " + response.getBodyAsString());
////                    }
////                });
////    }
////
////    // 保存分析结果到 MCP
////    private static CompletableFuture<Boolean> saveAnalysisResult(
////            McpTransport transport, String personId, String analysis) {
////
////        AnalysisResult result = new AnalysisResult(personId, analysis);
////
////        McpRequest request = McpRequest.newBuilder()
////                .method("POST")
////                .path("/api/v1/analysis")
////                .body(result)
////                .build();
////
////        return transport.send(request)
////                .thenApply(response -> response.getStatus() == 200);
////    }
//    private static String generateRequestId() {
//        return java.util.UUID.randomUUID().toString();
//    }
//
//    // DTO 类
//    static class PersonInfo {
//        private String id;
//        private String name;
//        private String position;
//        private String department;
//
//        // getters and setters
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getPosition() {
//            return position;
//        }
//
//        public void setPosition(String position) {
//            this.position = position;
//        }
//
//        public String getDepartment() {
//            return department;
//        }
//
//        public void setDepartment(String department) {
//            this.department = department;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("Person{name='%s', position='%s', department='%s'}",
//                    name, position, department);
//        }
//    }
//
//    static class AnalysisResult {
//        private String personId;
//        private String analysis;
//
//        public AnalysisResult(String personId, String analysis) {
//            this.personId = personId;
//            this.analysis = analysis;
//        }
//
//        // getters
//        public String getPersonId() {
//            return personId;
//        }
//
//        public String getAnalysis() {
//            return analysis;
//        }
//    }
//}