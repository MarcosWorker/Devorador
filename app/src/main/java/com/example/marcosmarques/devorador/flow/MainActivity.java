package com.example.marcosmarques.devorador.flow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Debito;
import com.example.marcosmarques.devorador.bean.Tipo;
import com.example.marcosmarques.devorador.control.AdapterMain;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Realm realm;
    private AdapterMain adapterMain;
    private RecyclerView recyclerView;
    private RealmResults<Tipo> tiposRealm;
    private RealmResults<Debito> debitosRealm;
    private RealmQuery<Tipo> queryTipo;
    private RealmQuery<Debito> queryDebito;
    private AlertDialog.Builder builderSingle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.item_menu_main_novo_tipo:
                intent = new Intent(MainActivity.this, AdicionarTipoActivity.class);
                startActivity(intent);
                return true;
            case R.id.item_menu_main_novo_debito:
                intent = new Intent(MainActivity.this, AdicionarDebitoActivity.class);
                intent.putExtra("tela", "novo");
                startActivity(intent);
                return true;
            case R.id.item_menu_main_pagar:

                queryDebito = realm.where(Debito.class);
                debitosRealm = queryDebito.findAll();

                if (debitosRealm.size() > 0) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                    builderSingle.setTitle("O que você já pagou?");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item);

                    queryTipo = realm.where(Tipo.class);
                    tiposRealm = queryTipo.findAll();

                    for (Tipo tipo : tiposRealm) {
                        arrayAdapter.add(tipo.getNome());
                    }

                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("Confirmo o pagamento desse débito");
                            builderInner.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    boolean existeDebito = false;
                                    boolean debitoAtualizado = false;

                                    for (final Debito debito : debitosRealm) {
                                        if (debito.getTipo().equals(strName)) {
                                            existeDebito = true;
                                            final int parcelasPagas = debito.getQtdParcelaQuitada() + 1;

                                            if (parcelasPagas == Integer.valueOf(debito.getQtdParcela())) {
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        try {
                                                            debito.deleteFromRealm();
                                                        } catch (RealmError ex) {
                                                            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                                        } finally {
                                                            Toast.makeText(MainActivity.this, strName + " já está quitado", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                realm.beginTransaction();
                                                debito.setQtdParcelaQuitada(parcelasPagas);
                                                realm.commitTransaction();
                                                debitoAtualizado = true;
                                            }
                                        }
                                    }

                                    if (existeDebito == false) {
                                        Toast.makeText(MainActivity.this, "Não existe débito para esse tipo", Toast.LENGTH_SHORT).show();
                                    }

                                    if (debitoAtualizado) {
                                        Toast.makeText(MainActivity.this, "Débito Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                    }

                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();
                        }
                    });
                    builderSingle.show();
                } else {
                    Toast.makeText(this, "Não existem débitos para quitar", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.item_menu_main_deletar_tipo:
                AlertDialog.Builder builderSingleDel = new AlertDialog.Builder(MainActivity.this);
                builderSingleDel.setTitle("Qual tipo você quer deletar ?");

                final ArrayAdapter<String> arrayAdapterDel = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item);

                queryTipo = realm.where(Tipo.class);
                tiposRealm = queryTipo.findAll();

                for (Tipo tipo : tiposRealm) {
                    arrayAdapterDel.add(tipo.getNome());
                }

                builderSingleDel.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingleDel.setAdapter(arrayAdapterDel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = arrayAdapterDel.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Tem certeza que quer excluir esse tipo?");
                        builderInner.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                queryTipo = realm.where(Tipo.class).equalTo("nome", strName);
                                final Tipo tipo = queryTipo.findFirst();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try {
                                            tipo.deleteFromRealm();
                                        } catch (RealmError ex) {
                                            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        } finally {
                                            Toast.makeText(MainActivity.this, "Tipo excluído", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingleDel.show();
                return true;
            case R.id.item_menu_main_sair:
                onDestroy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void montarLista() {
        adapterMain = new AdapterMain(realm.where(Debito.class).findAll());
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
