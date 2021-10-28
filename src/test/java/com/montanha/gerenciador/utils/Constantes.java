package com.montanha.gerenciador.utils;

import io.restassured.http.ContentType;

public interface Constantes {

    String APP_BASE_URL = "http://localhost/api";
    Integer APP_PORT = 8089;
    String APP_BASE_PATH = "";
    ContentType APP_CONTENT_TYPE = ContentType.JSON;
    Long MAX_TIMEOUT = 10000L;

}
