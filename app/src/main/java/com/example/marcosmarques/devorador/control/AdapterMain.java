package com.example.marcosmarques.devorador.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Conta;
import com.example.marcosmarques.devorador.bean.ItemConta;
import com.example.marcosmarques.devorador.flow.NovoItemContaActivity;
import com.example.marcosmarques.devorador.flow.VisualizarContaActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;

public class AdapterMain extends RealmRecyclerViewAdapter<Conta, AdapterMain.ViewHolder> {

    private Realm realm;
    private AlertDialog.Builder builder;
    private Intent intent;

    public AdapterMain(RealmResults<Conta> data) {
        super(data, true);
        // Only set this if the model class has a primary key that is also a integer or long.
        // In that case, {@code getItemId(int)} must also be overridden to return the key.
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#hasStableIds()
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#getItemId(int)
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final Conta conta = getItem(position);
        DecimalFormat df = new DecimalFormat("###,##0.00");
        GregorianCalendar calendar = new GregorianCalendar();
        viewHolder.tvDescricao.setText(conta.getDescricao());

        // calculo do valor total
        double total = 0.0;
        double totalParcela = 0.0;
        if (conta.isCartao()) {
            if (conta.getItens().size() > 0) {
                for (ItemConta itemConta : conta.getItens()) {
                    totalParcela = itemConta.getValorTotal() / itemConta.getQtdParcela();
                    total = totalParcela + total;
                }
                viewHolder.tvValor.setText(String.valueOf(df.format(total)));
            } else {
                viewHolder.tvValor.setText(String.valueOf(df.format(total)));
            }
        } else {
            total = conta.getTotalConta();
            viewHolder.tvValor.setText(String.valueOf(df.format(total)));
        }


        viewHolder.tvVencimento.setText(conta.getDiaVencimento() + "/"
                + (Integer.valueOf(calendar.get(Calendar.MONTH)) + 1) + "/"
                + calendar.get(GregorianCalendar.YEAR));
        //Regra de visualização
        if (conta.isCartao()) {
            viewHolder.btAdd.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btAdd.setVisibility(View.INVISIBLE);
        }
        //visualizar conta

        viewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), VisualizarContaActivity.class);
                intent.putExtra("id", conta.getId());
                view.getContext().startActivity(intent);
            }
        });

        //adicionar debito na conta

        viewHolder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(view.getContext(), NovoItemContaActivity.class);
                intent.putExtra("id", conta.getId());
                view.getContext().startActivity(intent);
            }
        });

        //pagar conta

        viewHolder.btPag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //setar mês pago
                realm = Realm.getDefaultInstance();
                //se for cartao atualizar
                if (conta.isCartao()) {
                    if (conta.getItens().size() > 0) {
                        for (int i = conta.getItens().size() - 1; i >= 0; i--) {
                            final ItemConta itemConta = conta.getItens().get(i);
                            int qtdParcela = itemConta.getQtdParcela() - 1;
                            if (qtdParcela == 0) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try {
                                            itemConta.deleteFromRealm();
                                        } catch (RealmError ex) {
                                            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                realm.beginTransaction();
                                conta.setMesPago(GregorianCalendar.MONTH);
                                itemConta.setQtdParcela(qtdParcela);
                                realm.commitTransaction();
                            }
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Não existem débitos no cartão " + conta.getDescricao(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {
                                conta.deleteFromRealm();
                            } catch (RealmError ex) {
                                Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //verificar situacao da conta no mes
        if (conta.getMesPago() == GregorianCalendar.MONTH) {
            viewHolder.layoutItem.setBackgroundResource(R.color.colorPago);
        }

        //excluir conta
        viewHolder.btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Excluir a conta " + conta.getDescricao() + "?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try {
                                            conta.deleteFromRealm();
                                        } catch (RealmError ex) {
                                            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        } finally {
                                            Toast.makeText(view.getContext(), "Cliente excluído com sucesso", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.show();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescricao;
        private TextView tvVencimento;
        private TextView tvValor;
        private Button btAdd;
        private Button btDel;
        private Button btPag;
        private RelativeLayout layoutItem;

        public ViewHolder(View v) {
            super(v);

            tvDescricao = v.findViewById(R.id.descricao_adapter_main);
            tvVencimento = v.findViewById(R.id.vencimento_adapter_main);
            tvValor = v.findViewById(R.id.valor_adapter_main);
            btAdd = v.findViewById(R.id.bt_add_item_adapter_main);
            btDel = v.findViewById(R.id.bt_excluir_item_adapter_main);
            btPag = v.findViewById(R.id.bt_pagar_item_adapter_main);
            layoutItem = v.findViewById(R.id.layout_adapter_main);

        }

    }
}
