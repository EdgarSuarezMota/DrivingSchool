package org.edgarsuarezmota.informer.drivingschool.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("temas")
    private List<CategoryResponse.Tema> temas;

    public List<CategoryResponse.Tema> getTemas() {
        return temas;
    }

    public void setTemas(List<CategoryResponse.Tema> temas) {
        this.temas = temas;
    }


    public class Tema {
        @SerializedName("categoria")
        private String categoria;

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }


    }
}
