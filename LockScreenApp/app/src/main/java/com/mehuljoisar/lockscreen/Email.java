package com.mehuljoisar.lockscreen;


import android.util.Log;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email extends javax.mail.Authenticator {

    private String _user;
    private String _pass;

    private String _to;
    private String _from;

    private String _port;
    private String _sport;

    private String _host;

    private String _subject;
    private String _body;
    private String _fileName;
    private boolean _auth;

    private boolean _debuggable;

    private Multipart _multipart;

    public Email() {
        _host = "smtp.gmail.com"; // default smtp server
        _port = "465"; // default smtp port
        _sport = "465"; // default socketfactory port
        _user = ""; // username
        _pass = ""; // password
        _from = ""; // email sent from
        _subject = ""; // email subject
        _body = ""; // email body

        _debuggable = false; // debug mode on or off - default off
        _auth = true; // smtp authentication - default on

        _multipart = new MimeMultipart();

// There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public Email(String user, String pass) {
        this();

        _user = user;
        _pass = pass;
    }

    public void setPictureFileName(String fileName)
    {
        _fileName=fileName;
    }

    public boolean send() throws Exception {
        Properties props = _setProperties();

        if(!_user.equals("") && !_pass.equals("") && _to.length() > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {
            try{
                Log.e("JMAAA","JMAAA-1");
                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(_user, _pass);
                    }
                });

                Log.e("JMAAA","JMAAA-2");
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);
                Log.e("JMAAA","JMAAA-2");
                // Set From: header field of the header.
                message.setFrom(new InternetAddress(_from));
                Log.e("JMAAA","JMAAA-3");
                // Set To: header field of the header.
                //message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(_to[0]));
                message.setRecipients(MimeMessage.RecipientType.TO, _to);

                Log.e("JMAAA","JMAAA-4");
                // Set Subject: header field
                message.setSubject(_subject);
                Log.e("JMAAA","JMAAA-5");
                //This mail has 2 part, the BODY and the embedded image

                // put everything together
                message.setText(_body);
                Log.e("JMAAA","JMAAA-16");
                // Send message
                Transport.send(message);
                Log.e("JMA","Correo Enviado Correctamente");
                return true;
            }catch (Exception e){
                Log.e("JMA",e.toString());
                return false;
            }

        }else{
            return false;
        }
    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        _multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(_user, _pass);
    }

    private Properties _setProperties() {
        Log.e("JMAA","Entro a las propiedaddes");
        Properties props = new Properties();
        props.put("mail.smtp.host", _host);
        //props.setProperty("mail.smtp.starttls.enable", "true");
        if(_debuggable) {
            props.put("mail.debug", "true");
        }

        if(_auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", _port);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Log.e("JMAA","Saliendo de las propiedades");


        return props;
    }

    // the getters and setters
    public String getBody() {
        return _body;
    }

    public void setBody(String _body) {
        this._body = _body;
    }

    public void setTo(String toArr) {
        this._to = toArr;
    }

    public void setFrom(String string) {
        this._from = string;
    }

    public void setSubject(String string) {
        this._subject = string;
    }



}
