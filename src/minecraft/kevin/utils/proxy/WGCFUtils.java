package kevin.utils.proxy;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Base64;
//TODO: Skid nekobox
public class WGCFUtils {

    public static WGCFUtils INSTANCE = new WGCFUtils();



//    public static void main(String[] args) {
//
//        String confDirectory = "/home/user/WireGuard/";
//        String outputFile = "/home/user/WireGuard/converted_configs.txt";
//
//        convertConfToCustomFormat(confDirectory, outputFile);
//    }
//
//    public static void convertConfToCustomFormat(String confDirectory, String outputFilePath) {
//        File dir = new File(confDirectory);
//        if (!dir.exists() || !dir.isDirectory()) {
//            System.err.println("Invalid directory: " + confDirectory);
//            return;
//        }
//
//        List<String> customUrls = new ArrayList<>();
//
//        for (File file : Objects.requireNonNull(dir.listFiles())) {
//            if (file.isFile() && file.getName().endsWith(".conf")) {
//                try {
//                    Map<String, String> configMap = parseWireGuardConf(file);
//                    String vpnName = removeExtension(file.getName());
//
//                    // Build the inner "cs" JSON object
//                    Map<String, Object> csMap = new HashMap<>();
//                    csMap.put("tag", "wg-out");
//                    csMap.put("type", "wireguard");
//                    csMap.put("interface_name", "wg0");
//
//                    String endpoint = configMap.getOrDefault("Endpoint", ":");
//                    String[] endpointParts = endpoint.split(":");
//                    csMap.put("server", endpointParts[0]);
//                    csMap.put("server_port", Integer.parseInt(endpointParts.length > 1 ? endpointParts[1] : "0"));
//
//                    csMap.put("local_address", List.of(configMap.getOrDefault("Address", "") + "/32"));
//                    csMap.put("peer_public_key", configMap.getOrDefault("PublicKey", ""));
//                    csMap.put("private_key", configMap.getOrDefault("PrivateKey", ""));
//                    csMap.put("mtu", 1420);
//                    csMap.put("system_interface", false);
//
//                    // Convert to JSON string
//                    String csJson = mapToJson(csMap);
//
//                    // Build outer JSON
//                    Map<String, Object> rootMap = new HashMap<>();
//                    rootMap.put("_v", 0);
//                    rootMap.put("addr", "127.0.0.1");
//                    rootMap.put("cmd", List.of(""));
//                    rootMap.put("core", "internal");
//                    rootMap.put("cs", csJson);
//                    rootMap.put("mapping_port", 0);
//                    rootMap.put("name", vpnName);
//                    rootMap.put("port", 1080);
//                    rootMap.put("socks_port", 0);
//
//                    String jsonStr = mapToJson(rootMap);
//                    String base64Encoded = Base64.getEncoder().encodeToString(jsonStr.getBytes());
//
//                    String customUrl = "nekoray://custom#" + base64Encoded;
//                    customUrls.add(customUrl);
//
//                } catch (Exception e) {
//                    System.err.println("Error processing file: " + file.getName());
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        // Write all URLs to output file
//        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {
//            for (String url : customUrls) {
//                writer.write(url);
//                writer.newLine();
//            }
//            System.out.println("All configurations have been saved to: " + outputFilePath);
//        } catch (IOException e) {
//            System.err.println("Failed to write output file.");
//            e.printStackTrace();
//        }
//    }
//
//    private static Map<String, String> parseWireGuardConf(File file) throws IOException {
//        Map<String, String> result = new HashMap<>();
//        List<String> lines = Files.readAllLines(file.toPath());
//        for (String line : lines) {
//            if (line.contains("=")) {
//                String[] parts = line.split("=", 2);
//                result.put(parts[0].trim(), parts[1].trim());
//            }
//        }
//        return result;
//    }
//
//    private static String removeExtension(String filename) {
//        int dotIndex = filename.lastIndexOf('.');
//        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
//    }
//
//    private static String mapToJson(Map<?, ?> map) {
//        StringBuilder sb = new StringBuilder("{");
//        for (Map.Entry<?, ?> entry : map.entrySet()) {
//            sb.append("\"").append(entry.getKey()).append("\":");
//            Object value = entry.getValue();
//            if (value instanceof String) {
//                sb.append("\"").append(value).append("\"");
//            } else if (value instanceof List<?>) {
//                sb.append("[\"");
//                sb.append(String.join("\",\"", (List<String>) value));
//                sb.append("\"]");
//            } else if (value instanceof Boolean || value instanceof Number) {
//                sb.append(value);
//            } else {
//                sb.append("\"").append(value.toString()).append("\"");
//            }
//            sb.append(",");
//        }
//        if (sb.length() > 1) sb.setLength(sb.length() - 1); // Remove trailing comma
//        sb.append("}");
//        return sb.toString();
//    }
}