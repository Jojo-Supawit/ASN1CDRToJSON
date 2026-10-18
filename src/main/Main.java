package main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import decoder.Controller;
import file.Container;
import file.FileSearcher;

public class Main {
	
	private static ThreadPoolExecutor threadPool;
	private static long startTime = System.currentTimeMillis();
	private static Timer timer = new Timer();
	private static TimerTask debugTask = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			printDebug();
		}
	};
	
	private static void printDebug() {
		if(threadPool == null || threadPool.isTerminated()) return;
		long timeElapsed = System.currentTimeMillis() - startTime;
		System.out.print(String.format("%s | %s | %s\r", String.format("%1$02d:%2$02d:%3$02d", timeElapsed/3600000, (timeElapsed/60000)%60, (timeElapsed/1000)%60), String.format("Memory usage: %d MB", bytesToMegabytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())), String.format("Current: %d, Queued: %d, Done: %d", threadPool.getActiveCount(), threadPool.getQueue().size(), threadPool.getCompletedTaskCount())));
	}
	
	private static long bytesToMegabytes(long bytes) {
        return bytes / (1024L * 1024L); //
    }

	public static void main(String[] args) throws Exception {
		if(args.length < 1) throw new Exception("Invalid argument(s)");
		Vector<Container> containers = new Vector<Container>();
		for(String path: args) {
			containers.addAll(FileSearcher.searchAll(Path.of(path), null, null, null));
		}
		Controller defaultController = new Controller();
		timer.scheduleAtFixedRate(debugTask, 1000, 5000); 
		threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
		for(Container con: containers) {
			threadPool.execute(getThread(con, defaultController));
			Thread t = getThread(con, defaultController);
			t.start();
			t.join();
		}
		threadPool.shutdown();
		while(!threadPool.awaitTermination(10, TimeUnit.SECONDS)) Thread.sleep(1000);
		timer.cancel();
	}
	
	public static HashSet<StandardOpenOption> openOptions = new HashSet<StandardOpenOption>(Arrays.asList(StandardOpenOption.WRITE, StandardOpenOption.CREATE));
	
	public static Thread getThread(Container container, Controller controller) {
		Thread t = new Thread(()->{
			String jsonObj = controller.decode(container);
			try {
				FileChannel writer = FileChannel.open(Path.of(container.getFile().getParentFile().getPath(), container.getFileName() + ".json"), openOptions);
				writer.write(ByteBuffer.wrap(jsonObj.getBytes()));
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		});
		return t;
	}

}
