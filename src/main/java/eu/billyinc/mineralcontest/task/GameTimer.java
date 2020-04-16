package eu.billyinc.mineralcontest.task;

import java.util.TimerTask;

public class GameTimer extends TimerTask {
	
	private int gameTime = 3600;
	private volatile int remainingCountDownTime = 10;
	private volatile int remainingGameTime = 0;
	
	public GameTimer() {
		this.remainingGameTime = this.gameTime;
	}
	
	public GameTimer(int time) {
		this.gameTime = time;
		this.remainingGameTime = this.gameTime;
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
	
	public int getGAMETIME() {
		return this.gameTime;
	}
	
	public int getRemaingGameTime() {
		return this.remainingGameTime;
	}
	
	public int getRemaingCountdownTime() {
		return this.remainingCountDownTime;
	}

}
