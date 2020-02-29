package carmelo.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import carmelo.log.CarmeloLogger;
import carmelo.log.LogUtil;

import java.util.concurrent.Executors;

public class Timer {

	private PriorityQueue<TimerTask> tasks = new PriorityQueue<TimerTask>();
	
	private ExecutorService executor = Executors.newFixedThreadPool(5);

	private TimerThread timerThread = new TimerThread();
	
	public Timer() {
		timerThread.start();
	}
	
	public void schedule(TimerTask newTask) {
		synchronized(tasks) {
			if (tasks.isEmpty()) {
				tasks.add(newTask);
				tasks.notify();
			}
			else {
				tasks.add(newTask);
			}
		}
		CarmeloLogger.TIMER.info(LogUtil.buildTimerScheduleLog(newTask));
	}

	class TimerThread extends Thread {

		public void run() {
			while (true) {
				try {
					synchronized(tasks) {
						while (tasks.isEmpty()) {
							tasks.wait();
						}
						long currTime = System.currentTimeMillis();
						while (!tasks.isEmpty()) {
							TimerTask task = tasks.peek();
							if (task.getExecTime() <= currTime) {
								tasks.poll();
								
								if (!task.isCancelled())
									executor.execute(task); 
							}
							else 
								break;
						}
					}
					
					Thread.sleep(300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String args[]) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask("myTask", System.currentTimeMillis() + 3000) {

			@Override
			public void exec() {
				System.out.println("hit1");
			}
			
		});
		
		timer.schedule(new TimerTask("myTask", System.currentTimeMillis() + 4000) {

			@Override
			public void exec() {
				System.out.println("hit2");
			}
			
		});
		
		TimerTask t3 = new TimerTask("myTask", System.currentTimeMillis() + 5000) {

			@Override
			public void exec() {
				System.out.println("hit3");
			}
			
		};
		timer.schedule(t3);
		t3.cancel();
		
		timer.schedule(new TimerTask("myTask", System.currentTimeMillis() + 1000) {

			@Override
			public void exec() {
				System.out.println("hit4");
			}
			
		});
		
//		for (int i = 1; i <= 15; i++) {
//			timer.schedule(new TimerTask("myTask", System.currentTimeMillis() + 1000) {
//
//				@Override
//				public void exec() {
//					int i = 0 / 0;
//					System.out.println("hot");
//				}
//				
//			});
//		}
	}

}
