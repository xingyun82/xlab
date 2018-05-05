package xlab.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TaskExecutor {

    private static final ExecutorService executorSerivce = Executors.newCachedThreadPool();

    public static List<Object> executeAll(List<TimingTask> tasks)
            throws ExecutionException, InterruptedException {

        List<CompletableFuture<Object>> queryFutures = new ArrayList<>();
        tasks.forEach(task -> queryFutures.add(execute(task)));
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(queryFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Object>> allQueryFutures = allFutures.thenApply(
                v -> queryFutures.stream().map(queryFuture -> queryFuture.join()).collect(Collectors.toList())
        );

        return allQueryFutures.get();
    }

    private static CompletableFuture execute(TimingTask task) {
        return CompletableFuture.supplyAsync(() -> {
            Future future = executorSerivce.submit(task.getCallable());
            Object result = null;
            try {
                result = future.get(task.getTimeout(), task.getTimeUnit());
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                future.cancel(true);
            }
            return result;
        });
    }
}
