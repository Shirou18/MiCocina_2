package com.guardabarrancostudios.micocina

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guardabarrancostudios.micocina.databinding.ActivityRecetaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Receta : AppCompatActivity() {
    private lateinit var binding: ActivityRecetaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecetaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Ajustar el padding para los insets de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del botón para crear receta
        binding.btnCrear.setOnClickListener {
            crearReceta()
        }

        // Configuración del botón para postear la receta
        binding.btnPostear.setOnClickListener {
            crearReceta()
        }

        // Configuración de los botones de navegación
        binding.btnInicio.setOnClickListener {
            abrirInicio()
        }

        binding.btnPerfil.setOnClickListener {
            abrirPerfil()
        }
    }

    private fun crearReceta() {
        val tituloReceta = binding.edtxtReceta.text.toString().trim()
        val ingredientes = binding.edtxtIngredientes.text.toString().trim()
        val instrucciones = binding.edtxtInstrucciones.text.toString().trim()
        val tiempoPreparacion = binding.edtxtTiempoPreparacion.text.toString().trim()
        val categoria = binding.edtxtCategoria.text.toString().trim()

        // Validaciones
        if (tituloReceta.isEmpty() || ingredientes.isEmpty() || instrucciones.isEmpty() ||
            tiempoPreparacion.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener la dificultad seleccionada
        val dificultad = when (binding.radioDificultad.checkedRadioButtonId) {
            R.id.radioFacil -> "Fácil"
            R.id.radioMedio -> "Medio"
            R.id.radioDificil -> "Difícil"
            else -> ""
        }

        val receta = ModelReceta(
            tituloReceta,
            ingredientes,
            instrucciones,
            "", // Asignar valor por defecto a imagenReceta ya que no se recoge en el formulario
            tiempoPreparacion,
            categoria,
            dificultad
        )

        // Enviar la receta a través de Retrofit
        enviarReceta(receta)
    }


    private fun enviarReceta(receta: ModelReceta) {
        val retrofitService = ApiClient.retrofitInstance.create(InterfazRetrofit_RecetasAPI::class.java)
        val call = retrofitService.crearReceta(receta)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Receta, "Receta creada correctamente", Toast.LENGTH_SHORT).show()
                    abrirInicio()
                } else {
                    Toast.makeText(this@Receta, "Error al crear la receta", Toast.LENGTH_SHORT).show()
                }
            }
            //Maneja los errores del dominio
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Receta, "Error:${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun abrirInicio() {
        val intent = Intent(this, Inicio::class.java)
        startActivity(intent)
        finish()
    }

    private fun abrirPerfil() {
        val intent = Intent(this, Perfil::class.java)
        startActivity(intent)
        finish()
    }
}
