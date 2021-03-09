package com.XiaoHuiHui.LimitEnchantment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

/*插件主体、命令处理类、事件监听器之间的信息交互使用本类对象
 * 采用信息独立机制，即本类专门负责与config.yml的数据交互
 * 所有数据设置在这里后统一写入
 * 并统一在这里读取，已保证时效性
 */
public class LEData {
	//开启单例模式！
	static LEData data=new LEData();
	//是否初始化的flag
	static boolean flag=false;
	
	public static LEData getInstance() throws Exception{
		if(!flag){
			throw new Exception("你丫还没初始化呢！！！！");
		}
		return data;
	}
	
	//真·Constructor
	public static LEData initialize(LEMain main) {
		data.main=main;
		data.fileConfig = new File(main.getDataFolder(), "config.yml");
		flag=true;
		return data;
	}
	
	LEMain main;
	/* 配置文件路径，因为主类的saveConfig()不抛错，
	  * 所以只能写点多余的辣鸡代码让它抛错，这才是我的风格！
	  * 这个对象就是多余的，因为还需要重新定义
	  * 本来主类定义了的，但是用的private
	  * 我真是x了狗了(误)
	  */
	File fileConfig;
	FileConfiguration config;
	
	//最大允许的附魔等级
	int maxLevel;
	//是否开启Ban人模式
	boolean isBanned;
	//提示信息
	String Message;
	//是否删除该物品
	boolean isDelItem;
	//是否清除附魔
	boolean isClearEnchantment;
	//是否开启详细附魔限制功能
	boolean isMoreEnchantmentEnable;
	//是否开启物品附魔限制功能
	boolean isMoreBanItemEnable;
	//详细附魔限制功能的列表
	Map<Enchantment,Integer> moreEnchantment=new HashMap<Enchantment,Integer>();
	//物品附魔限制功能的字符串列表
	List<String> moreBanItemStringList=new ArrayList<String>();
	//物品附魔限制功能的列表
	Map<Integer,List<Enchantment>> moreBanItemList=new HashMap<Integer,List<Enchantment>>();
	
	//config.yml的版本
	public static final String version="1.0";

	//getter and setter
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	public boolean isBanned() {
		return isBanned;
	}
	
	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public void setMessage(String message) {
		Message = message;
	}
	
	public boolean isDelItem() {
		return isDelItem;
	}
	
	public void setDelItem(boolean isDelItem) {
		this.isDelItem = isDelItem;
	}
	
	public boolean isClearEnchantment() {
		return isClearEnchantment;
	}

	public void setClearEnchantment(boolean isClearEnchantment) {
		this.isClearEnchantment = isClearEnchantment;
	}
	
	public boolean isMoreEnchantmentEnable() {
		return isMoreEnchantmentEnable;
	}

	public void setMoreEnchantmentEnable(boolean isMoreEnchantmentEnable) {
		this.isMoreEnchantmentEnable = isMoreEnchantmentEnable;
	}

	public boolean isMoreBanItemEnable() {
		return isMoreBanItemEnable;
	}

	public void setMoreBanItemEnable(boolean isMoreBanItemEnable) {
		this.isMoreBanItemEnable = isMoreBanItemEnable;
	}

	public Map<Enchantment, Integer> getMoreEnchantment() {
		return moreEnchantment;
	}

	public void setMoreEnchantment(Map<Enchantment, Integer> moreEnchantment) {
		this.moreEnchantment = moreEnchantment;
	}

	public List<String> getMoreBanItemStringList() {
		return moreBanItemStringList;
	}

	public void setMoreBanItemStringList(List<String> list) {
		this.moreBanItemStringList = list;
	}

	public Map<Integer, List<Enchantment>> getMoreBanItemList() {
		return moreBanItemList;
	}

	public void setMoreBanItemList(Map<Integer, List<Enchantment>> moreBanItemList) {
		this.moreBanItemList = moreBanItemList;
	}
	
	public static String getVersion() {
		return version;
	}
	
	//Constructor
	private LEData(){
		
	}

	//读取config的数据，出问题就抛异常
	private void load0() throws Exception{
		main.saveDefaultConfig();
		config=main.getConfig();
		String version=config.getString("version");
		if(!verifyVersion(version)){
			throw new IllegalArgumentException(
					"config.yml版本无法读取或不符合当前版本！请使用最新版本并不要改动config.yml的version项！");
		}
		setMaxLevel(config.getInt("maxLevel"));
		setBanned( config.getBoolean("isBanned"));
		setMessage(config.getString("message"));
		setDelItem(config.getBoolean("isDelItem"));
		setClearEnchantment(config.getBoolean("isClearEnchantment"));
		setMoreEnchantmentEnable(config.getBoolean("more.enchantment.enable"));
		setMoreBanItemEnable(config.getBoolean("more.banItem.enable"));
		if(isMoreEnchantmentEnable()){
			Enchantment[] enchants=Enchantment.values();
			for(int i=0;i<enchants.length;i++)
				moreEnchantment.put(enchants[i],config.getInt("more.enchantment."+enchants[i].getName()));
		}
		if(isMoreBanItemEnable()){
			setMoreBanItemStringList(config.getStringList("more.banItem.list"));
		}
		banItemStringListToMap();
	}
	
