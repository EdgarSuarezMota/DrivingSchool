package org.edgarsuarezmota.informer.drivingschool.main;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TopicsResponse {
    @SerializedName("temas")
    private List<Tema> temas;

    public List<Tema> getTemas() {
        return temas;
    }

    public void setTemas(List<Tema> temas) {
        this.temas = temas;
    }


    public class Tema {
        @SerializedName("categoria")
        private String categoria;
        @SerializedName("preguntas")
        private List<Pregunta> preguntas;

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public List<Pregunta> getPreguntas() {
            return preguntas;
        }

        public void setPreguntas(List<Pregunta> preguntas) {
            this.preguntas = preguntas;
        }
    }

    public class Pregunta {
        @SerializedName("pregunta")
        private String pregunta;
        @SerializedName("opciones")
        private Opciones opciones;
        @SerializedName("respuesta")
        private String respuesta;

        public String getPregunta() {
            return pregunta;
        }

        public void setPregunta(String pregunta) {
            this.pregunta = pregunta;
        }

        public Opciones getOpciones() {
            return opciones;
        }

        public void setOpciones(Opciones opciones) {
            this.opciones = opciones;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }

    public class Opciones {
        @SerializedName("A")
        private String opcionA;
        @SerializedName("B")
        private String opcionB;
        @SerializedName("C")
        private String opcionC;
        @SerializedName("D")
        private String opcionD;

        public String getOpcionA() {
            return opcionA;
        }

        public void setOpcionA(String opcionA) {
            this.opcionA = opcionA;
        }

        public String getOpcionB() {
            return opcionB;
        }

        public void setOpcionB(String opcionB) {
            this.opcionB = opcionB;
        }

        public String getOpcionC() {
            return opcionC;
        }

        public void setOpcionC(String opcionC) {
            this.opcionC = opcionC;
        }

        public String getOpcionD() {
            return opcionD;
        }

        public void setOpcionD(String opcionD) {
            this.opcionD = opcionD;
        }
    }
}

