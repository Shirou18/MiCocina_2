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
    private var idUsuario: Int = -1 // Variable para almacenar el ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        // Obtener el ID del usuario guardado en SharedPreferences
        idUsuario = sharedPreferences.getInt("idUsuario", -1)

        // Configurar el RecyclerView y el adaptador
        binding.recyclerRecetasInicio.layoutManager = LinearLayoutManager(this)
        adaptador = InicioAdaptador(emptyList()) { receta ->
            // Acción al hacer clic en una receta (puedes implementar lo que necesites aquí)
            Toast.makeText(this, "Clic en ${receta.tituloReceta}", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerRecetasInicio.adapter = adaptador

        // Cargar y mostrar las recetas
        cargarRecetas()

        binding.btnInicio.setOnClickListener {
            // No necesitas reiniciar la actividad actual
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

        // Mostrar el idUsuario en el TextView txtRecetaInicio si es necesario
        // binding.txtRecetaInicio.text = "ID de Usuario: $idUsuario"
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

                    // Filtrar recetas por el ID de usuario
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
