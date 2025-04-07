/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Oauth2;

import com.example.dao.OauthDAO;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.BASE64EncoderStream;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Humphrey
 */
public class Oauth2mail {

    StringBuffer oauth2;

//STEP 2
    private String mailOauth2() {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://accounts.google.com/o/oauth2/token");
        String client_id = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com";
        String client_secret = "xxxxxxxxxxxxx-xxxx";
        OauthDAO dao = new OauthDAO();
        String code = dao.getCode().trim();
        String redirect_uri = "http://localhost";
        String grant_type = "authorization_code";

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>(5);
            nameValuePairs.add(new BasicNameValuePair("code", code));//code
            nameValuePairs.add(new BasicNameValuePair("client_id", client_id));//cilent id
            nameValuePairs.add(new BasicNameValuePair("client_secret", client_secret));//cilent secret
            nameValuePairs.add(new BasicNameValuePair("redirect_uri", redirect_uri));//redirect url
            nameValuePairs.add(new BasicNameValuePair("grant_type", grant_type));//grant_type

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            org.apache.http.HttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            oauth2 = new StringBuffer();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                oauth2.append(line);
            }

        } catch (IOException e) {
            System.err.println("mailOauth2 error : " + e);
        }
        String oauth2request = oauth2.toString();

        return oauth2request;
    }

//STEP 3
    public void getOauth2() {
        String oauth2url = mailOauth2();
        String[] urlResponse = oauth2url.split("\"access_token\": \"");
//String access1 = urlResponse[0]; // 004
        String access2 = urlResponse[1];
//Access Token
        String[] access3 = access2.split("\",  \"expires_in\": ");
        String access_code = access3[0]; // access code
        String access4 = access3[1];//
//Expires in
        String[] access5 = access4.split(",  \"refresh_token\": \"");
        String expires_in = access5[0]; // expires in
        String access6 = access5[1];//
//Refresh Token
        String[] access7 = access6.split("\",  \"scope\": \"");
        String refresh_token = access7[0]; // Refresh Token
        String access8 = access7[1];//
//Scope
        String[] access9 = access8.split("\",  \"token_type\": \"");
        String scope = access9[0]; // Scope
        String access10 = access9[1];
//Token Type
        String[] access11 = access10.split("\"}");
        String token_type = access11[0]; // Token Type
//STEP 3.1
//SAVE OAUTH2 ACCESS IN DATABASE
        try {
            OauthDAO oauthdao = new OauthDAO();

            // Call update method
            oauthdao.updateEmailSettings(refresh_token, access_code, expires_in, scope, token_type);
            // JOptionPane.showMessageDialog(this,"Discount Update","Alert",JOptionPane.OK_OPTION);
            //STEP 5
            //SENDING TEST MAIL
            String SMTP_SERVER_HOST = "smtp.gmail.com";
            String SMTP_SERVER_PORT = "587";
            String SUBJECT = "Test mail from: AVANTSTEEL";
            String BODY = "Hi,<br><br>This is a programmatic email.Do not reply";
            //

            String FROM_USER_EMAIL = null;
            String FROM_USER_FULLNAME = "AVANTSTEEL ";
            String FROM_USER_ACCESSTOKEN = access_code;
            //
            OauthDAO dao = new OauthDAO();

            // Fetch username and recipient
            String[] emails = dao.getEmailSettings();
            System.out.println("From: " + emails[0]);
            System.out.println("To: " + emails[1]);

            //SEND TEST MAIL
            sendMail(SMTP_SERVER_HOST, SMTP_SERVER_PORT, emails[0], FROM_USER_ACCESSTOKEN, FROM_USER_EMAIL, FROM_USER_FULLNAME, emails[0], SUBJECT, BODY);
            //POP UP SUCCESFULL MAIL SENT
            JOptionPane.showMessageDialog(null, "Test Mail Sucessful Sent ", "Sucess", JOptionPane.INFORMATION_MESSAGE);

        } catch (HeadlessException esql) {
            Logger.getLogger(Oauth2mail.class.getName()).log(Level.SEVERE, null, esql);
        }

    }

//STEP 4
//SENDING MAIL
    void sendMail(String smtpServerHost, String smtpServerPort, String smtpUserName, String smtpUserAccessToken, String fromUserEmail, String fromUserFullName, String toEmail, String subject, String body) {
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", smtpServerPort);
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromUserEmail, fromUserFullName));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");

            SMTPTransport transport = new SMTPTransport(session, null);
            transport.connect(smtpServerHost, smtpUserName, null);
            transport.issueCommand("AUTH XOAUTH2 " + new String(BASE64EncoderStream.encode(String.format("user=%s\1auth=Bearer %s\1\1", smtpUserName, smtpUserAccessToken).getBytes())), 235);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (UnsupportedEncodingException | MessagingException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
