package org.edgarsuarezmota.informer.drivingschool;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("Preguntas.json?alt=media&token=5c2fc7a1-6c5b-4673-b2fc-1b38221e890f")
    Call<TemasResponse> obtenerTemas();
}



