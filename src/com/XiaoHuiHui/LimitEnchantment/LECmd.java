package com.XiaoHuiHui.LimitEnchantment;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LECmd implements CommandExecutor{
	LEMain main;
	LEData data;
	
	//Constructor
	public LECmd(LEMain main) {
		this.main=main;
		try {
			data=LEData.getInstance();
		} catch (Exception e) {
			data=LEData.initialize(main);
		}
	}

	//�����������Ӧ�����setflag
	private void setFlag(String string, String string2, CommandSender sender) {
		sender.sendMessage("��1���Ƽ�ʹ�ñ������Щ���ù��ڸ��ӣ�����config.yml���޸�");
		String temp1=string.toLowerCase();
		boolean b=Boolean.parseBoolean(string2);
		switch(temp1){
		case "maxlevel":
			int i;
			try{
				i=Integer.parseInt(string2);
			}catch(NumberFormatException e){
				sender.sendMessage(string2+"��4������Ч�����֣�");
				e.printStackTrace();
				return;
			}
			data.setMaxLevel(i);
			data.write(sender);
			sender.sendMessage("��2�޸���ϣ�");
			return;
		case "isbanned":
			data.setBanned(b);
			data.write(sender);
			sender.sendMessage("��2�޸���ϣ�");
			return;
		case "isdelitem":
			data.setDelItem(b);
			data.write(sender);
			sender.sendMessage("��2�޸���ϣ�");
			return;
		case "isclearenchantment":
			data.setClearEnchantment(b);
			data.write(sender);
			sender.sendMessage("��2�޸���ϣ�");
			return;
		case "message":
			data.setMessage(string2);
			data.write(sender);
			sender.sendMessage("��2�޸���ϣ�");
			return;
		default:
			sender.sendMessage("��4��Ч���");
			return;
		}
	}

	//����
	private void help(CommandSender sender) {
		sender.sendMessage("��a/LimitEnchantment reload ���������ļ�");
		sender.sendMessage("��a/LimitEnchantment setFlag <index> <value> ����������");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		int leng=args.length;
		if(leng == 0) return false;
		String arg1=args[0].toLowerCase();
		switch(leng){
		case 1:
			switch(arg1){
			case "help":
				help(sender);
				return true;
			case "reload":
				data.load(sender);
				return true;
			}
		case 3:
			if(arg1.equals("setflag")){
				setFlag(args[1], args[2], sender);
				return true;
			}
		}
		return false;
	}
}
