package com.wm.servicemqtt.mqtt.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-04-18 13:15
 */
@Component
public class SslUtil {

    /**
     * 获取 tls 安全套接字工厂   (单向认证，服务器端认证)
     *
     * @param caCrtFile null:使用系统默认的 ca 证书来验证。 非 null:指定使用的 ca 证书来验证服务器的证书。
     * @return tls 套接字工厂
     * @throws Exception
     */
/*    public SSLSocketFactory getSocketFactory(final String caCrtFile) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, KeyManagementException {
        Security.addProvider(new BouncyCastleProvider());
        //===========加载 ca 证书==================================
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        if (null != caCrtFile) {
            // 加载本地指定的 ca 证书
            PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
            X509Certificate caCert = (X509Certificate) reader.readObject();
            reader.close();

            // CA certificate is used to authenticate server
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", caCert);
            // 把ca作为信任的 ca 列表,来验证服务器证书
            tmf.init(caKs);
        } else {
            //使用系统默认的安全证书
            tmf.init((KeyStore) null);
        }

        // ============finally, create SSL socket factory==============
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }*/


    /**
     * InputStream转化为byte[]数组
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     *
     * @param resourcePath   /keystore/server.crt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws KeyManagementException
     */
    public SSLSocketFactory getSocketFactoryFromResourcePath(String resourcePath) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, KeyManagementException {
        Security.addProvider(new BouncyCastleProvider());
        //===========加载 ca 证书==================================
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
        PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(toByteArray(inputStream))));

        X509Certificate caCert = (X509Certificate) reader.readObject();
        reader.close();
        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        // 把ca作为信任的 ca 列表,来验证服务器证书
        tmf.init(caKs);


        // ============finally, create SSL socket factory==============
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }



    /**
     * 双向认证
     */
    public SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile,
                                                    final String password) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
        X509Certificate caCert = (X509Certificate) reader.readObject();
        reader.close();

        // load client certificate
        reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
        X509Certificate cert = (X509Certificate) reader.readObject();
        reader.close();

        // load client private key
        reader = new PEMReader(
                new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))),
                new PasswordFinder() {
                    @Override
                    public char[] getPassword() {
                        return password.toCharArray();
                    }
                }
        );
        KeyPair key = (KeyPair) reader.readObject();
        reader.close();

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);

        // client key and certificates are sent to server so it can authenticate us
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

}
