package com.example.marcosmarques.devorador.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Conta;
import com.example.marcosmarques.devorador.control.AdapterMain;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Realm realm;
    private AdapterMain adapterMain;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Contas a Pagar");
        realm = Realm.getDefaultInstance();
        recyclerView = findViewById(R.id.list_main);
        montarLista();
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
            case R.id.item_menu_main_nova_conta:
                intent = new Intent(MainActivity.this, NovaContaActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void montarLista() {
        adapterMain = new AdapterMain(realm.where(Conta.class).findAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterMain);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
