package com.grokchat.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.grokchat.GrokChatPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class GrokApiClient {

    private final GrokChatPlugin plugin;
    private final Gson gson;
    private final ExecutorService executor;

    public GrokApiClient(GrokChatPlugin plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
        this.executor = Executors.newFixedThreadPool(4);
    }

    public CompletableFuture<String> sendMessage(String message, String playerName) {
        return sendMessage(message, playerName, null, null);
    }

    public CompletableFuture<String> sendMessage(String message, String playerName, String chatContext) {
        return sendMessage(message, playerName, chatContext, null);
    }

    public CompletableFuture<String> sendMessage(String message, String playerName, String chatContext, String playerData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Build request
                JsonObject request = buildRequest(message, playerName, chatContext, playerData);
                
                if (plugin.getConfigManager().isDebug()) {
                    plugin.getLogger().info("Request: " + gson.toJson(request));
                }

                // Send HTTP request
                String response = sendHttpRequest(request);
                
                if (plugin.getConfigManager().isDebug()) {
                    plugin.getLogger().info("Response: " + response);
                }

                // Parse response
                return parseResponse(response);
                
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error calling Grok API", e);
                return "Sorry, I encountered an error processing your request: " + e.getMessage();
            }
        }, executor);
    }

    private JsonObject buildRequest(String message, String playerName, String chatContext, String playerData) {
        JsonObject request = new JsonObject();
        
        // Set model
        request.addProperty("model", plugin.getConfigManager().getModel());
        
        // Build messages array
        JsonArray messages = new JsonArray();
        
        // System message with configurable prompt
        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        String systemPrompt = plugin.getConfigManager().getSystemPrompt()
            .replace("{player}", playerName);
        
        // Add chat context to system prompt if provided
        if (chatContext != null && !chatContext.isEmpty()) {
            systemPrompt += "\n\n" + chatContext;
        }
        
        // Add player data context if provided
        if (playerData != null && !playerData.isEmpty()) {
            systemPrompt += "\n\n" + playerData;
        }
        
        systemMsg.addProperty("content", systemPrompt);
        messages.add(systemMsg);
        
        // User message
        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", message);
        messages.add(userMsg);
        
        request.add("messages", messages);
        
        // Advanced settings
        request.addProperty("temperature", plugin.getConfigManager().getTemperature());
        request.addProperty("max_tokens", plugin.getConfigManager().getMaxTokens());
        request.addProperty("stream", false);
        
        return request;
    }

    private String sendHttpRequest(JsonObject request) throws Exception {
        URL url = URI.create(plugin.getConfigManager().getApiEndpoint()).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            // Set request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + plugin.getConfigManager().getApiKey());
            conn.setDoOutput(true);
            conn.setConnectTimeout(plugin.getConfigManager().getTimeout() * 1000);
            conn.setReadTimeout(plugin.getConfigManager().getTimeout() * 1000);
            
            // Write request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = gson.toJson(request).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Check response code
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("API returned error code: " + responseCode);
            }
            
            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            
            return response.toString();
            
        } finally {
            conn.disconnect();
        }
    }

    private String parseResponse(String jsonResponse) throws Exception {
        JsonObject response = JsonParser.parseString(jsonResponse).getAsJsonObject();
        
        // Extract message from response
        JsonArray choices = response.getAsJsonArray("choices");
        if (choices == null || choices.size() == 0) {
            throw new Exception("No response from Grok");
        }
        
        JsonObject firstChoice = choices.get(0).getAsJsonObject();
        JsonObject message = firstChoice.getAsJsonObject("message");
        String content = message.get("content").getAsString();
        
        // Log usage if debug is enabled
        if (plugin.getConfigManager().isDebug() && response.has("usage")) {
            JsonObject usage = response.getAsJsonObject("usage");
            plugin.getLogger().info("Tokens used - Prompt: " + 
                usage.get("prompt_tokens") + ", Completion: " + 
                usage.get("completion_tokens") + ", Total: " + 
                usage.get("total_tokens"));
        }
        
        return content;
    }

    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}

