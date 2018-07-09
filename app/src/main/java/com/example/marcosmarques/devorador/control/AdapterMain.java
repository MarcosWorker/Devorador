package com.example.marcosmarques.devorador.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcosmarques.devorador.R;
import com.example.marcosmarques.devorador.bean.Debito;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;

public class AdapterMain extends RealmRecyclerViewAdapter<Debito, AdapterMain.ViewHolder> {

    private Realm realm;

    public AdapterMain(RealmResults<Debito> data) {
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
        final Debito debito = getItem(position);
        DecimalFormat df = new DecimalFormat("###,##0.00");
        viewHolder.tvDescricao.setText(debito.getDescricao());
        viewHolder.tvDiaVencimento.setText("Dia do Vencimento - " + debito.getDiaVencimento() + " de cada Mês");
        viewHolder.tvParcelasQuitadas.setText(debito.getQtdParcelaQuitada() + " parcela(s) quitada(s)");
        viewHolder.tvQtdParcela.setText("Dividido em " + debito.getQtdParcela() + " vezes");
        viewHolder.tvTipo.setText(debito.getTipo());
        viewHolder.tvValorParcela.setText("Valor Parcela - R$ " + String.valueOf(df.format(debito.getValorParcela())));
        viewHolder.tvValorTotal.setText("Valor Total - R$ " + String.valueOf(df.format(debito.getValorTotal())));
        viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Deletar Débito ?")
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                realm = Realm.getDefaultInstance();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        try {
                                            debito.deleteFromRealm();
                                        } catch (RealmError ex) {
                                            Toast.makeText(view.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        } finally {
                                            Toast.makeText(view.getContext(), "Débito excluído com sucesso", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        });
                builder.show();
                return true;
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescricao;
        private TextView tvTipo;
        private TextView tvQtdParcela;
        private TextView tvValorParcela;
        private TextView tvValorTotal;
        private TextView tvDiaVencimento;
        private TextView tvParcelasQuitadas;
        private RelativeLayout layout;

        public ViewHolder(View v) {
            super(v);

            tvDescricao = v.findViewById(R.id.descricao_adapter_main);
            tvTipo = v.findViewById(R.id.tipo_adapter_main);
            tvQtdParcela = v.findViewById(R.id.qtd_parcela_adapter_main);
            tvValorParcela = v.findViewById(R.id.valor_parcela_adapter_main);
            tvValorTotal = v.findViewById(R.id.valor_total_adapter_main);
            tvDiaVencimento = v.findViewById(R.id.dia_vencimento_adapter_main);
            tvParcelasQuitadas = v.findViewById(R.id.qtd_parcela_quiatada_adapter_main);
            layout = v.findViewById(R.id.layout_adapter_main);
        }

    }
}
