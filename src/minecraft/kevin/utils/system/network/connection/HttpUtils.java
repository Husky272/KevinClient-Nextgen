package kevin.utils.system.network.connection;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * LiquidBounce moment
 */
public final class HttpUtils {
    @NotNull
    public static final HttpUtils INSTANCE = new HttpUtils();
    @NotNull
    private static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";

    static {
        HttpURLConnection.setFollowRedirects(true);
    }

    private HttpUtils() {
    }

    // $FF: synthetic method
    public static Pair request$default(HttpUtils var0, String var1, String var2, String var3, Pair[] var4, int var5, Object var6) throws IOException {
        if ((var5 & 4) != 0) {
            var3 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
        }

        if ((var5 & 8) != 0) {
            var4 = new Pair[0];
        }

        return var0.request(var1, var2, var3, var4);
    }


    // $FF: synthetic method
    public static Pair requestStream$default(HttpUtils var0, String var1, String var2, String var3, Pair[] var4, int var5, Object var6) throws IOException {
        if ((var5 & 4) != 0) {
            var3 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
        }

        if ((var5 & 8) != 0) {
            var4 = new Pair[0];
        }

        return var0.requestStream(var1, var2, var3, var4);
    }

    private HttpURLConnection make(String url, String method, String agent, Pair[] headers) throws IOException {
        URLConnection var6 = (new URL(url)).openConnection();
        Intrinsics.checkNotNull(var6, "null cannot be cast to non-null type java.net.HttpURLConnection");
        HttpURLConnection httpConnection = (HttpURLConnection) var6;
        httpConnection.setRequestMethod(method);
        httpConnection.setConnectTimeout(2000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestProperty("User-Agent", agent);
        int var11 = 0;

        for (int var7 = headers.length; var11 < var7; ++var11) {
            Pair var8 = headers[var11];
            String key = (String) var8.component1();
            String value = (String) var8.component2();
            httpConnection.setRequestProperty(key, value);
        }

        httpConnection.setInstanceFollowRedirects(true);
        httpConnection.setDoOutput(true);
        return httpConnection;
    }

    @NotNull
    public Pair request(@NotNull String url, @NotNull String method, @NotNull String agent, @NotNull Pair[] headers) throws IOException {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(agent, "agent");
        Intrinsics.checkNotNullParameter(headers, "headers");
        Pair var5 = this.requestStream(url, method, agent, headers);
        int var7 = 0;
        InputStream stream = (InputStream) var5.component1();
        int code = ((Number) var5.component2()).intValue();
        Charset var11 = Charsets.UTF_8;
        return TuplesKt.to(TextStreamsKt.readText(new InputStreamReader(stream, var11)), code);
    }

    @NotNull
    public String post(@NotNull String url, @NotNull String agent, @NotNull Pair[] headers, @NotNull Function0 entity) throws IOException {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(agent, "agent");
        Intrinsics.checkNotNullParameter(headers, "headers");
        Intrinsics.checkNotNullParameter(entity, "entity");
        CloseableHttpClient httpClient = HttpClientBuilder.create().setUserAgent(agent).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity((HttpEntity) entity.invoke());
        int response = 0;

        for (int var8 = headers.length; response < var8; ++response) {
            Pair var9 = headers[response];
            String key = (String) var9.component1();
            String value = (String) var9.component2();
            httpPost.setHeader(key, value);
        }

        CloseableHttpResponse r = httpClient.execute(httpPost);
        InputStream var13 = r.getEntity().getContent();
        Intrinsics.checkNotNullExpressionValue(var13, "getContent(...)");
        Charset var14 = Charsets.UTF_8;
        return TextStreamsKt.readText(new InputStreamReader(var13, var14));
    }

    @NotNull
    public Pair requestStream(@NotNull String url, @NotNull String method, @NotNull String agent, @NotNull Pair[] headers) throws IOException {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(agent, "agent");
        Intrinsics.checkNotNullParameter(headers, "headers");
        HttpURLConnection conn = this.make(url, method, agent, headers);
        return TuplesKt.to(conn.getResponseCode() < 400 ? conn.getInputStream() : conn.getErrorStream(), conn.getResponseCode());
    }

    @NotNull
    public Pair get(@NotNull String url) throws IOException {
        Intrinsics.checkNotNullParameter(url, "url");
        return request$default(this, url, "GET", null, null, 12, null);
    }

    public int responseCode(@NotNull String url, @NotNull String method, @NotNull String agent) throws IOException {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(agent, "agent");
        return make( url, method, agent, null).getResponseCode();
    }

    public void download(@NotNull String url, @NotNull File file) throws IOException {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(file, "file");
        Pair var3 = requestStream$default(this, url, "GET", null, null, 12, null);
        InputStream stream = (InputStream) var3.component1();
        int code = ((Number) var3.component2()).intValue();
        if (code != 200) {
            throw new IllegalStateException(("Response code is " + code));
        } else {
            FileUtils.copyInputStreamToFile(stream, file);
        }
    }
}
