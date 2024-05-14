package org.edgarsuarezmota.informer.drivingschool.category;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import org.edgarsuarezmota.informer.drivingschool.NetworkUtils;
import org.edgarsuarezmota.informer.drivingschool.api.ApiService;
import org.edgarsuarezmota.informer.drivingschool.api.RetrofitClient;
import org.edgarsuarezmota.informer.drivingschool.R;
import org.edgarsuarezmota.informer.drivingschool.main.TopicsResponse;
import org.edgarsuarezmota.informer.drivingschool.databinding.ActivityQuestionsTopicsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsTopicsActivity extends AppCompatActivity {
    private ActivityQuestionsTopicsBinding binding;
    private static final String TAG = "QuestionsActivity";
    private List<TopicsResponse.Tema> temas;
    private Random random;
    // Variable para guardar un String
    private String answer;
    private String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsTopicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        random = new Random();

        binding.rgOptions.clearCheck();

        String categoria = getIntent().getStringExtra("categoria");
        if (NetworkUtils.isNetworkAvailable(this)) {
            realizarLlamadaObtenerTemas(categoria);
        } else {
            mostrarAlertaSinWifi("Error", "No hay conexion a internet");
        }


        binding.btnNext.setOnClickListener(v -> {
            alternarVistasInversa();
            if (NetworkUtils.isNetworkAvailable(this)) {
                realizarLlamadaObtenerTemas(categoria);
            } else {
                mostrarAlertaSinWifi("Error", "No hay conexion a internet");
            }
        });

        binding.topBarQuestions.setNavigationOnClickListener(v -> onBackPressed());

        binding.btnCheck.setOnClickListener(v -> {
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
                    binding.btnCheck.setVisibility(View.INVISIBLE);
                    binding.btnNext.setVisibility(View.VISIBLE);
                    if (selectedOption.equals("A")) {
                        binding.rbA.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    } else if (selectedOption.equals("B")) {
                        binding.rbB.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    } else if (selectedOption.equals("C")) {
                        binding.rbC.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    } else if (selectedOption.equals("D")) {
                        binding.rbD.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    }
                    // Llamada a la función para mostrar la alerta
                    mostrarAlerta("Respuesta correcta", "Has acertado la pregunta.");
                    guardarAciertos(question);
                } else {
                    binding.btnCheck.setVisibility(View.INVISIBLE);
                    binding.btnNext.setVisibility(View.VISIBLE);
                    if (selectedOption.equals("A")) {
                        binding.rbA.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_incorrect));
                    } else if (selectedOption.equals("B")) {
                        binding.rbB.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_incorrect));
                    } else if (selectedOption.equals("C")) {
                        binding.rbC.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_incorrect));
                    } else if (selectedOption.equals("D")) {
                        binding.rbD.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_incorrect));
                    }
                    if (answer.equals("A")) {
                        binding.rbA.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    } else if (answer.equals("B")) {
                        binding.rbB.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    } else if (answer.equals("C")) {
                        binding.rbC.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    } else if (answer.equals("D")) {
                        binding.rbD.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.answer_correct));
                    }
                    guardarFallos(question);
                    mostrarAlerta("Respuesta incorrecta", "No has acertado la pregunta.");
                }
            } else {
                // Si no se ha seleccionado ninguna opción
                Log.d(TAG, "Ninguna opción seleccionada");
            }
        });

    }


    private void realizarLlamadaObtenerTemas(String categoria) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<TopicsResponse> call = apiService.obtenerTemas();

        call.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                if (response.isSuccessful()) {
                    TopicsResponse topicsResponse = response.body();
                    if (topicsResponse != null) {
                        temas = topicsResponse.getTemas();
                        // Filtrar preguntas por la categoría proporcionada
                        List<TopicsResponse.Pregunta> preguntasCategoria = new ArrayList<>();
                        for (TopicsResponse.Tema tema : temas) {
                            if (tema.getCategoria().equals(categoria)) {
                                preguntasCategoria.addAll(tema.getPreguntas());
                            }
                        }
                        // Llamar a la función para obtener una pregunta aleatoria
                        TopicsResponse.Pregunta preguntaAleatoria = obtenerPreguntaAleatoria(preguntasCategoria);
                        if (preguntaAleatoria != null) {
                            binding.tvQuestion.setText(preguntaAleatoria.getPregunta());
                            binding.rbA.setText(preguntaAleatoria.getOpciones().getOpcionA());
                            binding.rbB.setText(preguntaAleatoria.getOpciones().getOpcionB());
                            binding.rbC.setText(preguntaAleatoria.getOpciones().getOpcionC());
                            binding.rbD.setText(preguntaAleatoria.getOpciones().getOpcionD());
                            question=preguntaAleatoria.getPregunta();
                            answer = preguntaAleatoria.getRespuesta();
                            alternarVistas();
                            // Aquí puedes utilizar la pregunta aleatoria como desees
                            Log.d(TAG, "Pregunta aleatoria: " + preguntaAleatoria.getPregunta());
                        } else {
                            mostrarAlerta("Error", "No se pudo obtener una pregunta aleatoria");
                            Log.e(TAG, "No se pudo obtener una pregunta aleatoria");
                        }
                    } else {
                        mostrarAlerta("Error", "No se pudo obtener una pregunta aleatoria");
                        Log.e(TAG, "El objeto TemasResponse es nulo");
                    }
                } else {
                    mostrarAlertaSinWifi("Error", "No se pudo conectar al servidor");
                    Log.e(TAG, "Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                Log.e(TAG, "Error al obtener los temas: " + t.getMessage());
            }
        });
    }

    private TopicsResponse.Pregunta obtenerPreguntaAleatoria(List<TopicsResponse.Pregunta> preguntas) {
        if (preguntas != null && !preguntas.isEmpty()) {
            // Seleccionar una pregunta aleatoria
            return preguntas.get(random.nextInt(preguntas.size()));
        }
        return null;
    }

    // Función para mostrar una alerta simple con un título, un mensaje y un botón "Aceptar"
    private void mostrarAlerta(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>" + titulo + "</font>"))
                .setMessage(Html.fromHtml("<font color='#000000'>" + mensaje + "</font>"))
                .setPositiveButton(Html.fromHtml("<font color='#990000'>Aceptar</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra la alerta
                        dialog.dismiss();
                    }
                });

        // Establecer el fondo blanco para el diálogo
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        alertDialog.show();
    }

    private void mostrarAlertaSinWifi(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#000000'>" + titulo + "</font>"))
                .setMessage(Html.fromHtml("<font color='#000000'>" + mensaje + "</font>"))
                .setPositiveButton(Html.fromHtml("<font color='#990000'>Aceptar</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra la alerta
                        dialog.dismiss();
                        // Cierra la aplicación por completo
                        finishAffinity();
                    }
                });

        // Establecer el fondo blanco para el diálogo
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        alertDialog.show();
    }
    private void alternarVistas() {
        binding.rgOptions.clearCheck();
        binding.rbA.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.white));
        binding.rbB.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.white));
        binding.rbC.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.white));
        binding.rbD.setBackgroundColor(ContextCompat.getColor(QuestionsTopicsActivity.this, R.color.white));
        // Oculta la vista para ocultar
        binding.cpiQuestions.setVisibility(View.GONE);
        // Muestra la vista para mostrar
        binding.rgOptions.setVisibility(View.VISIBLE);
        binding.tvQuestion.setVisibility(View.VISIBLE);
        binding.btnCheck.setVisibility(View.VISIBLE);
    }
    private void alternarVistasInversa() {
        // Oculta la vista para ocultar
        binding.cpiQuestions.setVisibility(View.VISIBLE);
        // Muestra la vista para mostrar
        binding.rgOptions.setVisibility(View.INVISIBLE);
        binding.tvQuestion.setVisibility(View.INVISIBLE);
        binding.btnCheck.setVisibility(View.INVISIBLE);
        binding.btnNext.setVisibility(View.INVISIBLE);
    }
    private void guardarAciertos(String texto) {
        // Obtener el ID guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UUID", ""); // Aquí "UUID" es la clave utilizada para guardar el ID del usuario

        // Verificar si se ha guardado un ID válido en SharedPreferences
        if (!userId.isEmpty()) {
            // Obtener una instancia de la base de datos de Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Crear una referencia al nodo del usuario bajo "Aciertos"
            DatabaseReference userReference = database.getReference("Aciertos").child(userId);

            // Crear una referencia al nodo del texto bajo el nodo del usuario
            DatabaseReference textoReference = userReference.child(texto); // Usar hash del texto como identificador único

            // Guardar el texto y el número asociado
            textoReference.child("texto").setValue(texto);

            // Incrementar el número o inicializarlo en 1 si no existe
            textoReference.child("numero").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer numeroActual = mutableData.getValue(Integer.class);
                    if (numeroActual == null) {
                        mutableData.setValue(1);
                    } else {
                        mutableData.setValue(numeroActual + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        // Manejar errores de transacción
                        Log.e("TAG", "Error en la transacción: " + databaseError.getMessage());
                    } else {
                        // Transacción completada exitosamente
                        Log.d("TAG", "Transacción completada con éxito");
                    }
                }
            });
        } else {
            // El ID guardado en SharedPreferences es inválido o está vacío
            Log.e("TAG", "No se encontró un ID válido en SharedPreferences");
        }
    }

    private void guardarFallos(String texto) {
        // Obtener el ID guardado en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("UUID", ""); // Aquí "UUID" es la clave utilizada para guardar el ID del usuario

        // Verificar si se ha guardado un ID válido en SharedPreferences
        if (!userId.isEmpty()) {
            // Obtener una instancia de la base de datos de Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            // Crear una referencia al nodo del usuario bajo "Aciertos"
            DatabaseReference userReference = database.getReference("Fallos").child(userId);

            // Crear una referencia al nodo del texto bajo el nodo del usuario
            DatabaseReference textoReference = userReference.child(texto); // Usar hash del texto como identificador único

            // Guardar el texto y el número asociado
            textoReference.child("texto").setValue(texto);

            // Incrementar el número o inicializarlo en 1 si no existe
            textoReference.child("numero").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Integer numeroActual = mutableData.getValue(Integer.class);
                    if (numeroActual == null) {
                        mutableData.setValue(1);
                    } else {
                        mutableData.setValue(numeroActual + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        // Manejar errores de transacción
                        Log.e("TAG", "Error en la transacción: " + databaseError.getMessage());
                    } else {
                        // Transacción completada exitosamente
                        Log.d("TAG", "Transacción completada con éxito");
                    }
                }
            });
        } else {
            // El ID guardado en SharedPreferences es inválido o está vacío
            Log.e("TAG", "No se encontró un ID válido en SharedPreferences");
        }
    }
}
