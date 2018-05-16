package com.sstyle.server.utils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @Author ss
 * @Date 2018/4/11 14:38
 */
public class EmailUtil {
    private static String myEmailAccount = "shaoshuai@raiyee.com";
    private static String myEmailPassword = "ss8181066";
    private static String myEmailSMTPHost = "smtp.ym.163.com";
    private static String receiveMailAccount = "yourEmailAccount";
//    private static String callBackUrl = "http://localhost:8999/#/active?checkCode=";
    private static String callBackUrl = "http://www.52nino.cn/#/active?checkCode=";


    public static void sendEmail(String checkCode, String userId, String receiveAccount) {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        Session session = Session.getInstance(props);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;
        try {
            message.setFrom(new InternetAddress(myEmailAccount, "尼诺的博客", "UTF-8"));
            // 3. To: 收件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveAccount));
            //    Cc: 抄送（可选）
//            message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
            //    Bcc: 密送（可选）
//            message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));
            message.setSubject("欢迎注册尼诺的博客，很高兴认识你，点击下面链接激活吧！", "UTF-8");
            // 5. Content: 邮件正文（可以使用html标签）
            message.setContent("<a target='_BLANK' href='" + callBackUrl + checkCode + "&userId=" + userId + "'>" + callBackUrl + checkCode + "&userId=" + userId + "</a>", "text/html;charset=UTF-8");
            // 6. 设置显示的发件时间
            message.setSentDate(new Date());
            // 7. 保存前面的设置
            message.saveChanges();
            transport =  session.getTransport();
            transport.connect(myEmailAccount, myEmailPassword);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                transport.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

}
