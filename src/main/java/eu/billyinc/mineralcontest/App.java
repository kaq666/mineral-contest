package eu.billyinc.mineralcontest;

import org.bukkit.plugin.java.JavaPlugin;

import eu.billyinc.mineralcontest.command.MineralContestCommand;
import eu.billyinc.mineralcontest.listener.MineralContestListener;
import eu.billyinc.mineralcontest.manager.MineralContestManager;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin
{
	@Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Mineral Contest Enabled");
        MineralContestManager.setApp(this);
        getCommand("mc").setExecutor(new MineralContestCommand());
        getServer().getPluginManager().registerEvents(new MineralContestListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Mineral Contest Disabled");
    }
}
