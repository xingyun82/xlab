package http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientTest {

    @Test
    public void getContent() {
        try {
            URL url = new URL("https://silverbullet-ats.itunes.apple.com/content/podcasts-static-config-internal/api/v1/news/topic/all.json");
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url.toURI());

            HttpResponse response = client.execute(request);

            if (response == null ||
                    response.getStatusLine() == null ||
                    response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                return;
            }

            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
