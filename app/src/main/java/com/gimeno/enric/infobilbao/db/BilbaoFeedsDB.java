package com.gimeno.enric.infobilbao.db;


import android.net.Uri;
import android.provider.BaseColumns;

public class BilbaoFeedsDB {
    /*
     * Espacio de nombres (se debe usar el mismo para definir el content provider
     * en el Manifest
     */
    public static final String AUTHORITY = "es.infobilbao.alerts";

    //Nombre de la base de datos
    public static final String DB_NAME = "BilbaoAlertsRSS.db";
    //Version de la base de datos
    public  static final int DB_VERSION = 1;

    // Esta clase no debe ser instanciada la pasamos a privada
    private BilbaoFeedsDB(){

    }
    // Definicion de la tabla alerts
    public static  final class Alerts implements BaseColumns {
        // Esta clase no debe ser instanciada la pasamos a privada
        private Alerts(){}

        /**
         *  content:// estilo URL para esta tabla
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/alerts");

        // Ponemos la tabla en orden por defecto
        public static  final String DEFAULT_SORT_ORDER = "_ID DESC";
        // Abstraccion de los nombres de campos y tabla a constantes
        // para facilitar cambios en la estructura interna de la BD

        public static final String NOMBRE_TABLA = "bilbao_feeds";

        public static final String CAMPO_GUID = "guid";
        public static final String CAMPO_TITLE = "title";
        public static final String CAMPO_PUB_DATE = "pubdate";
        public static final String CAMPO_URL_LINK = "link";
        public static final String CAMPO_DESCRIPTION = "description";
        //public static final String CAMPO_CLASE_FEED = "clase_de_rss";
    }

}
