package com.guardabarrancostudios.micocina

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.reflect.TypeToken
//import com.guardabarrancostudios.micocina.adapters.InicioAdaptador
import com.guardabarrancostudios.micocina.databinding.ActivityInicioBinding

class Inicio : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private lateinit var adaptador: InicioAdaptador
    private lateinit var sharedPreferences: SharedPreferences
    private var idUsuario: Int = -1
    private var idReceta: Int = -1 // Variable para almacenar el ID de la receta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        idUsuario = sharedPreferences.getInt("idUsuario", -1)

        // Configurar RecyclerView y adaptador
        binding.recyclerRecetasInicio.layoutManager = LinearLayoutManager(this)
        adaptador = InicioAdaptador(emptyList()) { receta ->
            // Guardar el ID de la receta seleccionada en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("idReceta", receta.idReceta)
            editor.apply()

            // Abrir la actividad de detalle de receta
            val intent = Intent(this, DetalleReceta::class.java)
            startActivity(intent)
        }
        binding.recyclerRecetasInicio.adapter = adaptador

        // Cargar las recetas del usuario actual
        cargarRecetas()

        // Configurar botones de navegación
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
        // Obtener el ID de la receta seleccionada desde SharedPreferences
        val idReceta = sharedPreferences.getInt("idReceta", -1)
        val url = "http://www.micocina.somee.com/api/Recetas_API?idUsuario=$idUsuario&idReceta=$idReceta"

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
