package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.Response;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FuseSearchTest {

    private static String LOCAL_ENDPOINT = "http://yunxing.scv.apple.com:9345/WebObjects/MZSearch.woa/wa/fuseSearch?";
    private static String PROD_ENDPOINT = "https://siri-search.itunes.apple.com/WebObjects/MZSearch.woa/1/wa/fuseSearch?";
    private static String PROD_TEST_ENDPOINT = "http://itunes.isoproxy.apple.com/WebObjects/MZSearch.woa/211000/wa/fuseSearch?";
    private static String SF_HEADER = "x-apple-store-front";
    private static String SF_US = "143441-1,21";


    public Response search(String q) {
        String endpoint = PROD_TEST_ENDPOINT;
        return given().header(SF_HEADER, SF_US).get(endpoint + q);
    }

    @Test
    public void testSearchBeats1Show() {
        List<String> queryList = Arrays.asList(
            "q=rocket hour",
            "q=rocket hour on beats 1",
            "q=beats 1 with elton john",
            "q=elton john’s rocket hour",
            "q=rocket hour by elton john",
            "q=the latest episode of rocket hour",
            "q=rocket hour&type=radioShow",
            "q=elton john&type=radioShow"
        );

        queryList.stream().forEach(q -> checkBeats1Show(search(q)));
    }

    private void checkBeats1Show(Response response) {
        response.then()
                .body(
                    "status.code", equalTo(200),
                    "data[0].id", equalTo("993269779"),
                    "data[0].title", equalTo("Rocket Hour"),
                    "data[0].contentType", equalTo("radioShow"),
                    "data[0].showHost", equalTo("Elton John"),
                    "data[0].items", hasSize(1),
                    "data[0].items[0].showAdamId", equalTo("993269779")
                );
    }

    @Test
    public void testSearchBeats1ShowEpisode() {
        List<String> queryList = Arrays.asList(
            "q=the latest episode of rocket hour with kesha",
            "q=rocket hour&filters=artist:kesha",
            "q=elton john’s rocket hour&filters=artist:kesha"
        );

        queryList.stream().forEach(q -> checkBeats1ShowEpisode(search(q)));
    }

    private void checkBeats1ShowEpisode(Response response) {
        response.then()
                .body(
                    "status.code", equalTo(200),
                    "data[0].id", equalTo("ra.1323016683"),
                        "data[0].title", equalTo("FaceTime with Kesha"),
                        "data[0].showAdamId", equalTo("993269779"),
                        "data[0].showName", equalTo("Rocket Hour")
                );
    }


    @Test
    public void testSearchByIds() {
        List<String> queryList = Arrays.asList(
                "ids=20044,83445997,80812428"
        );
        queryList.stream().forEach(q -> checkSearchByIds(search(q)));
    }

    private void checkSearchByIds(Response response) {
        response.then()
                .body(
                    "status.code", equalTo(200),
                        "data", hasSize(3),
                        "data[0].id", equalTo("20044"),
                        "data[1].id", equalTo("83445997"),
                        "data[2].id", equalTo("80812428")
                );
    }

    @Test
    public void testArtistSong() {
        List<String> queryList = Arrays.asList(
                "filters=artistId:398128&type=song",
                "q=jutin timberlake&type=song",
                "filters=artist:\"justin timberlake\"&type=song"
        );
        queryList.stream().forEach(q -> checkArtistSong(search(q)));
    }

    private void checkArtistSong(Response response) {
        response.then()
                .body(
                    "status.code", equalTo(200),
                    "data[0].artistId", equalTo("398128"),
                    "data[0].contentType", equalTo("Song")
                );
    }

    @Test
    public void testArtistAlbum() {
        List<String> queryList = Arrays.asList(
                "filters=artistId:398128&type=album",
                "filters=artist:\"justin timberlake\"&type=album"
        );
        queryList.stream().forEach(q -> checkArtistAlbum(search(q)));
    }

    private void checkArtistAlbum(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].artistId", equalTo("398128"),
                        "data[0].contentType", equalTo("Album")
                );
    }

    @Test
    public void testSortByHottest() {
        List<String> queryList = Arrays.asList(
            "q=madonna&sort=hottest"
        );
        queryList.stream().forEach(q -> checkSortByHottest(search(q)));
    }

    private void checkSortByHottest(Response response) {
        response.then()
                .body(
                    "status.code", equalTo(200),
                    "data[0].artist", equalTo("Madonna"),
                        "data[0].artistId", equalTo("20044"),
                        "data[0].id", equalTo("80815215")
                );
    }

    @Test
    public void testSortByNewest() {
        List<String> queryList = Arrays.asList(
                "q=madonna&sort=newest"
        );
        queryList.stream().forEach(q -> checkSortByNewest(search(q)));
    }

    private void checkSortByNewest(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].artist", equalTo("Madonna"),
                        "data[0].artistId", equalTo("20044"),
                        "data[0].id", equalTo("1270186821")
                );
    }


    @Test
    public void testSearchSongName() {
        List<String> queryList = Arrays.asList(
                "filters=song:\"black hole sun\"",
                "q=black hole sun&type=song"
        );
        queryList.stream().forEach(q -> checkSearchSongName(search(q)));
    }

    private void checkSearchSongName(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].artistId", equalTo("133036"),
                        "data[0].id", equalTo("390525812")
                );
    }


    @Test
    public void testSearchArtistName() {
        List<String> queryList = Arrays.asList(
            "filters=artist:\"green day\"",
            "q=green day&type=artist"
        );
        queryList.stream().forEach(q -> checkSearchArtistName(search(q)));
    }

    private void checkSearchArtistName(Response response) {
        response.then()
                .body(
                    "status.code", equalTo(200),
                    "data[0].id", equalTo("954266")
                );
    }

    @Test
    public void testSearchAlbumName() {
        List<String> queryList = Arrays.asList(
            "filters=album:\"in the lonely hour\"",
            "q=in the lonely hour&type=album"
        );
        queryList.stream().forEach(q -> checkSearchArtistAlbum(search(q)));
    }

    private void checkSearchArtistAlbum(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", equalTo("836166467"),
                        "data[0].contentType", equalTo("Album"),
                        "data[0].artistId", equalTo("156488786")
                );
    }

    @Test
    public void testSearchSongByAlbumName() {
        List<String> queryList = Arrays.asList(
                "filters=album:\"in the lonely hour\"&type=song"
        );
        queryList.stream().forEach(q -> checkSearchSongByAlbumName(search(q)));
    }

    private void checkSearchSongByAlbumName(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", equalTo("836166481"),
                        "data[0].albumId", equalTo("836166467"),
                        "data[0].contentType", equalTo("Song")
                );
    }

    @Test
    public void testSearchStation() {
        List<String> queryList = Arrays.asList(
                "q=npr&type=station"
        );
        queryList.stream().forEach(q -> checkSearchStation(search(q)));
    }

    private void checkSearchStation(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", equalTo("ra.840950253"),
                        "data[0].contentType", equalTo("Radio Station")
                );
    }

    @Test
    public void testSearchLiveSong() {
        List<String> queryList = Arrays.asList(
                "type=song&filters=live:true&q=Like a Virgin"
        );
        queryList.stream().forEach(q -> checkSearchLiveSong(search(q)));
    }

    private void checkSearchLiveSong(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", equalTo("270901336"),
                        "data[0].contentType", equalTo("Song")
                );
    }

    @Test
    public void testSongCharts() {
        List<String> queryList = Arrays.asList(
                "topItemsGenre=Rock&type=song",
                "topItemsGenre=Rock&type=song&sort=shuffle"
        );
        queryList.stream().forEach(q -> checkGeneralForSong(search(q)));
    }

    private void checkGeneralForSong(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", notNullValue(),
                        "data[0].contentType", equalTo("Song")
                );
    }

    @Test
    public void testAlapplibumCharts() {
        List<String> queryList = Arrays.asList(
                "topItemsGenre=Rock&type=album",
                "topItemsGenre=Rock&type=album&sort=shuffle"
                );
        queryList.stream().forEach(q -> checkGeneralForAlbum(search(q)));
    }

    private void checkGeneralForAlbum(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", notNullValue(),
                        "data[0].contentType", equalTo("Album")
                );
    }


    @Test
    public void testDescription() {
        List<String> queryList = Arrays.asList(
                "ids=571445253&fields=description"
        );
        queryList.stream().forEach(q -> checkDescription(search(q)));
    }

    private void checkDescription(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", notNullValue(),
                        "data[0].description", notNullValue()
                );
    }

    @Test
    public void testGetPodcast() {
        List<String> queryList = Arrays.asList(
                "q=wtf&type=podcast"
        );
        queryList.stream().forEach(q -> checkPodcast(search(q)));
    }


    private void checkPodcast(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", equalTo("329875043"),
                        "data[0].contentType", equalTo("Podcast")
                );
    }

    @Test
    public void testGetPodcastEpisode() {
        List<String> queryList = Arrays.asList(
                "filters=podcast:\"this american life\"&type=podcastEpisode",
                "filters=podcastId:201671138&type=podcastEpisode"
//                "q=This american life&type=podcastEpisode"
        );
        queryList.stream().forEach(q -> checkPodcastEpisode(search(q)));
    }

    private void checkPodcastEpisode(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].podcastId", equalTo("201671138"),
                        "data[0].contentType", equalTo("PodcastEpisode")
                );
    }

    @Test
    public void testPlaybackInfo() {
        List<String> queryList = Arrays.asList(
                "q=hard days night by the beatles&reqId=b6edb2f01797d0f106b4621a6a7eb808&caller=siri-test&platform=AppleTV&osVersion=11.3&language=en_US&storeId=143441&attachSongs=true&limit=5&token=AawZFcTcXakSPaZdR0rejp9GIFCNjBhmCmlOC5czdu229ciznRchdUYxLSSiVdxqG7U7BIwSevqSG2QfbfmLKIM"
        );
        queryList.stream().forEach(q -> checkPlaybackInfo(search(q)));
    }

    private void checkPlaybackInfo(Response response) {
        response.getBody().prettyPrint();
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].playbackInfo", notNullValue()
                );
    }

    @Test
    public void testPlayGeneralMusic() {
        List<String> queryList = Arrays.asList(
                "q=music&version=1&token=AawZFcTcXakSPaZdR0rejp9GIFCNjBhmCmlOC5czdu229ciznRchdUYxLSSiVdxqG7U7BIwSevqSG2QfbfmLKIM"
        );
        queryList.stream().forEach(q -> checkPlayGeneralMusic(search(q)));
    }

    private void checkPlayGeneralMusic(Response response) {
        response.then()
                .body(
                        "status.code", equalTo(200),
                        "data[0].id", equalTo("st.763226285"),
                        "data[0].radioType", equalTo("User"),
                        "data[0].contentType", equalTo("Radio Station"),
                        "data[0].introSongsDetail", hasSize(2),
                        "data[0].introSongsDetail[0].id", notNullValue(),
                        "data[0].introSongsDetail[0].contentType", equalTo("Song"),
                        "data[0].introSongsDetail[1].id", notNullValue(),
                        "data[0].introSongsDetail[1].contentType", equalTo("Song")
                );
    }

}
