package com.example.marcosmarques.devorador.flow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Conta;
import com.example.marcosmarques.devorador.bean.ItemConta;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmQuery;

public class NovoItemContaActivity extends AppCompatActivity {

    private EditText edtDescricao;
    private EditText edtValor;
    private EditText qtdParcela;
    private DatePicker datePicker;
    private Button btSalvar;
    private Realm realm;
    private String idConta;
    private ItemConta itemConta;
    private String idItemConta;
    private RealmQuery<Conta> queryConta;
    private RealmQuery<ItemConta> queryItemConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item_conta);
        setTitle("Novo débito");

        realm = Realm.getDefaultInstance();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                idConta = null;
            } else {
                idConta = extras.getString("id");
            }
        }

        edtDescricao = findViewById(R.id.descricao_novo_item);
        edtValor = findViewById(R.id.valor_novo_item);
        qtdParcela = findViewById(R.id.qtd_parcela_novo_item);
        datePicker = findViewById(R.id.data_compra);
        btSalvar = findViewById(R.id.bt_salvar_novo_item);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtDescricao.getText().toString().isEmpty() ||
                        edtValor.getText().toString().isEmpty() ||
                        qtdParcela.getText().toString().isEmpty()) {
                    Toast.makeText(NovoItemContaActivity.this, "Favor preencher todos os dados", Toast.LENGTH_SHORT).show();
                } else {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            idItemConta = UUID.randomUUID().toString();
                            itemConta = bgRealm.createObject(ItemConta.class, idItemConta);
                            itemConta.setQtdParcela(Integer.valueOf(qtdParcela.getText().toString()));
                            itemConta.setDataCompra(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear());
                            itemConta.setDescricao(edtDescricao.getText().toString());
                            itemConta.setValorTotal(Double.valueOf(edtValor.getText().toString()));
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            adicionarItemNaConta();
                            finish();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.d("MENSAGEM DE ERRO - ", error.getMessage());
                            Toast.makeText(NovoItemContaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void adicionarItemNaConta() {
        realm.beginTransaction();

        queryConta = realm.where(Conta.class).equalTo("id", idConta);
        Conta conta = queryConta.findFirst();

        queryItemConta = realm.where(ItemConta.class).equalTo("id", idItemConta);
        ItemConta itemConta = queryItemConta.findFirst();

        conta.getItens().add(itemConta);
        realm.commitTransaction();
        Toast.makeText(NovoItemContaActivity.this, "Débito salvo com sucesso!", Toast.LENGTH_SHORT).show();

    }
}
