package com.auzeill.github.tools.utlis;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class HttpUtils {

  public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.ALWAYS)
    .build();

  private HttpUtils() {
    // utility class
  }

  public static HttpRequest.Builder newRequest(String url) {
    return HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
      .header("Accept-Language", "en-US,en;q=0.9,fr-FR;q=0.8,fr;q=0.7");
  }

  public static HttpRequest.Builder newHtmlRequest(String url) {
    return newRequest(url)
      .header("Accept", "text/html");
  }

  public static HttpRequest.Builder newTextRequest(String url) {
    return newRequest(url)
      .header("Accept", "text/plain");
  }

  public static HttpRequest.Builder githubAPIRequest(String url) {
    return githubRequest(url)
      .header("Accept", "application/vnd.github.v3+json");
  }

  public static HttpRequest.Builder githubRequest(String url) {
    String token = System.getenv("GITHUB_READ_TOKEN");
    if (token == null) {
      throw new IllegalStateException("Missing environment variable GITHUB_READ_TOKEN");
    }
    return newRequest(url)
      .header("Authorization", "token " + token);
  }

  public static HttpRequest.Builder githubHtmlRequest(String url) {
    return githubRequest(url)
      .header("Accept", "text/html");
  }

  public static HttpResponse<String> send(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    return HTTP_CLIENT.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
  }

  public static String body(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    HttpRequest request = requestBuilder.build();
    return body(HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString()));
  }

  public static String body(HttpResponse<String> response) throws IOException {
    if (response.statusCode() != 200) {
      throw new IOException(response.statusCode() + " for " + response.uri());
    }
    return response.body();
  }

}
