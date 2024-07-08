package com.guardabarrancostudios.micocina

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.guardabarrancostudios.micocina.databinding.ActivityBuscarBinding
import com.guardabarrancostudios.micocina.databinding.ActivityInicioBinding

class Buscar : AppCompatActivity() {
    private lateinit var binding: ActivityBuscarBinding
    private lateinit var adaptador: InicioAdaptador
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBuscarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        binding.recyclerRecetasBuscar.layoutManager = LinearLayoutManager(this)
        adaptador = InicioAdaptador(emptyList()) { receta ->
            val intent = Intent(this, DetalleReceta::class.java).apply {
                putExtra("tituloReceta", receta.tituloReceta)
                putExtra("descripcionReceta", receta.descripcionReceta)
                putExtra("imagenReceta", receta.imagenReceta)
            }
            startActivity(intent)
        }
        binding.recyclerRecetasBuscar.adapter = adaptador

        binding.btnAtras.setOnClickListener {
            val paginaInicio = Intent(this, Inicio::class.java)
            startActivity(paginaInicio)
        }

        binding.btnBuscar.setOnClickListener {
            val titulo = binding.edtxtBuscar.text.toString()
            buscarRecetas(titulo)
        }
    }

    private fun buscarRecetas(titulo: String) {
        val url = "http://www.micocina.somee.com/api/Recetas_API?tituloReceta=$titulo"

        Fuel.get(url).responseString { _, _, result ->
            when (result) {
                is Result.Success -> {
                    val data = result.get()
                    val listaRecetas = Gson().fromJson<List<ModelUsuarioConReceta>>(
                        data,
                        object : TypeToken<List<ModelUsuarioConReceta>>() {}.type
                    )
                    val recetasUsuario = listaRecetas.filter { it.tituloReceta == titulo }

                    runOnUiThread {
                        adaptador.actualizarRecetas(recetasUsuario)
                    }
                }

                is Result.Failure -> {
                    val error = result.error.exception.message
                    Toast.makeText(this, "Error al cargar recetas: $error", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}