package org.edgarsuarezmota.informer.drivingschool.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.edgarsuarezmota.informer.drivingschool.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categories;
    private Context context;
    private OnCategoryClickListener clickListener; // Paso 1: Interfaz para manejar clics

    public CategoryAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
    }

    // Método para establecer el listener
    public void setOnCategoryClickListener(OnCategoryClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categories.get(position);
        holder.bind(category);

        // Paso 3: Registrar el oyente de clics
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.tv_title_category);
        }

        public void bind(String category) {
            categoryTextView.setText(category);
        }
    }

    // Paso 2: Definir la interfaz para manejar clics
    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }
}


