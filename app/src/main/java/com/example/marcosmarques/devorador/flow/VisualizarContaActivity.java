package com.example.marcosmarques.devorador.flow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Conta;
import com.example.marcosmarques.devorador.bean.ItemConta;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmQuery;

public class VisualizarContaActivity extends AppCompatActivity {

    private TextView texto;
    private Realm realm;
    private String idConta;
    private RealmQuery<Conta> realmQuery;
    private Conta conta;
    private StringBuilder stringBuilder;
    private DecimalFormat df;
    private double total;
    private double totalgeral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_conta);

        realm = Realm.getDefaultInstance();

        texto = findViewById(R.id.texto_visualizar_conta);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                idConta = null;
            } else {
                idConta = extras.getString("id");

                realmQuery = realm.where(Conta.class).equalTo("id", idConta);
                conta = realmQuery.findFirst();

                setTitle(conta.getDescricao());

                stringBuilder = new StringBuilder();

                df = new DecimalFormat("###,##0.00");
                if (conta.getItens().isEmpty()) {
                    stringBuilder.append("Valor - R$ ")
                            .append(String.valueOf(df.format(conta.getTotalConta())))
                            .append("\n")
                            .append("Vencimento - ")
                            .append(conta.getDiaVencimento())
                            .append(" desse mês.");
                    texto.setText(stringBuilder.toString());
                } else {
                    for (ItemConta itemConta : conta.getItens()) {
                        double valorParcela = (itemConta.getValorTotal() / itemConta.getQtdParcela());
                        total = valorParcela + total;
                        totalgeral = itemConta.getValorTotal() + totalgeral;
                        stringBuilder.append(itemConta.getDescricao())
                                .append("\n")
                                .append("Comprado em ")
                                .append(itemConta.getDataCompra())
                                .append("\n")
                                .append("Valor da parcela - R$ ")
                                .append(df.format(valorParcela))
                                .append("\n")
                                .append("Falta pagar ")
                                .append(itemConta.getQtdParcela())
                                .append(" Parcela(s)")
                                .append("\n\n");
                    }
                    stringBuilder.append("Valor total do mês - R$")
                            .append(String.valueOf(df.format(total)))
                            .append("\n")
                            .append("Valor total gasto no cartão de crédito - R$")
                            .append(String.valueOf(df.format(totalgeral)));
                    texto.setText(stringBuilder.toString());
                }

            }
        }
    }
}
