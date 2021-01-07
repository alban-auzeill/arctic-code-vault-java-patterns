package com.auzeill.github.tools;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.auzeill.github.tools.HttpUtils.body;

public class GitHubRestApi {

  private static final String RATE_LIMIT_URL = "https://api.github.com/rate_limit";

  int remainingCoreRequest = 0;
  int remainingSearchRequest = 0;

  public HttpResponse<String> core(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    if (remainingCoreRequest == 0) {
      remainingCoreRequest = waitForRemainingRequest("core");
    }
    remainingCoreRequest--;
    return HttpUtils.send(requestBuilder);
  }

  public HttpResponse<String> search(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    if (remainingSearchRequest == 0) {
      remainingSearchRequest = waitForRemainingRequest("search");
    }
    remainingSearchRequest--;
    return HttpUtils.send(requestBuilder);
  }

  private static int waitForRemainingRequest(String type) throws IOException, InterruptedException {
    int remainingRequest = 0;
    while (remainingRequest == 0) {
      String body = body(HttpUtils.githubAPIRequest(RATE_LIMIT_URL));
      JsonObject json = StringUtils.asJsonObject(body);
      JsonObject property = json.getAsJsonObject("resources").getAsJsonObject(type);
      remainingRequest = property.getAsJsonPrimitive("remaining").getAsInt();
      System.out.println("(remaining " + type + " requests: " + remainingRequest + ")");
      if (remainingRequest == 0) {
        long reset = property.getAsJsonPrimitive("reset").getAsLong() * 1000;
        long waitTime = Math.max(0, reset - System.currentTimeMillis() + 1000);
        System.out.println("(wait " + waitTime + " ms for remaining " + type + " requests)");
        Thread.sleep(waitTime);
      }
    }
    return remainingRequest;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println(body(HttpUtils.githubAPIRequest(RATE_LIMIT_URL)));
  }

}
