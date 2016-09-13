package camiloromero.hipotecapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Camilo on 12/09/2016.
 */
public class Configuracion extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.configuracion);
    }

}
