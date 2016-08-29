package camiloromero.hipotecapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by camilo on 29/08/16.
 */
public class HipotecaFormulario extends Activity {

    private HipotecaDbAdapter dbAdapter;
    private Cursor cursor;

    private int modo ;

    private long id ;

    private EditText nombre;
    private EditText condiciones;
    private EditText contacto;
    private EditText telefono;
    private EditText email;
    private EditText observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hipoteca_form);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;
        nombre = (EditText) findViewById(R.id.nombre);
        condiciones = (EditText) findViewById(R.id.condiciones);
        contacto = (EditText) findViewById(R.id.contacto);
        telefono = (EditText) findViewById(R.id.telefono);
        email = (EditText) findViewById(R.id.email);
        observaciones = (EditText) findViewById(R.id.observaciones);

        dbAdapter = new HipotecaDbAdapter(this);
        dbAdapter.abrir();

        if (extra.containsKey(HipotecaDbAdapter.C_COLUMNA_ID))
        {
            id = extra.getLong(HipotecaDbAdapter.C_COLUMNA_ID);
            consultar(id);
        }

        establecerModo(extra.getInt(MainActivity.C_MODO));

    }

    private void establecerModo(int m)
    {
        this.modo = m ;

        if (modo == MainActivity.C_VISUALIZAR)
        {
            this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        }
    }

    private void consultar(long id)
    {
        cursor = dbAdapter.getRegistro(id);

        nombre.setText(cursor.getString(cursor.getColumnIndex(HipotecaDbAdapter.C_COLUMNA_NOMBRE)));
        condiciones.setText(cursor.getString(cursor.getColumnIndex(HipotecaDbAdapter.C_COLUMNA_CONDICIONES)));
        contacto.setText(cursor.getString(cursor.getColumnIndex(HipotecaDbAdapter.C_COLUMNA_CONTACTO)));
        telefono.setText(cursor.getString(cursor.getColumnIndex(HipotecaDbAdapter.C_COLUMNA_TELEFONO)));
        email.setText(cursor.getString(cursor.getColumnIndex(HipotecaDbAdapter.C_COLUMNA_EMAIL)));
        observaciones.setText(cursor.getString(cursor.getColumnIndex(HipotecaDbAdapter.C_COLUMNA_OBSERVACIONES)));
    }

    private void setEdicion(boolean opcion)
    {
        nombre.setEnabled(opcion);
        condiciones.setEnabled(opcion);
        contacto.setEnabled(opcion);
        telefono.setEnabled(opcion);
        email.setEnabled(opcion);
        observaciones.setEnabled(opcion);
    }
}
