package gateways.practiTest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gateways.GatewayUtils;
import gateways.GatewayProps;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.File;

public class PractiTestJSONUtils {

    public static byte[] getEncoding(File f){
        String[] kv = GatewayUtils.readFile(f, GatewayProps.practiTest);
        return Base64.encodeBase64((kv[0] + ":" + kv[1]).getBytes());
    }

    public static HttpPost createInstance(byte[] encoding, String projectId, String testSetId, String testId) throws Exception {
        String json_str = "{\"data\": { \"type\": \"instances\", \"attributes\": {\"test-id\": " +
                testId + ", \"set-id\": " + testSetId + "}  } }";
        return postRequest(uriInstance(projectId), json_str, encoding);
    }

    public static HttpPost runTest(byte[] encoding, String projectId, String instanceID, String testSetId, String testId, String runDuration, String message, Integer exitCode) throws Exception {
        String json_str = "{\"data\": { \"type\": \"instances\", \"attributes\": {\"set-id\": " + testSetId + ", " +
                "\"test-id\": " + testId + ", \"run-duration\": \"" + runDuration + "\", \"instance-id\": " + instanceID + ", \"exit-code\": " + exitCode + ", " +
                "\"automated-execution-output\": \"" +
                automationOutput(message).replace("\n", " ").replace("\"", "\\\"").substring(0, 255)
                + "\" }, \"steps\": {\"data\": [";

        Integer outputSize = GatewayUtils.stepsOutput.size();

        for (int i = 0; i < outputSize; i++){
            if (i != 0){
                json_str += ", ";
            }

            json_str += "{\"name\": \"" + GatewayUtils.stepsOutput.get(i) + "\", \"status\": \"" + stepStatus(message, i, outputSize) + "\"}";
        }

        json_str += "] }}} ";
        return postRequest(uriRun(projectId), json_str, encoding);
    }

    private static String automationOutput(String message){
        if (message.equals(null)){
            return "";
        } else {
            return message;
        }
    }

    private static String stepStatus(String message, Integer count, Integer outputSize){
        if (count == outputSize - 1 && !message.equals(null)){
            return "FAILED";
        } else {
            return "PASSED";
        }
    }

    public static HttpPost postRequest(String uri, String json_str, byte[] encoding) throws Exception{
        HttpPost request = new HttpPost(uri);
        request.setEntity(new StringEntity(json_str));
        request.setHeader("Authorization", "Basic " + new String(encoding));
        request.addHeader("content-type", "application/json");

        System.out.println("executing request " + request.getURI());
        return request;
    }

    public static String getInstanceId(String responseBody){
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(responseBody);
        String newInstanceId = "";

        if(jsonTree.isJsonObject()){
            JsonObject jsonObject = jsonTree.getAsJsonObject();
            JsonElement data = jsonObject.get("data");

            if(data.isJsonObject()){
                JsonObject dataObject = data.getAsJsonObject();
                newInstanceId = dataObject.get("id").toString();
            }
        }

        return newInstanceId;
    }

    public static String uriRun(String projectId){
        return "https://api.practitest.com/api/v2/projects/" + projectId + "/runs.json";
    }

    public static String uriInstance(String projectId){
        return "https://api.practitest.com/api/v2/projects/" + projectId + "/instances.json";
    }
}
