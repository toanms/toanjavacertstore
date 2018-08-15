package com.certstore.demo;

import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.time.*;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class GetCert {

public static void main(String[] args) throws Exception {

// TODO Auto-generated method stub

// port.http is where WebApp will listen for your Java Process

String listenPort = System.getProperty("port.http");

System.out.println("listenPort is " + listenPort);

int port = Integer.parseInt(listenPort);

HttpServer azServer = HttpServer.create(new InetSocketAddress(port), 0);

azServer.createContext("/", new AzHandler());

azServer.setExecutor(null);

azServer.start();

}

static class AzHandler implements HttpHandler {

public void handle(HttpExchange httpExchange) throws IOException {


//Load certificates from certstore

try{
    KeyStore keyStore = KeyStore.getInstance("Windows-MY");
    keyStore.load(null, null);  // Load keystore
	Enumeration<String> aliases = keyStore.aliases();
	String keyAlias = "";
	//Listing available aliases
    System.out.println("LogTime: " + LocalDateTime.now() + " Listing aliases " + aliases);
    while (aliases.hasMoreElements())
    {
        keyAlias = (String) aliases.nextElement();

        System.out.println("LogTime: " + LocalDateTime.now() + " Key alias = " + keyAlias + " : Is a key entry = " + keyStore.isKeyEntry(keyAlias));
		//Output with the cert chain
		Certificate[] chain = keyStore.getCertificateChain(keyAlias);
     
		for(Certificate certChain : chain){
		System.out.println("LogTime: " + LocalDateTime.now() + " Showing certificate chain for " + keyAlias);
        System.out.println(certChain);
		
		//Load just the certificate without the whole chain.
		Certificate cert = keyStore.getCertificate(keyAlias);
		System.out.println("LogTime: " + LocalDateTime.now() + " Showing just the certificate for " + keyAlias);
		System.out.println(cert);
    }
    }
} catch (Exception ex){
    ex.printStackTrace();
}

						   

String response = "Logging all certificates in certstore.  Check logs in KUDU in D:/home/site/wwwroot";

httpExchange.sendResponseHeaders(200, response.length());

OutputStream os = httpExchange.getResponseBody();

os.write(response.getBytes());

os.close();

}

}

}
