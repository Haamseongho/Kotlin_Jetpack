package com.example.part2.newsapp.certification

import android.content.Context
import com.example.part2.newsapp.R
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.security.cert.X509Certificate

@Singleton
class SelfSigningHelper @Inject constructor(
    context: Context
){
    lateinit var tmf: TrustManagerFactory
    lateinit var sslContext: SSLContext

    init {
        val cf: CertificateFactory
        val ca: Certificate
        val caInput: InputStream

        try {
            cf = CertificateFactory.getInstance("x.509")
            caInput = context.resources.openRawResource(R.raw.news_cert)
            ca = cf.generateCertificate(caInput)
            println("ca = ${(ca as X509Certificate).subjectDN}")

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            with(keyStore){
                load(null, null)
                keyStore.setCertificateEntry("ca", ca)
            }

            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)

            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, java.security.SecureRandom())
            caInput.close()
        } catch(e: Exception){
            e.printStackTrace()
        }
    }
}