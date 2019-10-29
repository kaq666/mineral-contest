package eu.billyinc.mineralcontest.manager;

import eu.billyinc.mineralcontest.App;

public class MineralContestManager {
	
	private App app;
	private MineralContestChestManager mineralContestChestManager = new MineralContestChestManager();
	private MineralContestGameManager mineralContestGameManager = new MineralContestGameManager();
	private MineralContestPlayerManager mineralContestPlayerManager = new MineralContestPlayerManager();

	private MineralContestManager() {
		
	}
	
	private static MineralContestManager INSTANCE = new MineralContestManager();
	
	private static MineralContestManager getInstance() {
		return INSTANCE;
	}
	
	public static App getApp() {
		return getInstance().app;
	}

	public static void setApp(App app) {
		getInstance().app = app;
	}
	
	public static MineralContestChestManager getMineralContestChestManager() {
		return getInstance().mineralContestChestManager;
	}
	
	public static MineralContestGameManager getMineralContestGameManager() {
		return getInstance().mineralContestGameManager;
	}
	
	public static MineralContestPlayerManager getMineralContestPlayerManager() {
		return getInstance().mineralContestPlayerManager;
	}
}
