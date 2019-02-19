/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbpmemail;

/**
 *
 * @author faya
 */
public class EmailService {
    public Email verify(Email email){
    System.out.println("1: Email :" + email);
    email.setBody(email.getBody()+"[Body verified]");
    email.setSubject(email.getSubject()+"[Subject verified]");
    System.out.println("2: Email");
    return email;
    }
}
