package com.example.marcosmarques.devorador.flow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Conta;

import java.util.UUID;

import io.realm.Realm;

public class NovaContaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerCartao;
    private EditText edtDescricao;
    private EditText edtDiavencimento;
    private Button btSalvar;
    private ArrayAdapter<CharSequence> adapter;
    private Realm realm;
    private boolean cartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_conta);

        realm = Realm.getDefaultInstance();

        spinnerCartao = findViewById(R.id.sp_cartao);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_sim_nao, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCartao.setAdapter(adapter);
        spinnerCartao.setOnItemSelectedListener(this);

        edtDescricao = findViewById(R.id.descricao_nova_conta);
        edtDiavencimento = findViewById(R.id.dia_vencimento_nova_conta);
        btSalvar = findViewById(R.id.bt_salvar_nova_conta);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtDescricao.getText().toString().isEmpty()) {
                    Toast.makeText(NovaContaActivity.this, "Favor preencher a descrição", Toast.LENGTH_SHORT).show();
                } else if (edtDiavencimento.getText().toString().isEmpty()) {
                    Toast.makeText(NovaContaActivity.this, "Favor preencher o dia do vencimento", Toast.LENGTH_SHORT).show();
                } else {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            Conta conta = bgRealm.createObject(Conta.class, UUID.randomUUID().toString());
                            conta.setCartao(cartao);
                            conta.setDescricao(edtDescricao.getText().toString());
                            conta.setDiaVencimento(Integer.valueOf(edtDiavencimento.getText().toString()));
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(NovaContaActivity.this, "Conta salva com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Log.d("MENSAGEM DE ERRO - ", error.getMessage());
                            Toast.makeText(NovaContaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item = parent.getItemAtPosition(position).toString();
        if (item.equals("Sim")) {
            this.cartao = true;
        } else {
            this.cartao = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.cartao = false;
    }
}
