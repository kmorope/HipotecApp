package camiloromero.hipotecapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private HipotecaDbAdapter dbAdapter;
    private Cursor cursor;
    private HipotecaCursorAdapter hipotecaAdapter ;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(android.R.id.list);

        dbAdapter = new HipotecaDbAdapter(this);
        dbAdapter.abrir();

        consultar();
    }

    private void consultar()
    {
        cursor = dbAdapter.getCursor();
        startManagingCursor(cursor);
        hipotecaAdapter = new HipotecaCursorAdapter(this, cursor);
        lista.setAdapter(hipotecaAdapter);
    }

}
