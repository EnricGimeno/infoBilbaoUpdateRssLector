package com.gimeno.enric.infobilbao.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class BilbaoFeedsProvider extends ContentProvider {
    //URI
    public static final String PROVIDER_NAME = "es.infobilbao.alerts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/alerts");


    private static final int ALERTS = 1;
    private static final int ALERT_ID = 2;

    // Creamos el uriMatcher. Esto es el listado de direcciones validas que entiende nuestro proveedor
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "alerts", ALERTS);
        uriMatcher.addURI(PROVIDER_NAME, "alerts/#", ALERT_ID);
    }

    // Declaramos la base de datos. Objeto a traves del cual accedemos a la base de datos
    private SQLiteDatabase feedsDB;

    /*Este método se encarga de inicializar la conexión con la base de datos. Como
    usamos nuestro Helper, si la base de datos no había sido creada, se creará en
    este momento (y si tiene que ser actualizada también lo hará entonces).*/
    @Override
    public boolean onCreate() {
        Context context = getContext();
        BilbaoFeedsSQLProvider dbHelper = new BilbaoFeedsSQLProvider(context);
        feedsDB = dbHelper.getWritableDatabase();

        return (feedsDB == null) ? false:true;
    }


    // Despues de cada operación notificamos a los objetos que nos estan escuchando para que
    // se actualicen
    @Nullable
    @Override
    public  Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(BilbaoFeedsDB.Alerts.NOMBRE_TABLA);

        if (uriMatcher.match(uri) == ALERT_ID) {
            sqlBuilder.appendWhere(BilbaoFeedsDB.Alerts._ID + " = "
                    + uri.getPathSegments().get(1));
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = BilbaoFeedsDB.Alerts.DEFAULT_SORT_ORDER;

        }
        Cursor c = sqlBuilder.query(feedsDB, projection, selection, selectionArgs, null, null, sortOrder);
        // Registramos los cambios para que se enteren nuestros observers
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /*El método getType() devuelve el MimeType que se aplicará a los objetos de res-
    puesta. En nuestro caso dos, uno para los listados de registros y otro para un
    solo registro.*/

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case ALERTS:
                return "vnd.android.cursor.dir/vnd.infobilbao.alerts";

            case ALERT_ID:
                return "vnd.android.cursor.item/vnd.infobilbao.alerts";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);

        }
    }

    //Los metodos que quedan son los metodos de acceso a la base de datos
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Añade una nueva alerta
        long rowID = feedsDB.replace(BilbaoFeedsDB.Alerts.NOMBRE_TABLA, " ", values);
        // si todo ha ido ok devolvemos su uri
        if (rowID > 0) {

            //Añadimos el rowID a la uri
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);

            getContext().getContentResolver().notifyChange(_uri, null);
            getContext().getContentResolver().notifyChange(CONTENT_URI, null);

            return _uri;
        }
        throw new android.database.SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        int count = 0;
//        switch (uriMatcher.match(uri)){
//            case ALERTS:
//                count = feedsDB.delete(BilbaoFeedsDB.Posts.NOMBRE_TABLA, selection, selectionArgs);
//
//                break;
//
//            case ALERT_ID:
//                String id = uri.getPathSegments().get(1);
//                count = feedsDB.delete(BilbaoFeedsDB.Posts.NOMBRE_TABLA, ALERT_ID + " = " + id
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : " "), selectionArgs);
//
//                break;
//
//            default:
//                throw new IllegalArgumentException("Unknown URI " +uri);
//        }
//        getContext().getContentResolver().notifyChange(uri,null);
//        return count;
        // No realizaremos nada. Ya que no borraremos datos
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // No actualizaremos datos. Idem que caso delete
        return 0;
//        int count = 0;
//        switch (uriMatcher.match(uri)){
//            case ALERTS:
//                count = feedsDB.update(BilbaoFeedsDB.Posts.NOMBRE_TABLA, values, selection, selectionArgs);
//                break;
//
//            case ALERT_ID:
//                count = feedsDB.update(BilbaoFeedsDB.Posts.NOMBRE_TABLA, values,
//                        ALERT_ID
//                                + " = "
//                                + uri.getPathSegments().get(1)
//                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection
//                                + ')' : " "), selectionArgs);
//
//                break;
//
//            default:
//                throw new IllegalArgumentException("Unknown URI " +uri);
//        }
//        getContext().getContentResolver().notifyChange(uri,null);
//        return count;
    }

}
