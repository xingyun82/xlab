package xlab.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TimingTask {

    private final Callable callable;

    private final int timeout;

    private final TimeUnit timeUnit;

    private final String metricName;

    public TimingTask(String metricName, Callable callable, int timeout, TimeUnit timeUnit) {
        if (metricName == null) {
            this.metricName = "TimingTask.DefaultMetric";
        } else {
            this.metricName = metricName;
        }
        this.callable = callable;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public Callable getCallable() {
        return callable;
    }

    public int getTimeout() {
        return timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String getMetricName() {
        return metricName;
    }
}