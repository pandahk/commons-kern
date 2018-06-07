package com.vshcxl.common.mail;

import org.junit.Test;

import com.jszhan.commons.kern.apiext.mail.Mail;

public class MailTest {

	@Test
	public void send(){
	        String smtp = "smtp.qq.com";  //SMTP服务器
	        String to = "928684283@qq.com";  
	        String from = "2796717901@qq.com";  
	        String copyto = "";  //抄送人
	        String subject = "资金调拨单报表";  
	        String content = "资金调拨单邮件内容";  
	        String username="2796717901@qq.com";  //用户名
	        String password="kl135778";  //密码
//	        String filePath = "C:\\excel\\"; 
	        
//	        String filename ="资金调拨单_信托计划名称_20150923.xls";
	        Mail.sendAndCc(smtp, from, to, copyto, subject, content, username, password);  
	}
}
