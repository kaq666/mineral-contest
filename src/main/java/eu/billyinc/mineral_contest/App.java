package eu.billyinc.mineral_contest;

import org.bukkit.plugin.java.JavaPlugin;

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
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Mineral Contest Disabled");
    }
}
