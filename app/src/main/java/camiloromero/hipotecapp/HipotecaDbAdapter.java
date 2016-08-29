package camiloromero.hipotecapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Camilo Romero on 29/08/2016.
 */
public class HipotecaDbAdapter {
    public static final String C_TABLA = "HIPOTECA" ;
    public static final String C_COLUMNA_ID   = "_id";
    public static final String C_COLUMNA_NOMBRE = "hip_nombre";
    public static final String C_COLUMNA_CONDICIONES = "hip_condiciones";
    public static final String C_COLUMNA_CONTACTO = "hip_contacto";
    public static final String C_COLUMNA_EMAIL = "hip_email";
    public static final String C_COLUMNA_TELEFONO = "hip_telefono";
    public static final String C_COLUMNA_OBSERVACIONES = "hip_observaciones";

    private Context contexto;
    private HipotecaDbHelper dbHelper;
    private SQLiteDatabase db;

    private String[] columnas = new String[]{ C_COLUMNA_ID, C_COLUMNA_NOMBRE, C_COLUMNA_CONDICIONES, C_COLUMNA_CONTACTO, C_COLUMNA_EMAIL, C_COLUMNA_TELEFONO, C_COLUMNA_OBSERVACIONES} ;

    public HipotecaDbAdapter(Context context)
    {
        this.contexto = context;
    }

    public HipotecaDbAdapter abrir() throws SQLException
    {
        dbHelper = new HipotecaDbHelper(contexto);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar()
    {
        dbHelper.close();
    }

    public Cursor getCursor() throws SQLException
    {
        Cursor c = db.query( true, C_TABLA, columnas, null, null, null, null, null, null);

        return c;
    }
}
