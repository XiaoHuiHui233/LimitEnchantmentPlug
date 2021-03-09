package com.XiaoHuiHui.LimitEnchantment;

import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.XiaoHuiHui.LimitEnchantment.LEListener;

public class LEMain extends JavaPlugin {
	//事件监听器
	LEListener listener = new LEListener(this);
	//传递给监听器的数据
	LEData data=LEData.initialize(this);
	//命令处理类
	LECmd cmd=new LECmd(this);
	//版本号
	public static final String version = "00.02.14 BETA";
	
	//getter and setter
	public LEListener getListener() {
		return listener;
	}

	public void setListener(LEListener listener) {
		this.listener = listener;
	}

	public static String getVersion() {
		return version;
	}

	@Override
	public void onEnable() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginCommand("limitenchantment").setExecutor(cmd);
		if(data.load()){
			getLogger().info("插件启动完毕！版本:" +getVersion() + " 制作:小灰灰");
		}else{
			getLogger().log(Level.SEVERE,"插件启动异常！版本:" +getVersion() + " 制作:小灰灰");
		}
	}
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		getLogger().info("插件已经关闭了！版本:" +getVersion() + " 制作:小灰灰");
	}
}
