package com.gimeno.enric.infobilbao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class GetAlertsFromSettings {
    // Contexto
    Context mContext;

    // Definicion de la constante query y cursor
    Cursor cursor_alerts;
    ArrayList query = new ArrayList();

    private static Uri uri = Uri.parse("content://es.infobilbao.alerts/alerts/*");

    public GetAlertsFromSettings(Context mContext) {
        this.mContext = mContext;
    }

    // Obtenemos las alertas definidas por el usuario en la interfaz
    // y mostramos los datos a partir de esta
    public Cursor getAlerts(){
        SharedPreferences pref_alerts = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        //String alerts = pref_alerts.getString("pref_key_alerts", "N_MA");
        Set<String> alerts = pref_alerts.getStringSet("avisos_de_temas_varios", new Set<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends String> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }
        });

        // Add the alets into an ArrayList
        for (String alert: alerts) {
            query.add(alert);
        }

        // Create the query
        cursor_alerts = mContext.getContentResolver().query(uri, null, null, query, null);


        return cursor_alerts;
    }
}
