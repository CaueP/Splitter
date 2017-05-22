package com.caue.splitter;

import com.caue.splitter.model.Cartao;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by CaueGarciaPolimanti on 5/21/2017.
 */

public class CartaoUnitTest {
    Cartao cartao = null;

    @Test
    public void cartao_nome_invalido() throws Exception {
        try {
            cartao = new Cartao("", 123456789, 10, 2018, 123);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "NomeInvalido");
        }
    }
    @Test
    public void cartao_numero_invalido() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 0, 10, 2018, 123);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "NumeroInvalido");
        }
    }
    @Test
    public void cartao_cvv_invalido() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 123456789, 10, 2018, 0);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "CVVInvalido");
        }
    }

    @Test
    public void cartao_data_ano_ultrapassado() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 123456789, 6, 2016, 123);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "CartaoVencido");
        }
    }

    @Test
    public void cartao_data_mes_ultrapassado() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 123456789, 4, 2017, 123);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "CartaoVencido");
        }
    }

    @Test
    public void cartao_data_mes_invalido() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 123456789, 0, 2017, 123);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "CartaoVencido");
        }
    }

    @Test
    public void cartao_data_mes_invalido2() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 123456789, 13, 2017, 123);
        } catch (Exception exc) {
            assertEquals(exc.getMessage(), "DataValidadeInvalida");
        }
    }

    @Test
    public void cartao_valido() throws Exception {
        try {
            cartao = new Cartao("Caue Garcia", 123456789, 10, 2030, 123);
            assertNotNull("Cartao not null", cartao);
        } catch (Exception exc) {
            fail();
        }
    }
}
