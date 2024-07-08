package com.guardabarrancostudios.micocina

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.guardabarrancostudios.micocina.databinding.ActivityDetalleRecetaBinding

class DetalleReceta : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleRecetaBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var idUsuario: Int = -1
    private var idReceta: Int = -1
    private var esFavorito: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleRecetaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        idUsuario = sharedPreferences.getInt("idUsuario", -1)
        idReceta = sharedPreferences.getInt("idReceta", -1)

        if (idReceta != -1) {
            fetchRecetaDetalle(idReceta)
        }

        binding.btnFavorito.setOnClickListener {
            esFavorito = !esFavorito
            if (esFavorito) {
                binding.btnFavorito.setImageResource(R.drawable.favoritoseleccionado)
            } else {
                binding.btnFavorito.setImageResource(R.drawable.favorito)
            }
        }

        binding.btnEditar.setOnClickListener { editarReceta(idReceta) }
        binding.btnEliminar.setOnClickListener { eliminarReceta(idReceta) }
    }

    private fun fetchRecetaDetalle(idReceta: Int) {
        Fuel.get("http://www.micocina.somee.com/api/Recetas_API/$idReceta")
            .header("Follow-Redirects" to "false")
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Toast.makeText(this, "Error al obtener los detalles: ${ex.message}", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        val jsonBody = result.get()
                        val recetaDetalle = Gson().fromJson(jsonBody, ModelUsuarioConDetalle::class.java)
                        mostrarDetalleReceta(recetaDetalle)
                    }
                }
            }
    }

    private fun mostrarDetalleReceta(recetaDetalle: ModelUsuarioConDetalle) {
        binding.txtTituloRecetaDetalle.text = recetaDetalle.tituloReceta
        binding.edtxtReceta.setText(recetaDetalle.tituloReceta)
        binding.edtxtIngredientes.setText(recetaDetalle.ingredientes)
        binding.edtxtInstrucciones.setText(recetaDetalle.descripcionReceta)
        binding.edtxtTiempoPreparacion.setText(recetaDetalle.tiempoPreparacion)
        binding.edtxtCategoria.setText(recetaDetalle.nombreCategoria)

        when (recetaDetalle.tipoDificultad) {
            "Fácil" -> binding.radioFacil.isChecked = true
            "Medio" -> binding.radioMedio.isChecked = true
            "Difícil" -> binding.radioDificil.isChecked = true
        }

        esFavorito = recetaDetalle.favorito
        if (esFavorito) {
            binding.btnFavorito.setImageResource(R.drawable.favoritoseleccionado)
        } else {
            binding.btnFavorito.setImageResource(R.drawable.favorito)
        }
    }

    private fun abrirInicio() {
        startActivity(Intent(this, Inicio::class.java))
        finish() // Esto cierra la actividad actual para mantener limpia la pila de actividades
    }

    private fun editarReceta(idReceta: Int) {
        val tituloReceta = binding.edtxtReceta.text.toString()
        val ingredientes = binding.edtxtIngredientes.text.toString()
        val descripcionReceta = binding.edtxtInstrucciones.text.toString()
        val tiempoPreparacion = binding.edtxtTiempoPreparacion.text.toString()
        val categoria = binding.edtxtCategoria.text.toString()
        val dificultad = when {
            binding.radioFacil.isChecked -> "Fácil"
            binding.radioMedio.isChecked -> "Medio"
            binding.radioDificil.isChecked -> "Difícil"
            else -> ""
        }

        if (tituloReceta.isEmpty() || ingredientes.isEmpty() || descripcionReceta.isEmpty() ||
            tiempoPreparacion.isEmpty() || categoria.isEmpty() || dificultad.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val receta = ModelUsuarioConDetalle(
            idReceta = idReceta,
            idUsuario = idUsuario,
            tituloReceta = tituloReceta,
            ingredientes = ingredientes,
            descripcionReceta = descripcionReceta,
            tiempoPreparacion = tiempoPreparacion,
            nombreCategoria = categoria,
            tipoDificultad = dificultad,
            favorito = esFavorito
        )

        val jsonBody = Gson().toJson(receta)
        println("JSON enviado para editar receta: $jsonBody")

        val url = "http://www.micocina.somee.com/api/Recetas_API/$idReceta"

        Fuel.put(url)
            .header("Content-Type" to "application/json")
            .body(jsonBody)
            .response { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        runOnUiThread {
                            Toast.makeText(this, "Error al actualizar la receta: ${ex.message}", Toast.LENGTH_SHORT).show()
                            println("Error en la solicitud PUT: ${ex.message}")
                        }
                    }
                    is Result.Success -> {
                        runOnUiThread {
                            Toast.makeText(this, "Receta actualizada correctamente", Toast.LENGTH_SHORT).show()
                            abrirInicio() // Cambio aquí
                        }
                    }
                }
            }
    }

    private fun eliminarReceta(idReceta: Int) {
        val url = "http://www.micocina.somee.com/api/Recetas_API/$idReceta"

        Fuel.delete(url).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    runOnUiThread {
                        Toast.makeText(this, "Error eliminando receta: ${ex.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Success -> {
                    runOnUiThread {
                        Toast.makeText(this, "Receta eliminada", Toast.LENGTH_SHORT).show()
                        finish()
                        fetchRecetaDetalle(idReceta)
                    }
                }
            }
        }
    }
}
