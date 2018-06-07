package com.gimeno.enric.infobilbao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BilbaoFeedsSQLProvider extends SQLiteOpenHelper {

    public BilbaoFeedsSQLProvider(Context context) {
        super(context, BilbaoFeedsDB.DB_NAME, null, BilbaoFeedsDB.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if(db.isReadOnly()){
            db = getWritableDatabase();}

        //Tabla con sus campos
        db.execSQL("CREATE TABLE " + BilbaoFeedsDB.Alerts.NOMBRE_TABLA + " (" +
                BilbaoFeedsDB.Alerts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BilbaoFeedsDB.Alerts.CAMPO_GUID + " INTEGER," +
                BilbaoFeedsDB.Alerts.CAMPO_TITLE + " TEXT," +
                BilbaoFeedsDB.Alerts.CAMPO_PUB_DATE + " DATETIME DEFAULT CURRENT_DATE," +
                BilbaoFeedsDB.Alerts.CAMPO_URL_LINK + " TEXT UNIQUE," +
                BilbaoFeedsDB.Alerts.CAMPO_DESCRIPTION + " TEXT" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Cuando haya cambios en la estuctura deberemos incluir el codigo
        // SQL necesario para actualizar la base de datos
        // tendremos en cuenta la version antigua y la nueva para aplicar solo
        // los necesarios
    }
}
