package org.edgarsuarezmota.informer.drivingschool.result;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.edgarsuarezmota.informer.drivingschool.R;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<ResultItem> resultList;
    private Context context;

    public ResultAdapter(Context context, List<ResultItem> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        ResultItem resultItem = resultList.get(position);
        holder.bind(resultItem);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTexto;
        private TextView textViewNumero;
        private TextView textViewEtiqueta;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTexto = itemView.findViewById(R.id.tv_question_result);
            textViewNumero = itemView.findViewById(R.id.tv_number_result);
            textViewEtiqueta = itemView.findViewById(R.id.tv_label);
        }

        public void bind(ResultItem resultItem) {
            textViewTexto.setText(resultItem.getTexto());
            textViewNumero.setText(String.valueOf(resultItem.getNumero()));
            textViewEtiqueta.setText(resultItem.getEtiqueta());
        }
    }
}

