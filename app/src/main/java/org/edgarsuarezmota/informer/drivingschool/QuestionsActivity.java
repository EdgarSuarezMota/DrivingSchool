package org.edgarsuarezmota.informer.drivingschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.edgarsuarezmota.informer.drivingschool.databinding.ActivityQuestionsBinding;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class QuestionsActivity extends AppCompatActivity {
    private ActivityQuestionsBinding binding;
    private static final String TAG = "QuestionsActivity";
    private List<TemasResponse.Tema> temas;
    private Random random;
    // Variable para guardar un String
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        random = new Random();

        realizarLlamadaObtenerTemas();
        binding.topBarQuestions.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del RadioButton seleccionado en el RadioGroup
                int selectedRadioButtonId = binding.rgOptions.getCheckedRadioButtonId();
                // Verificar si se ha seleccionado alguna opción
                if (selectedRadioButtonId != -1) {
                    // Obtener el texto de la opción seleccionada
                    String selectedOption = "";
                    if (selectedRadioButtonId == R.id.rb_a) {
                        selectedOption = "A";
                    } else if (selectedRadioButtonId == R.id.rb_b) {
                        selectedOption = "B";
                    } else if (selectedRadioButtonId == R.id.rb_c) {
                        selectedOption = "C";
                    } else if (selectedRadioButtonId == R.id.rb_d) {
                        selectedOption = "D";
                    }
                    // Realizar la acción deseada con la opción seleccionada
                    Log.d(TAG, "Opción seleccionada: " + selectedOption);
                    if (answer.equals(selectedOption)) {
                        binding.btnNext.setVisibility(View.INVISIBLE);
                        // Llamada a la función para mostrar la alerta
                        mostrarAlertaSimple("Respuesta correcta", "Has acertado la pregunta.");

                    } else {
                        binding.btnNext.setVisibility(View.INVISIBLE);
                        mostrarAlertaSimple("Respuesta incorrecta", "No has acertado la pregunta, la respuesta era  " + answer);
                    }
                } else {
                    // Si no se ha seleccionado ninguna opción
                    Log.d(TAG, "Ninguna opción seleccionada");
                }
            }
        });

    }


    private void realizarLlamadaObtenerTemas() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<TemasResponse> call = apiService.obtenerTemas();

        call.enqueue(new Callback<TemasResponse>() {
            @Override
            public void onResponse(Call<TemasResponse> call, Response<TemasResponse> response) {
                if (response.isSuccessful()) {
                    TemasResponse temasResponse = response.body();
                    if (temasResponse != null) {
                        temas = temasResponse.getTemas();
                        // Llamar a la función para obtener una pregunta aleatoria
                        TemasResponse.Pregunta preguntaAleatoria = obtenerPreguntaAleatoria();
                        if (preguntaAleatoria != null) {
                            binding.tvQuestion.setText(preguntaAleatoria.getPregunta());
                            binding.rbA.setText(preguntaAleatoria.getOpciones().getOpcionA());
                            binding.rbB.setText(preguntaAleatoria.getOpciones().getOpcionB());
                            binding.rbC.setText(preguntaAleatoria.getOpciones().getOpcionC());
                            binding.rbD.setText(preguntaAleatoria.getOpciones().getOpcionD());
                            answer = preguntaAleatoria.getRespuesta();
                            // Aquí puedes utilizar la pregunta aleatoria como desees
                            Log.d(TAG, "Pregunta aleatoria: " + preguntaAleatoria.getPregunta());
                        } else {
                            Log.e(TAG, "No se pudo obtener una pregunta aleatoria");
                        }
                    } else {
                        Log.e(TAG, "El objeto TemasResponse es nulo");
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TemasResponse> call, Throwable t) {
                Log.e(TAG, "Error al obtener los temas: " + t.getMessage());
            }
        });
    }

    private TemasResponse.Pregunta obtenerPreguntaAleatoria() {
        if (temas != null && !temas.isEmpty()) {
            // Seleccionar un tema aleatorio
            TemasResponse.Tema temaAleatorio = temas.get(random.nextInt(temas.size()));
            List<TemasResponse.Pregunta> preguntas = temaAleatorio.getPreguntas();
            if (preguntas != null && !preguntas.isEmpty()) {
                // Seleccionar una pregunta aleatoria del tema seleccionado
                return preguntas.get(random.nextInt(preguntas.size()));
            }
        }
        return null;
    }

    // Función para mostrar una alerta simple con un título, un mensaje y un botón "Aceptar"
    private void mostrarAlertaSimple(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra la alerta
                        dialog.dismiss();
                    }
                });
        // Crea y muestra la alerta
        AlertDialog alert = builder.create();
        alert.show();
    }
}
