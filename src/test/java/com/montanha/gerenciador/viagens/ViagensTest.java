package com.montanha.gerenciador.viagens;
import com.montanha.gerenciador.entities.Usuario;
import com.montanha.gerenciador.entities.Viagem;
import com.montanha.gerenciador.enums.PerfilEnum;
import com.montanha.gerenciador.utils.BasePage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
//import org.h2.util.New;

import org.junit.FixMethodOrder;
import org.junit.Test;


import java.util.Collections;
import java.util.Date;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ViagensTest  extends BasePage {
    int   id;
    int   idNew;
    Usuario usuario =  new Usuario();
    Viagem viagem = new Viagem();
    Date data = new Date();


    @Test
    public void test_01_DadoUmAdminQuandoCadastroViagensEntaoStatusCode201(){


        usuario.setEmail("admin@email.com");
        usuario.setSenha("654321");



        String token = given()

        .body(usuario)
                .when()
                .post("/v1/auth")
                .then()
                .extract().path("data.token")
                ;

        viagem.setAcompanhante("Teste");
        viagem.setDataPartida(data);
        viagem.setDataRetorno(data);
        viagem.setLocalDeDestino("Salvador");
        viagem.setRegiao("Nordeste");

   Response response =     given()
                .header("Authorization",token)
           .body(viagem)
                .when()
                .post("/v1/viagens");
           response.then()
                .log().all()
                .body("data.acompanhante",is("Teste"))
                .body("data.regiao",is("Nordeste"))
                .statusCode(201);

        idNew =	response.jsonPath().getInt("data.id");
        System.out.println("ID : "+idNew);





    }

    @Test
    public void test_10_CadastrarViagemSemDataRetorno(){


        usuario.setEmail("admin@email.com");
        usuario.setSenha("654321");



        String token = given()

                .body(usuario)
                .when()
                .post("/v1/auth")
                .then()
                .log().all()
                .extract().path("data.token")
                ;

        viagem.setAcompanhante("teste");
        //viagem.setDataPartida(data);
        viagem.setDataRetorno(data);
        viagem.setLocalDeDestino("Manaus");
        viagem.setRegiao("Manaus");

            given()
                .header("Authorization",token)
                .body(viagem)
            .when()
                .post("/v1/viagens")
                    .then()
                .log().all()
                    .body("errors.defaultMessage",hasItem("Data da Partida é uma informação obrigatória"))
               // .assertThat()
               // .statusCode(201);

;

    }

    @Test
    public void test_02_testDadoUmAdminQuandoPesquistaTodasEntaoTodasSeramExibidas(){



        usuario.setEmail("usuario@email.com");
        usuario.setSenha("123456");

        String token = given()
                .body(usuario)
                .when()
                    .post("/v1/auth")
                .then()
                    .log().all()
                    .extract().path("data.token")
                ;
        Response response =
                 given()
                    .header("Authorization",token)

                .when()
                    .get("/v1/viagens");

        response.then()
                .statusCode(200)
                .log().all()
                .body("data.id[0]", is(1))
        ;

       id =	response.jsonPath().getInt("data.id[0]");
        System.out.println("ID : "+id);






    }

    @Test
    public void test_07_BuscarViagensPorID(){



        usuario.setEmail("usuario@email.com");
        usuario.setSenha("123456");

        String token = given()
                .body(usuario)
                .when()
                    .post("/v1/auth")
                .then()
                    .extract().path("data.token");

                 given()
                    .header("Authorization",token)
                    .when()
                    .get("/v1/viagens/2")
                .then()
                     .log().all()
                     .assertThat()
                      .statusCode(200);





    }


    @Test
    public void test_03_DadoUmAdminQuandoPesquistaPorRegiaoEntaoViagensPorUnidadesSeraoExibidas(){



        usuario.setEmail("usuario@email.com");
        usuario.setSenha("123456");

        String token = given()
                .body(usuario)
                .when()
                .post("/v1/auth")
                .then()
                .log().all()
                .extract().path("data.token");

        given()
                .header("Authorization",token)
                .queryParam("regiao","Sul")
                .when()
                .get("/v1/viagens")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);





    }




    @Test
    public void test_09_DeletaViagens(){


        test_01_DadoUmAdminQuandoCadastroViagensEntaoStatusCode201();

        usuario.setEmail("admin@email.com");
        usuario.setSenha("654321");

        String token = given()
                .body(usuario)
                .when()
                    .post("/v1/auth")
                .then()
                     .log().all()
                    .extract().path("data.token");

        given()
                .header("Authorization",token)
                .when()
                    .delete("/v1/viagens/"+idNew)
                .then()
                    .log().all()
                    .assertThat()
                    .statusCode(204);

    }



    @Test
    public void test_06_DadoUmAdminQuandoAtualizarViagensEntaoStatusCode201(){

        test_01_DadoUmAdminQuandoCadastroViagensEntaoStatusCode201();

        usuario.setEmail("admin@email.com");
        usuario.setSenha("654321");

        String token = given()
                .body(usuario)
                .when()
                    .post("/v1/auth")
                .then()
                    .log().all()
                    .extract().path("data.token")
                ;
        viagem.setAcompanhante("teste");
        viagem.setDataPartida(data);
        viagem.setDataRetorno(data);
        viagem.setLocalDeDestino("Manaus");
        viagem.setRegiao("Manaus");

        given()

                .header("Authorization",token)
                .body(viagem)
                .when()
                   .put("/v1/viagens/"+idNew)
                .then()
                    .log().all()
                    .assertThat()
                    .statusCode(204);





    }




    @Test
    public void testSatus(){


        given()
                .when()
                .get("/v1/status")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
}
