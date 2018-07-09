package com.example.marcosmarques.devorador.control;

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

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class AdapterMain extends RealmRecyclerViewAdapter<Debito, AdapterMain.ViewHolder> {

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
        viewHolder.tvDescricao.setText(debito.getDescricao());
        viewHolder.tvDiaVencimento.setText("Dia do Vencimento - " + debito.getDiaVencimento() + "de cada Mês");
        viewHolder.tvParcelasQuitadas.setText(debito.getQtdParcelaQuitada() + " parcela(s) quitada(s)");
        viewHolder.tvQtdParcela.setText("Dividido em " + debito.getQtdParcela() + " vezes");
        viewHolder.tvTipo.setText(debito.getTipo());
        viewHolder.tvValorParcela.setText("Valor Parcela - R$ " + String.valueOf(debito.getValorParcela()));
        viewHolder.tvValorTotal.setText("Valor Total - R$ " + String.valueOf(debito.getValorTotal()));
        viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "ok", Toast.LENGTH_SHORT).show();
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
