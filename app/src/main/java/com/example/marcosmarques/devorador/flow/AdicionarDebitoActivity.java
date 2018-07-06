package com.example.marcosmarques.devorador.flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Debito;
import com.example.marcosmarques.devorador.bean.Tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AdicionarDebitoActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private EditText edtDescricao;
    private EditText edtValorTotal;
    private EditText edtQtdParcela;
    private EditText edtDiaVencimanto;
    private TextView tvValorParcela;
    private Spinner spinnerTipo;
    private Button btSalvar;
    private String tela;
    private ArrayAdapter arrayAdapter;
    private Realm realm;
    private List<String> tiposSpinner;
    private RealmResults<Tipo> tiposRealm;
    private RealmQuery<Tipo> query;
    private String tipo;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_debito);
        realm = Realm.getDefaultInstance();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                tela = extras.getString("tela");
            }else {
                tela = getString(R.string.app_name);
            }
        }else {
            tela = getString(R.string.app_name);
        }
        setTitle(tela);

        edtDescricao = findViewById(R.id.descricao_add_debito);
        edtDiaVencimanto = findViewById(R.id.dia_vencimento_add_debito);
        edtQtdParcela = findViewById(R.id.qtd_parcela_add_debito);
        edtValorTotal = findViewById(R.id.valor_total_add_debito);

        spinnerTipo = findViewById(R.id.tipo_add_debito);
        spinnerTipo.setOnItemSelectedListener(this);

        tiposSpinner = new ArrayList<>();
        query = realm.where(Tipo.class);
        tiposRealm = query.findAll();

        for(Tipo tipo:tiposRealm){
            tiposSpinner.add(tipo.getNome());
        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tiposSpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerTipo.setAdapter(arrayAdapter);

        btSalvar = findViewById(R.id.bt_salvar_add_debito);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        Debito debito = bgRealm.createObject(Debito.class, UUID.randomUUID().toString());
                        debito.setDescricao(edtDescricao.getText().toString());
                        debito.setDiaVencimento(edtDiaVencimanto.getText().toString());
                        debito.setQtdParcela(edtQtdParcela.getText().toString());
                        debito.setQtdParcelaQuitada(0);
                        debito.setStatus("pendente");
                        debito.setTipo(tipo);
                        debito.setValorParcela(Double.valueOf(tvValorParcela.getText().toString()));
                        debito.setValorTotal(Double.valueOf(edtValorTotal.getText().toString()));
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AdicionarDebitoActivity.this, "Débito salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AdicionarDebitoActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("MENSAGEM DE ERRO - ",error.getMessage());
                        Toast.makeText(AdicionarDebitoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        tipo = spinnerTipo.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
