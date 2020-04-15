package eu.billyinc.mineralcontest.task;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
	
	private volatile int remainingCountDownTime = 10;
	private volatile int remainingGameTime = 3600;

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
