package rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.response.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class FuseSearchPerfTest {

    private static String LOCAL_ENDPOINT = "http://yunxing.scv.apple.com:9345/WebObjects/MZSearch.woa/wa/fuseSearch?";

    private static String SF_HEADER = "x-apple-store-front";
    private static String SF_US = "143441-1,21";


    @Test
    public void test() throws Exception {
        int N = 1000;
        HttpClient client = HttpClientBuilder.create().build();
        String q = "attachSongs=true&caller=jingle-eval.baseline&filters=artist%3A%22Eminem%22&ignoreRawQuery=false&language=en_US&limit=1&q=beautiful&rawQuery=beautiful+by+Eminem&token=AawZFcTcXakSPaZdR0rejp9GIFCNjBhmCmlOC5czdu229ciznRchdUYxLSSiVdxqG7U7BIwSevqSG2QfbfmLKIM&version=1";
        for (int i=0; i<N; ++i) {
            System.out.println(i);
            long time = System.currentTimeMillis();
            String url = q + "&timestamp=" + time;
            String response = search(client, url);
            checkResponse(response, String.valueOf(time));
            Thread.sleep(10);
        }
    }

    public String search(HttpClient client, String q) throws Exception {
        URL url = new URL(LOCAL_ENDPOINT + q);
        HttpGet request = new HttpGet(url.toURI());
        request.setHeader(SF_HEADER, SF_US);
        HttpResponse response = client.execute(request);
        String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(responseStr);
        return responseStr;

    }

    private void checkResponse(String response, String timestamp) {
        Map<String, Object> body = new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {}.getType());
        try {
            List<Object> items = (List<Object>) body.get("data");
            if (items.size() < 1) {
                System.out.println("No result:" + timestamp);
            }
        } catch (Throwable e) {
            System.out.println("Error:" + timestamp);
        }
    }
}
