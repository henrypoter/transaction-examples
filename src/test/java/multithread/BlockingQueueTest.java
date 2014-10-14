package multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BlockingQueueTest {

    private static final long WAIT_TO_VERIFY = 5000;
    private BlockingQueue<Integer> blockingQueue;

    @Before
    public void setUp() {
        blockingQueue = new LinkedBlockingQueue<Integer>();
    }

    @Test
    public void givenBlockingQueueWhenEmptyThenTakeMethodShouldBeBlocking() throws InterruptedException {
        Taker taker = new Taker();
        taker.start();
        Thread.sleep(WAIT_TO_VERIFY);
        taker.interrupt();
        taker.join();
        Assert.assertFalse(taker.isAlive());
    }

    public void test() {
    }

    private final class Taker extends Thread {
        @Override
        public void run() {
            try {
                blockingQueue.take();
                Assert.fail();
            } catch (InterruptedException e) {
            }
        }
    }
}
