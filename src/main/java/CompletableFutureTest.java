import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFutureTest {


    public void test() throws Exception {

        String structureQueryRequest = "structure query";
        String rawQueryRequest = "raw query";

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<CompletableFuture<String>> queryFutures = new ArrayList<>();
        queryFutures.add(execute1(structureQueryRequest, executor));
        queryFutures.add(execute2(rawQueryRequest, executor));
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(queryFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<String>> allQueryFutures = allFutures.thenApply(v -> {
            return queryFutures.stream().map(queryFuture -> queryFuture.join()).collect(Collectors.toList());
        });

        List<String> result = allQueryFutures.get();
        System.out.println(result.get(0));
        System.out.println(result.get(1));

        executor.shutdown();

    }

    private CompletableFuture<String> execute1(String request, ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            Future<String> future = executor.submit(() -> {

//                    Random random = new Random();
//                    int ms = random.nextInt(100) * 1000;
//                    System.out.println("thread 1 sleep " + ms + " ms");
//                    Thread.sleep(ms);
                    throw new IllegalArgumentException("Exception 1");

            });
            try {
                return future.get(1, TimeUnit.SECONDS);
            } catch (IllegalArgumentException e) {
                System.out.println("future1 illegalArgument error!");
                future.cancel(true);
                return null;
            } catch (Exception e) {
                System.out.println("future1 timeout error!");
                future.cancel(true);
                return null;
            }
        });
    }

    private CompletableFuture<String> execute2(String request, ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> {
            Future<String> future = executor.submit(() -> {
                try {
                    Random random = new Random();
                    int ms = random.nextInt(120) * 1000;
                    System.out.println("thread 2 sleep " + ms + " ms");
                    Thread.sleep(ms);
                    return "hello, " + request;
                } catch (Exception e) {
                    System.out.println("exception 2");
                    return null;
                }
            });
            try {
                return future.get(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.out.println("future2 timeout error!");
                future.cancel(true);
                return null;
            }
        });
    }

    public static void main(String[] args) throws Exception {
        CompletableFutureTest inst = new CompletableFutureTest();
        inst.test();
    }


}
