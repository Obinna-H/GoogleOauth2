/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dao;

import com.example.model.Oauth;
import com.example.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author Personal PC
 */
public class OauthDAO {
// Save a user to the database

    // Update email_settings where SN = '1'
    public void updateEmailSettings(String refreshToken, String accessCode, String expiresIn, String scope, String tokenType) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // HQL Update Query
            Query query = session.createQuery("UPDATE EmailSettings SET requestCode = :refreshToken, accessCode = :accessCode, expiration = :expiresIn, scope = :scope, tokenType = :tokenType WHERE id = 1");
            query.setParameter("refreshToken", refreshToken);
            query.setParameter("accessCode", accessCode);
            query.setParameter("expiresIn", expiresIn);
            query.setParameter("scope", scope);
            query.setParameter("tokenType", tokenType);

            int updatedRows = query.executeUpdate();
            System.out.println(updatedRows + " rows updated.");

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }
    // Method to get username and recipient

    public String[] getEmailSettings() {
        Transaction transaction = null;
        String FROM_USER_EMAIL = null;
        String TO_USER_EMAIL = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Fetch the first record from the table
            List<Oauth> list = session.createQuery("FROM EmailSettings", Oauth.class).getResultList();
            if (!list.isEmpty()) {
                Oauth emailSettings = list.get(0);
                FROM_USER_EMAIL = emailSettings.getEmail();
                //  TO_USER_EMAIL = emailSettings.getRecipient();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return new String[]{FROM_USER_EMAIL, TO_USER_EMAIL};
    }

    // Get code from email_settings table
    public String getCode() {
        String code = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Fetch the first record's code
            List<Oauth> list = session.createQuery("FROM Oauth", Oauth.class).getResultList();
            if (!list.isEmpty()) {
                code = list.get(0).getCode();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return code;
    }

    // Get refresh token from email_settings table
    public String getRefreshToken() {
        String refreshtoken = null;
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Fetch the first record's code
            List<Oauth> list = session.createQuery("FROM Oauth", Oauth.class).getResultList();
            if (!list.isEmpty()) {
                refreshtoken = list.get(0).getRequest_code();
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return refreshtoken;
    }
}
