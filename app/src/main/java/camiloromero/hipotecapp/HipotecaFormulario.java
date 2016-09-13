package camiloromero.hipotecapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class HipotecaFormulario extends AppCompatActivity {

    private SituacionDbAdapter dbAdapterSituacion ;
    private SituacionSpinnerAdapter situacionSpinnerAdapter ;

    //
    // Modo del formulario
    //
    private int modo ;

    //
    // Identificador del registro que se edita cuando la opción es MODIFICAR
    //
    private long id ;
    private Hipoteca hipoteca = new Hipoteca(this);

    //
    // Elementos de la vista
    //
    private EditText nombre;
    private EditText condiciones;
    private EditText contacto;
    private EditText telefono;
    private EditText email;
    private EditText observaciones;
    private CheckBox pasivo ;
    private Spinner situacion ;

    private Button boton_guardar;
    private Button boton_cancelar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hipoteca_form);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        //
        // Obtenemos los elementos de la vista
        //
        nombre = (EditText) findViewById(R.id.nombre);
        condiciones = (EditText) findViewById(R.id.condiciones);
        contacto = (EditText) findViewById(R.id.contacto);
        telefono = (EditText) findViewById(R.id.telefono);
        email = (EditText) findViewById(R.id.email);
        observaciones = (EditText) findViewById(R.id.observaciones);
        pasivo = (CheckBox) findViewById(R.id.pasivo);
        situacion = (Spinner) findViewById(R.id.situacion);

        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        //
        // Creamos el adaptador del spinner de situaciones y lo asociamos
        //
        situacionSpinnerAdapter = new SituacionSpinnerAdapter(this, Situacion.getAll(this, null));
        situacion.setAdapter(situacionSpinnerAdapter);

        //
        // Obtenemos el identificador del registro si viene indicado
        //
        if (extra.containsKey(HipotecaDbAdapter.C_COLUMNA_ID))
        {
            id = extra.getLong(HipotecaDbAdapter.C_COLUMNA_ID);
            consultar(id);
        }

        //
        // Establecemos el modo del formulario
        //
        establecerModo(extra.getInt(HipotecaActivity.C_MODO));

        //
        // Definimos las acciones para los dos botones
        //
        boton_guardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                guardar();
            }
        });

        boton_cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                cancelar();
            }
        });

    }

    private void establecerModo(int m)
    {
        this.modo = m ;

        if (modo == HipotecaActivity.C_VISUALIZAR)
        {
            this.setTitle(nombre.getText().toString());
            this.setEdicion(false);
        }
        else if (modo == HipotecaActivity.C_CREAR)
        {
            this.setTitle(R.string.hipoteca_crear_titulo);
            this.setEdicion(true);
        }
        else if (modo == HipotecaActivity.C_EDITAR)
        {
            this.setTitle(R.string.hipoteca_editar_titulo);
            this.setEdicion(true);
        }
    }

    private void consultar(long id)
    {
        //
        // Consultamos la hipoteca por el identificador
        //
        hipoteca = Hipoteca.find(this, id);

        nombre.setText(hipoteca.getNombre());
        condiciones.setText(hipoteca.getCondiciones());
        contacto.setText(hipoteca.getContacto());
        telefono.setText(hipoteca.getTelefono());
        email.setText(hipoteca.getEmail());
        observaciones.setText(hipoteca.getObservaciones());
        pasivo.setChecked(hipoteca.isPasivo());
        situacion.setSelection(situacionSpinnerAdapter.getPositionById(hipoteca.getSituacionId()));

    }

    private void setEdicion(boolean opcion)
    {
        nombre.setEnabled(opcion);
        condiciones.setEnabled(opcion);
        contacto.setEnabled(opcion);
        telefono.setEnabled(opcion);
        email.setEnabled(opcion);
        observaciones.setEnabled(opcion);
        pasivo.setEnabled(opcion);
        situacion.setEnabled(opcion);

        // Controlamos visibilidad de botonera
        LinearLayout v = (LinearLayout) findViewById(R.id.botonera);

        if (opcion)
            v.setVisibility(View.VISIBLE);

        else
            v.setVisibility(View.GONE);
    }

    private void guardar()
    {
        hipoteca.setNombre(nombre.getText().toString());
        hipoteca.setCondiciones(condiciones.getText().toString());
        hipoteca.setContacto(contacto.getText().toString());
        hipoteca.setTelefono(telefono.getText().toString());
        hipoteca.setEmail(email.getText().toString());
        hipoteca.setObservaciones(observaciones.getText().toString());
        hipoteca.setPasivo(pasivo.isChecked());
        hipoteca.setSituacionId(situacion.getSelectedItemId());

        hipoteca.save();

        if (modo == HipotecaActivity.C_CREAR)
        {
            Toast.makeText(HipotecaFormulario.this, R.string.hipoteca_crear_confirmacion, Toast.LENGTH_SHORT).show();
        }
        else if (modo == HipotecaActivity.C_EDITAR)
        {
            Toast.makeText(HipotecaFormulario.this, R.string.hipoteca_editar_confirmacion, Toast.LENGTH_SHORT).show();
        }

        //
        // Devolvemos el control
        //
        setResult(RESULT_OK);
        finish();
    }

    private void cancelar()
    {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();

        if (modo == HipotecaActivity.C_VISUALIZAR)
            getMenuInflater().inflate(R.menu.hipoteca_formulario_ver, menu);

        else
            getMenuInflater().inflate(R.menu.hipoteca_formulario_editar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_eliminar:
                borrar(id);
                return true;

            case R.id.menu_cancelar:
                cancelar();
                return true;

            case R.id.menu_guardar:
                guardar();
                return true;

            case R.id.menu_editar:
                establecerModo(HipotecaActivity.C_EDITAR);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void borrar(final long id)
    {
		/*
		 * Borramos el registro con confirmación
		 */
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);

        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle(getResources().getString(R.string.hipoteca_eliminar_titulo));
        dialogEliminar.setMessage(getResources().getString(R.string.hipoteca_eliminar_mensaje));
        dialogEliminar.setCancelable(false);

        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int boton) {
                hipoteca.delete();
                Toast.makeText(HipotecaFormulario.this, R.string.hipoteca_eliminar_confirmacion, Toast.LENGTH_SHORT).show();
				/*
				 * Devolvemos el control
				 */
                setResult(RESULT_OK);
                finish();
            }
        });

        dialogEliminar.setNegativeButton(android.R.string.no, null);

        dialogEliminar.show();

    }

}