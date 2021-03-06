package com.caue.splitter.helper;

/**
 * @author Caue
 * @version 1.0
 * @date 4/03/2017
 */

public class Constants {
    public static class HTTP{

        public static final String API_URL = "http://splitter.mybluemix.net/api/";
//        public static final String API_URL = "http://192.168.2.115:6002/api/";
    }

    public static class KEY {
        public static final String EMAIL_USUARIO = "email";
        public static final String USER_DATA = "UserData";
        public static final String CHECKIN_DATA = "CheckinData";
        public static final String CARDAPIO_DATA = "CardapioData";
        public static final String PRODUTO_DATA = "ProdutoData";
        public static final String PEDIDOS_DATA = "PedidosData";
        public static final String CONTA_DATA = "ContaData";
        public static final String MESA_DATA = "MesaData";
    }

    public static class TIPO_DIVISAO_PEDIDOS {
        public static final int VAZIO = 3;
        public static final int MESA = 1;
        public static final int INDIVIDUAL = 2;
    }

    public static class STATUS_PEDIDO {
        public static final int AGUARDANDO = 0;
        public static final int ENTREGUE = 1;
        public static final int FINALIZADO = 2;
        public static final int CANCELADO = 3;
    }

    public static class CARTAO_EXCEPTION {
        public static final String NomeInvalido = "NomeInvalido";
        public static final String NumeroInvalido = "NumeroInvalido";
        public static final String DataValidadeInvalida = "DataValidadeInvalida";
        public static final String CartaoVencido = "CartaoVencido";
        public static final String CVVInvalido = "CVVInvalido";
    }

}
