package cz.perwin.digitalclock;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.perwin.digitalclock.utils.Update;

public class Version {
	private static Thread updatingThread;
	private static Update actualVersion;
	
	protected static void check(final String currentVersion) {
		System.out.println("[DigitalClock] Checking if there is a newer version...");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if(currentVersion.equals(Version.getActualVersion().getVersion())) {
					System.out.println("[DigitalClock] Version of this plugin (v"+ currentVersion +") is actual.");
				} else {
					System.out.println("[DigitalClock] There is a newer version (v"+ Version.getActualVersion().getVersion() +") of this plugin (v"+ currentVersion +"). Download it from "+ Version.getActualVersion().getDownloadLink() +" or use command '/dc update'.");
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch(InterruptedException e) {
			//e.printStackTrace();
			System.err.println("[DigitalClock] Can't check the version, an error occured ("+ e +")");
		}
	}
	
	public static Update getActualVersion() {
		if(Version.actualVersion == null) {
			Version.actualVersion = new Update(47070, "1eeca4e1c3d27f01e69f3b045cb3769c8ff214d8");
		}
		return Version.actualVersion;
	}
	
	public static void update(final CommandSender sender, final String prefix, String currentVersion, final String pluginName) {
		if(Version.updatingThread != null) {
			sender.sendMessage(ChatColor.DARK_GREEN + prefix + ChatColor.GREEN +" Plugin is currently updating");
		} else {
			if(Version.getActualVersion().getVersion().equals(currentVersion)) {
				sender.sendMessage(ChatColor.DARK_RED + prefix + ChatColor.RED +" Your version is actual so it can't be updated");
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + prefix + ChatColor.GREEN +" Updating process started, please wait until it finishes");
				
				Version.updatingThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							File file = new File(Bukkit.getPluginManager().getPlugin(pluginName).getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
							if(file.exists()) {
								file.delete();
							}
						
							URL url = new URL(Version.getActualVersion().getDownloadLink());
							ReadableByteChannel rbc = Channels.newChannel(url.openStream());
							FileOutputStream fos = new FileOutputStream(file);
							fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
							fos.close();

							this.sendMessage(ChatColor.DARK_GREEN + prefix + ChatColor.GREEN +" Plugin has been updated. Use '/reload' command or restart the server");
						} catch(Exception e) {
							//e.printStackTrace();
							this.sendMessage(ChatColor.DARK_RED + prefix + ChatColor.RED +" An error occured when updating plugin ("+ e +")");
						} finally {
							Version.updatingThread = null;
						}
					}
					
					private void sendMessage(String msg) {
						if(!(sender instanceof Player && !((Player) sender).isOnline())) {
							sender.sendMessage(msg);
						}
					}
				});
				Version.updatingThread.start();
			}
		}
	}
}
