package com.guardabarrancostudios.micocina

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.guardabarrancostudios.micocina.databinding.ActivityInicioBinding
import com.google.gson.Gson
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.reflect.TypeToken

class Inicio : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private lateinit var adaptador: InicioAdaptador
    private lateinit var sharedPreferences: SharedPreferences
    private var idUsuario: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        idUsuario = sharedPreferences.getInt("idUsuario", -1)

        binding.recyclerRecetasInicio.layoutManager = LinearLayoutManager(this)
        adaptador = InicioAdaptador(emptyList()) { receta ->
            val intent = Intent(this, DetalleReceta::class.java).apply {
                putExtra("tituloReceta", receta.tituloReceta)
                putExtra("descripcionReceta", receta.descripcionReceta)
                putExtra("imagenReceta", receta.imagenReceta)
            }
            startActivity(intent)
        }
        binding.recyclerRecetasInicio.adapter = adaptador

        cargarRecetas()

        binding.btnInicio.setOnClickListener {
            // No necesitas reiniciar la actividad actual
            val paginaInicio = Intent(this, Receta::class.java)
            startActivity(paginaInicio)
        }

        binding.btnCrear.setOnClickListener {
            val paginaCrear = Intent(this, Receta::class.java)
            startActivity(paginaCrear)
        }

        binding.btnPerfil.setOnClickListener {
            val paginaPerfil = Intent(this, Perfil::class.java)
            startActivity(paginaPerfil)
        }

        binding.btnBuscar.setOnClickListener {
            val paginaBuscar = Intent(this, Buscar::class.java)
            startActivity(paginaBuscar)
        }
    }

    private fun cargarRecetas() {
        val url = "http://www.micocina.somee.com/api/Recetas_API?idUsuario=$idUsuario"

        Fuel.get(url).responseString { _, _, result ->
            when (result) {
                is Result.Success -> {
                    val data = result.get()
                    val listaRecetas = Gson().fromJson<List<ModelUsuarioConReceta>>(
                        data,
                        object : TypeToken<List<ModelUsuarioConReceta>>() {}.type
                    )
                    val recetasUsuario = listaRecetas.filter { it.idUsuario == idUsuario }

                    runOnUiThread {
                        adaptador.actualizarRecetas(recetasUsuario)
                    }
                }
                is Result.Failure -> {
                    val error = result.error.exception.message
                    Toast.makeText(this, "Error al cargar recetas: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
