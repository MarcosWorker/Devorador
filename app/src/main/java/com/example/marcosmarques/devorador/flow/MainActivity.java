package com.example.marcosmarques.devorador.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_menu_main_novo_tipo:
                intent = new Intent(MainActivity.this, AdicionarTipoActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_menu_main_novo_debito:
                intent = new Intent(MainActivity.this, AdicionarDebitoActivity.class);
                intent.putExtra("tela","novo");
                startActivity(intent);
                return true;
            case R.id.item_menu_main_sair:
                onDestroy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }
}
