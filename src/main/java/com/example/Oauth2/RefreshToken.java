/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Oauth2;

import com.example.dao.OauthDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
public class RefreshToken {

    public void refreshToken() {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://accounts.google.com/o/oauth2/token");
        String client_id = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com";
        String client_secret = "xxxxxxxxxxxxx-xxxx";
        OauthDAO dao = new OauthDAO();
        String refresh_token = dao.getRefreshToken().trim();
        String grant_type = "refresh_token";

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("client_id", client_id));//cilent id
            nameValuePairs.add(new BasicNameValuePair("client_secret", client_secret));//cilent secret
            nameValuePairs.add(new BasicNameValuePair("refresh_token", refresh_token));//refresh token
            nameValuePairs.add(new BasicNameValuePair("grant_type", grant_type));//grant_type
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //
            org.apache.http.HttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer refreshtoken = new StringBuffer();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                try {
                    refreshtoken.append(line);
                } catch (Exception uhe) {
                }
            }
            //  System.out.println("Result"+refreshtoken);

            //STEP 3
            String oauth2url = refreshtoken.toString();
            System.out.println("Oauth2 : " + oauth2url);
            String[] urlResponse = oauth2url.split("\"access_token\": \"");
            String access1 = urlResponse[0]; // 004
            System.out.println("Access1 : " + access1);
            String access2 = urlResponse[1];
            System.out.println("Access2 : " + access2);
            //Access Token
            String[] access3 = access2.split("\",  \"expires_in\": ");
            String access_code = access3[0]; // access code
            String access4 = access3[1];//
            //Expires in
            String[] access7 = access4.split(",  \"scope\":");
            String expires_in = access7[0]; // Refresh Token
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
            OauthDAO oauthdao = new OauthDAO();
            // Call update method
            oauthdao.updateEmailSettings(refresh_token, access_code, expires_in, scope, token_type);

        } catch (IOException e) {
            //  e.printStackTrace();
        }

//return refresh_token;
    }

}
