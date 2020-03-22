import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorTest {
    private static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    @Test
    public void executorTest() {
        EXECUTOR_SERVICE.execute(() -> System.out.println("1 is done"));
        EXECUTOR_SERVICE.execute(() -> System.out.println("2 is done"));
//        EXECUTOR_SERVICE.shutdown();
    }
}
