package eu.billyinc.mineralcontest.task;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
	
	private volatile int remainingCountDownTime = 10;
	private volatile int remainingGameTime = 3600;
	
	public GameTimer() {
	}
	
	public GameTimer(int time) {
		this.remainingGameTime = time;
	}

	@Override
	public void run() {
		if (remainingCountDownTime > 0) {
			remainingCountDownTime--;
		} else {
			remainingGameTime--;
			if (remainingGameTime <= 0) {
				this.cancel();
			}
		}
	}
	
	public int getRemaingGameTime() {
		return this.remainingGameTime;
	}
	
	public int getRemaingCountdownTime() {
		return this.remainingGameTime;
	}

}
