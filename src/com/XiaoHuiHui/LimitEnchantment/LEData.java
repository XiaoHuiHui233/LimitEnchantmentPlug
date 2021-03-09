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

/*������塢������ࡢ�¼�������֮�����Ϣ����ʹ�ñ������
 * ������Ϣ�������ƣ�������ר�Ÿ�����config.yml�����ݽ���
 * �������������������ͳһд��
 * ��ͳһ�������ȡ���ѱ�֤ʱЧ��
 */
public class LEData {
	//��������ģʽ��
	static LEData data=new LEData();
	//�Ƿ��ʼ����flag
	static boolean flag=false;
	
	public static LEData getInstance() throws Exception{
		if(!flag){
			throw new Exception("��Ѿ��û��ʼ���أ�������");
		}
		return data;
	}
	
	//�桤Constructor
	public static LEData initialize(LEMain main) {
		data.main=main;
		data.fileConfig = new File(main.getDataFolder(), "config.yml");
		flag=true;
		return data;
	}
	
	LEMain main;
	/* �����ļ�·������Ϊ�����saveConfig()���״�
	  * ����ֻ��д�������������������״�������ҵķ��
	  * ���������Ƕ���ģ���Ϊ����Ҫ���¶���
	  * �������ඨ���˵ģ������õ�private
	  * ������x�˹���(��)
	  */
	File fileConfig;
	FileConfiguration config;
	
	//�������ĸ�ħ�ȼ�
	int maxLevel;
	//�Ƿ���Ban��ģʽ
	boolean isBanned;
	//��ʾ��Ϣ
	String Message;
	//�Ƿ�ɾ������Ʒ
	boolean isDelItem;
	//�Ƿ������ħ
	boolean isClearEnchantment;
	//�Ƿ�����ϸ��ħ���ƹ���
	boolean isMoreEnchantmentEnable;
	//�Ƿ�����Ʒ��ħ���ƹ���
	boolean isMoreBanItemEnable;
	//��ϸ��ħ���ƹ��ܵ��б�
	Map<Enchantment,Integer> moreEnchantment=new HashMap<Enchantment,Integer>();
	//��Ʒ��ħ���ƹ��ܵ��ַ����б�
	List<String> moreBanItemStringList=new ArrayList<String>();
	//��Ʒ��ħ���ƹ��ܵ��б�
	Map<Integer,List<Enchantment>> moreBanItemList=new HashMap<Integer,List<Enchantment>>();
	
	//config.yml�İ汾
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

	//��ȡconfig�����ݣ�����������쳣
	private void load0() throws Exception{
		main.saveDefaultConfig();
		config=main.getConfig();
		String version=config.getString("version");
		if(!verifyVersion(version)){
			throw new IllegalArgumentException(
					"config.yml�汾�޷���ȡ�򲻷��ϵ�ǰ�汾����ʹ�����°汾����Ҫ�Ķ�config.yml��version�");
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
	
	//�Ѷ�ȡ���ַ����б�ת����ӳ�䣬������ⲿ������
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
	
	/* �ع�������������ֵ����ħ��ת��
	  * �������Ӳ����ٴν��е��ع�
	  * ������ԭ��
	  * �����жϷָ���������:�ָ���Ļ�
	  * ��ʾ�����ṩ����Ҫ���õĸ�ħID
	  * ����ֱ��ת���ɸ�ħ������ӵ��б���
	  * �������:�ָ��ôһ����;�ָ�����޷ָ��
	  * ��ô�������������һ������
	  * ����ʽ �Ȱ��б��������еĸ�ħ����
	  * Ȼ�����һ������ɾ�����޷ָ����Ȼ�Ͳ��ý���ɾ��
	  */
	private void integerToEnchantment(int i, String field,String splitChar) {
		String temp2[]=field.split(splitChar);
		boolean flag=splitChar.equals(":");
		int temp3;
		try{
			temp3=Integer.parseInt(temp2[0]);
		}catch(NumberFormatException e){
			throw new IllegalArgumentException("banItem�б����ƷID���Ϸ�������������"+(i+1));
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
				throw new IllegalArgumentException("banItem�б�ĸ�ħID���Ϸ�������������"+(i+1));
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

	//�ع���������ӵ�����ħ
	@SuppressWarnings("deprecation")
	private void changeSingleEnchantment(List<Enchantment> temp4, int temp5,boolean flag) {
		if(flag){
			temp4.add(Enchantment.getById(temp5));
		}else{
			temp4.remove(Enchantment.getById(temp5));
		}
	}

	/* �ع���������Ӷ�����ڵĸ�ħ
	  * ����i��ʾ��Щ��ħID����ʼֵ��j��ʾĩβֵ
	  * ��Ӵ�i��j������ID�ĸ�ħ
	  */
	private void chanceMultiEnchantments(int i, int j, List<Enchantment> temp4, boolean flag) {
		for(int k=i;k<=j;k++){
			changeSingleEnchantment(temp4,k,flag);
		}
	}

	//��������ļ��İ汾
	private boolean verifyVersion(String version) {
		if(version==null)return false;
		return version.equalsIgnoreCase(getVersion());
	}
	
	//��װ�쳣�����Ѻõ���ʾ���в�����ʾ����̨�������ִ������
	public boolean load(CommandSender sender) {
		try {
			load0();
		} catch (Exception e) {
			sender.sendMessage(" ��4��ȡ�����ļ���Ϣ�������ϴ��������������жϣ�");
			e.printStackTrace();
			return false;
		}
		sender.sendMessage(" ��2��ȡ�����ļ��ɹ���");
		return true;
	}
	
	//��װ�쳣�����Ѻõ���ʾ���޲α�ʾϵͳ����
	public boolean load() {
		try {
			load0();
		} catch (Exception e) {
			main.getLogger().log(Level.SEVERE,"��ȡ�����ļ���Ϣ�������ϴ��������������жϣ�");
			main.getServer().getPluginManager().disablePlugin(main);
			e.printStackTrace();
			return false;
		}
		main.getLogger().info("��ȡ�����ļ���Ϣ�ɹ���");
		return true;
	}

	//д����
	private void write0() {
		config.set("maxLevel",getMaxLevel());
		config.set("isBanned",isBanned());
		config.set("message",getMessage());
		config.set("isDelItem",isDelItem());
		config.set("isClearEnchantment",isClearEnchantment());
	}
	
	//д��֮�󱣴棬��װ�쳣���Ѻõ���ʾ���޲α�ʾϵͳ����
	public boolean write(){
		write0();
		try{
			config.save(fileConfig);
		}catch(IOException e){
			main.getLogger().log(Level.SEVERE,"�Ķ������ļ�������ȷ���������󣬴���δ����δ��д������");
			e.printStackTrace();
			return false;
		}
		main.getLogger().info("�ѳɹ��Ķ������ļ���");
		return true;
	}
	
	//д��֮�󱣴棬��װ�쳣���Ѻõ���ʾ���в�����ʾ����̨�������ִ������
	public boolean write(CommandSender sender){
		write0();
		try{
			config.save(fileConfig);
		}catch(IOException e){
			sender.sendMessage("��4�Ķ������ļ�������ȷ���������󣬴���δ����δ��д������");
			e.printStackTrace();
			return false;
		}
		sender.sendMessage("��2�ѳɹ��Ķ������ļ���");
		return true;
	}
}