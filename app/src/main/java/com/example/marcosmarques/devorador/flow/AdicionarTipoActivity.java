package com.example.marcosmarques.devorador.flow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Tipo;

import java.util.UUID;

import io.realm.Realm;

public class AdicionarTipoActivity extends AppCompatActivity {

    private EditText edtNomeTipo;
    private Button btSalvar;
    private Realm realm;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tipo);
        setTitle("Novo Tipo");

        realm =Realm.getDefaultInstance();

        edtNomeTipo = findViewById(R.id.tipo_add_tipo);
        btSalvar = findViewById(R.id.bt_salvar_add_tipo);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        Tipo tipo = bgRealm.createObject(Tipo.class, UUID.randomUUID().toString());
                        tipo.setNome(edtNomeTipo.getText().toString());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AdicionarTipoActivity.this, "Tipo salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AdicionarTipoActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("MENSAGEM DE ERRO - ",error.getMessage());
                        Toast.makeText(AdicionarTipoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
