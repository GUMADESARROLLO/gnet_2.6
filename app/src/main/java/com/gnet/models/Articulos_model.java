package com.gnet.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gnet.objetos.Articulo;
import com.gnet.utils.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class Articulos_model {
    public static List<Articulo> getArticulos(String basedir, Context context, String Vista) {
        List<Articulo> lista = new ArrayList<>();
        SQLiteDatabase myDataBase = null;
        SQLiteHelper myDbHelper = null;
        try
        {
            //myDbHelper = new SQLiteHelper(basedir, context);
            //myDataBase = myDbHelper.getReadableDatabase();
            //Cursor cursor = myDataBase.rawQuery("SELECT * FROM ARTICULOS WHERE ARTICULO NOT LIKE ? ",new String[] { "%" + "-M"  });
           /* Articulo tmp = new Articulo();
            tmp.setmCodigo("ARTICULO");
            tmp.setmName("DESCRIPCION");
            tmp.setmExistencia("EXISTENCIA");
            tmp.setmUnidad("UNIDAD");
            tmp.setmPrecio("PRECIO");
            tmp.setmPuntos("PUNTOS");
            tmp.setmReglas("REGLAS");
            tmp.setmUnidadMedida("UNIDAD_MEDIDA");
            lista.add(tmp);*/

            /*if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    Articulo tmp = new Articulo();
                    tmp.setmCodigo(cursor.getString(cursor.getColumnIndex("ARTICULO")));
                    tmp.setmName(cursor.getString(cursor.getColumnIndex("DESCRIPCION")));
                    tmp.setmExistencia(cursor.getString(cursor.getColumnIndex("EXISTENCIA")));
                    tmp.setmUnidad(cursor.getString(cursor.getColumnIndex("UNIDAD")));
                    tmp.setmPrecio(cursor.getString(cursor.getColumnIndex("PRECIO")));
                    tmp.setmPuntos(cursor.getString(cursor.getColumnIndex("PUNTOS")));
                    tmp.setmReglas(cursor.getString(cursor.getColumnIndex("REGLAS")));
                    tmp.setmUnidadMedida(cursor.getString(cursor.getColumnIndex("UNIDAD_MEDIDA")));
                    lista.add(tmp);
                    cursor.moveToNext();
                }
            }*/
        }
        catch (Exception e) {
            e.printStackTrace(); }
        finally
        {
            if(myDataBase != null) { myDataBase.close(); }
            if(myDbHelper != null) { myDbHelper.close(); }
        }
        return lista;
    }
}
