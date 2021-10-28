package com.montanha.gerenciador.utils;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.SSLConfig;


public class BasePage implements Constantes {

    @BeforeClass
    public static void setup() {

        RestAssured.baseURI = APP_BASE_URL;
        RestAssured.port    = APP_PORT;
        RestAssured.basePath = APP_BASE_PATH;

        SSLSocketFactory customSslFactory = null;
        try {
            customSslFactory = new SSLSocketFactory(
                    SSLContext.getDefault(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RestAssured.config = RestAssured.config().sslConfig(
                SSLConfig.sslConfig().sslSocketFactory(customSslFactory));
        RestAssured.config.getHttpClientConfig().reuseHttpClientInstance();






        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(APP_CONTENT_TYPE);
        RestAssured.requestSpecification = reqBuilder.build();





        ResponseSpecBuilder resBuider = new ResponseSpecBuilder();
        resBuider.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        RestAssured.responseSpecification = resBuider.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

}
