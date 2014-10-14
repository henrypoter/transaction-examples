package multithread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RaceConditionTest {

    public static final int NUMBER_OF_THREADS = 10;
    private AtomicInteger counter;
    private ExpensiveResource resource;
    private CountDownLatch startSignal;
    private CountDownLatch doneSignal;

    @Before
    public void setUp() {
        counter = new AtomicInteger(0);
        resource = null;
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(NUMBER_OF_THREADS);
    }

    @Test
    public void givenLaizyInitializatingSharedResourceWhenRaceConditionAppearsThenResourceCouldBeCreatedMultipleTimes() throws InterruptedException {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            startLaizyInitializatior(startSignal, doneSignal);
        }
        startSignal.countDown();
        doneSignal.await();
        Assert.assertEquals(1, counter.get());
    }

    private void startLaizyInitializatior(final CountDownLatch startSignal, final CountDownLatch doneSignal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSignal.await();
                    if (resource == null) {
                        resource = new ExpensiveResource();
                    }
                    doneSignal.countDown();
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    public class ExpensiveResource {
        public ExpensiveResource() {
            counter.incrementAndGet();
        }
    }
}