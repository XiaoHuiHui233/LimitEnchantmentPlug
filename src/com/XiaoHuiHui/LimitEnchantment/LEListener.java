package com.XiaoHuiHui.LimitEnchantment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class LEListener implements Listener {
	LEMain main;
	LEData data;

	//Constructor
	public LEListener(LEMain main) {
		this.main=main;
		try {
			data=LEData.getInstance();
		} catch (Exception e) {
			data=LEData.initialize(main);
		}
	}

	@EventHandler
	public void onEnchant(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		Item item = e.getItem();
		doEnchant(player,item,e);
	}

	@EventHandler
	public void onEnchant(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		Item item = e.getItemDrop();
		doEnchant(player,item,e);
	}
	
	@EventHandler
	public void onEnchant(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		doEnchant(player,item,e);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEnchant(PlayerItemHeldEvent e) {
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		doEnchant(player,item,e);
	}
	
	@EventHandler
	public void onEnchant(PrepareItemEnchantEvent e) {
		Player player = e.getEnchanter();
		ItemStack item = e.getItem();
		doEnchant(player,item,e);
	}

	//进行附魔检查，参数不同
	private void doEnchant(Player player,ItemStack item,Cancellable e){
		if (checkEnchantment(item) || checkBanItem(item)){
			e.setCancelled(true);
			String message=data.getMessage();
			message=message.replace('&','§');
			player.sendMessage(message);
			if (data.isBanned()) {
				player.getInventory().clear();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban "+player.getDisplayName()+" 使用超过限定附魔等级的物品");
			}
			player.getInventory().remove(item);
			if(data.isClearEnchantment()){
				Enchantment[] ens=Enchantment.values();
				for(int i=0;i<ens.length;i++){
					item.removeEnchantment(ens[i]);
				}
			}
			if (data.isDelItem()) {
				return;
			}
			player.getInventory().addItem(item);
		}
	}
	
	//对物品附魔限定进行检查
	@SuppressWarnings("deprecation")
	private boolean checkBanItem(ItemStack item) {
		if(item == null || item.getType() == Material.AIR){
			return false;
		}
		if(!data.isMoreBanItemEnable()){
			return false;
		}
		List<Enchantment> temp1=data.getMoreBanItemList().get(item.getTypeId());
		if(temp1==null)return false;
		for(int i=0;i<temp1.size();i++){
			Enchantment en=temp1.get(i);
			if(en==null)continue;
			if(item.containsEnchantment(en)){
				return true;
			}
		}
		return false;
	}

	//进行附魔检查，参数不同
	private void doEnchant(Player player,Item item,Cancellable e){
		ItemStack items=item.getItemStack();
		if (checkEnchantment(items) || checkBanItem(items)) {
			e.setCancelled(true);
			String message=data.getMessage();
			message=message.replace('&','§');
			player.sendMessage(message);
			if (data.isBanned()) {
				player.getInventory().clear();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban "+player.getDisplayName()+" 使用超过限定附魔等级的物品");
			}
			player.getInventory().removeItem(item);
			if(data.isClearEnchantment()){
				Enchantment[] ens=Enchantment.values();
				for(int i=0;i<ens.length;i++){
					item.removeEnchantment(ens[i]);
				}
			}
			if (data.isDelItem()) {
				return;
			}
			player.getInventory().addItem(item);
		}
	}

	//附魔等级的检查函数
	public boolean checkEnchantment(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			return false;
		}
		Map<Enchantment, Integer> map = item.getEnchantments();
		Set<Enchantment> set=map.keySet();
		Map<Enchantment,Integer> temp1 = null;
		 if(data.isMoreEnchantmentEnable()){
			 temp1=data.getMoreEnchantment();
		 }
		for(Enchantment en:set){
			int temp2=data.getMaxLevel();
			if(temp1!=null){
				int temp3=temp1.get(en);
				temp2=temp3==0?temp2:temp3;
			}
			int value = map.get(en);
			if (value > temp2) {
				return true;
			}
		}
		return false;
	}
}
