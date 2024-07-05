package com.guardabarrancostudios.micocina

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guardabarrancostudios.micocina.databinding.ActivityRecetaBinding
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result


class Receta : AppCompatActivity() {
    private lateinit var binding: ActivityRecetaBinding
    private var selectedImageUri: Uri? = null

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

        // Configuración del botón para seleccionar una imagen
        binding.btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
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
        val descripcionReceta = binding.edtxtInstrucciones.text.toString().trim()
        val tiempoPreparacion = binding.edtxtTiempoPreparacion.text.toString().trim()
        val nombreCategoria = binding.edtxtCategoria.text.toString().trim()

        // Validaciones
        if (tituloReceta.isEmpty() || ingredientes.isEmpty() || descripcionReceta.isEmpty() ||
            tiempoPreparacion.isEmpty() || nombreCategoria.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener la dificultad seleccionada
        val tipoDificultad = when (binding.radioDificultad.checkedRadioButtonId) {
            R.id.radioFacil -> "Fácil"
            R.id.radioMedio -> "Medio"
            R.id.radioDificil -> "Difícil"
            else -> {
                // Mostrar un mensaje al usuario para que seleccione una opción
                binding.radioDificultad.requestFocus() // Establece el foco en el grupo de radio buttons
                Toast.makeText(this, "Por favor, selecciona una dificultad", Toast.LENGTH_SHORT).show()
                return // Salir del método o función
            }
        }
        //Constructor
        val receta = ModelReceta(
            tituloReceta,
            ingredientes,
            descripcionReceta,
            tiempoPreparacion,
            nombreCategoria,
            tipoDificultad,
            "" // Asignar valor por defecto a imagenReceta ya que no se recoge en el formulario
        )

        // Enviar la receta a través de Fuel
        enviarReceta(receta)
    }

    private fun enviarReceta(receta: ModelReceta) {
        val json = """
    {
        "tituloReceta": "${receta.tituloReceta}",
        "nombreCategoria": "${receta.nombreCategoria}",
        "tipoDificultad": "${receta.tipoDificultad}",
        "descripcionReceta": "${receta.descripcionReceta}",
        "imagenReceta": "${receta.imagenReceta}", // Ajustar esta línea para incluir correctamente la imagen seleccionada
        "tiempoPreparacion": "${receta.tiempoPreparacion}",
        "ingredientes": "${receta.ingredientes}"
    }
""".trimIndent()


        Fuel.post("http://www.micocina.somee.com/api/Recetas_API")
            .header("Content-Type" to "application/json")
            .body(json)
            .response { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        runOnUiThread {
                            Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Result.Success -> {
                        runOnUiThread {
                            Toast.makeText(this, "Receta creada correctamente", Toast.LENGTH_LONG).show()
                            abrirInicio()
                        }
                    }
                }
            }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_GET)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgReceta.setImageURI(selectedImageUri)
        }
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

    companion object {
        private const val REQUEST_IMAGE_GET = 1
    }
}
