package com.XiaoHuiHui.LimitEnchantment;

import java.util.logging.Level;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.XiaoHuiHui.LimitEnchantment.LEListener;

public class LEMain extends JavaPlugin {
	//�¼�������
	LEListener listener = new LEListener(this);
	//���ݸ�������������
	LEData data=LEData.initialize(this);
	//�������
	LECmd cmd=new LECmd(this);
	//�汾��
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
			getLogger().info("���������ϣ��汾:" +getVersion() + " ����:С�һ�");
		}else{
			getLogger().log(Level.SEVERE,"��������쳣���汾:" +getVersion() + " ����:С�һ�");
		}
	}
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		getLogger().info("����Ѿ��ر��ˣ��汾:" +getVersion() + " ����:С�һ�");
	}
}