	//把读取的字符串列表转换成映射，即解读这部分数据
	private void banItemStringListToMap() {
		List<String> list=getMoreBanItemStringList();
		for(int i=0;i<list.size();i++){
			String temp1=list.get(i);
			if(temp1.contains(":")){
				integerToEnchantment(i, temp1,":");
			}else if(temp1.contains(";")){
				integerToEnchantment(i, temp1,";");
			}else{
				integerToEnchantment(i, temp1,";");
			}
		}
	}
	
	/* 重构函数，处理数值到附魔的转换
	  * 具体的添加部分再次进行的重构
	  * 解释下原理：
	  * 这里判断分割符，如果是:分割符的话
	  * 表示这里提供的是要禁用的附魔ID
	  * 所以直接转换成附魔对象添加到列表即可
	  * 如果不是:分割，那么一定是;分割或者无分割符
	  * 那么这两种情况可以一并处理
	  * 处理方式 先把列表填满所有的附魔类型
	  * 然后进行一个个的删除，无分割符当然就不用进行删除
	  */
	private void integerToEnchantment(int i, String field,String splitChar) {
		String temp2[]=field.split(splitChar);
		boolean flag=splitChar.equals(":");
		int temp3;
		try{
			temp3=Integer.parseInt(temp2[0]);
		}catch(NumberFormatException e){
			throw new IllegalArgumentException("banItem列表的物品ID不合法！所在行数："+(i+1));
		}
		List<Enchantment> temp4;
		if(flag){
			temp4=new ArrayList<Enchantment>();
		}else{
			temp4=Arrays.asList(Enchantment.values());
		}
		for(int j=1;j<temp2.length;j++){
			int temp5;
			try{
				temp5=Integer.parseInt(temp2[j]);
			}catch(NumberFormatException e){
				throw new IllegalArgumentException("banItem列表的附魔ID不合法！所在行数："+(i+1));
			}
			if(temp5>64){
				switch(temp5){
				case 65:
					chanceMultiEnchantments(0,9,temp4,flag);
					break;
				case 66:
					chanceMultiEnchantments(16,21,temp4,flag);
					break;
				case 67:
					chanceMultiEnchantments(32,33,temp4,flag);
					break;
				case 68:
					chanceMultiEnchantments(34,51,temp4,flag);
					break;
				case 69:
					chanceMultiEnchantments(61,62,temp4,flag);
					break;
				}
			}else{
				changeSingleEnchantment(temp4, temp5,flag);
			}
		}
		getMoreBanItemList().put(temp3,temp4);
	}

	//重构函数，添加单个附魔
	@SuppressWarnings("deprecation")
	private void changeSingleEnchantment(List<Enchantment> temp4, int temp5,boolean flag) {
		if(flag){
			temp4.add(Enchantment.getById(temp5));
		}else{
			temp4.remove(Enchantment.getById(temp5));
		}
	}

	/* 重构函数，添加多个相邻的附魔
	  * 参数i表示这些附魔ID的起始值，j表示末尾值
	  * 添加从i到j的所有ID的附魔
	  */
	private void chanceMultiEnchantments(int i, int j, List<Enchantment> temp4, boolean flag) {
		for(int k=i;k<=j;k++){
			changeSingleEnchantment(temp4,k,flag);
		}
	}

	//检查配置文件的版本
	private boolean verifyVersion(String version) {
		if(version==null)return false;
		return version.equalsIgnoreCase(getVersion());
	}
	
	//封装异常，更友好的显示，有参数表示控制台或者玩家执行命令
	public boolean load(CommandSender sender) {
		try {
			load0();
		} catch (Exception e) {
			sender.sendMessage(" §4读取配置文件信息出错！请结合错误代码进行问题判断！");
			e.printStackTrace();
			return false;
		}
		sender.sendMessage(" §2读取配置文件成功！");
		return true;
	}
	
	//封装异常，更友好的显示，无参表示系统调用
	public boolean load() {
		try {
			load0();
		} catch (Exception e) {
			main.getLogger().log(Level.SEVERE,"读取配置文件信息出错！请结合错误代码进行问题判断！");
			main.getServer().getPluginManager().disablePlugin(main);
			e.printStackTrace();
			return false;
		}
		main.getLogger().info("读取配置文件信息成功！");
		return true;
	}

	//写配置
	private void write0() {
		config.set("maxLevel",getMaxLevel());
		config.set("isBanned",isBanned());
		config.set("message",getMessage());
		config.set("isDelItem",isDelItem());
		config.set("isClearEnchantment",isClearEnchantment());
	}
	
	//写入之后保存，封装异常，友好的显示，无参表示系统调用
	public boolean write(){
		write0();
		try{
			config.save(fileConfig);
		}catch(IOException e){
			main.getLogger().log(Level.SEVERE,"改动配置文件出错！请确定参数无误，磁盘未满且未被写保护！");
			e.printStackTrace();
			return false;
		}
		main.getLogger().info("已成功改动配置文件！");
		return true;
	}
	
	//写入之后保存，封装异常，友好的显示，有参数表示控制台或者玩家执行命令
	public boolean write(CommandSender sender){
		write0();
		try{
			config.save(fileConfig);
		}catch(IOException e){
			sender.sendMessage("§4改动配置文件出错！请确定参数无误，磁盘未满且未被写保护！");
			e.printStackTrace();
			return false;
		}
		sender.sendMessage("§2已成功改动配置文件！");
		return true;
	}
}