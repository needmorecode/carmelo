package carmelo.timer;

import java.util.concurrent.atomic.AtomicInteger;

import carmelo.log.CarmeloLogger;
import carmelo.log.LogUtil;

public abstract class TimerTask implements Runnable{
	
	private static AtomicInteger currId = new AtomicInteger(0);
	
	private int taskId;
	
	private String name;
	
	private long execTime;
	
	private boolean isCancelled;
	
	public TimerTask(String name, long execTime) {
		this.taskId = currId.incrementAndGet();
		this.name = name;
		this.execTime = execTime;
	}

	public int getTaskId() {
		return taskId;
	}

	public long getExecTime() {
		return execTime;
	}
	
	public void cancel() {
		this.isCancelled = true;
	}
	
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	public String getName() {
		return name;
	}
	
	public void run() {
		befExec();
		try {
			exec();
		} finally {
			aftExec();
		}
	}
	
	public abstract void exec();
	
	private void befExec() {
		CarmeloLogger.TIMER.info(LogUtil.buildTimerStartLog(this));
	}
	
	private void aftExec() {
		CarmeloLogger.TIMER.info(LogUtil.buildTimerEndLog(this));
	}

}
