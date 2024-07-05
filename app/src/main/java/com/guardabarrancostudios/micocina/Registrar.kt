package com.guardabarrancostudios.micocina

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.github.kittinunf.result.Result
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.Fuel
import com.guardabarrancostudios.micocina.databinding.ActivityRegistrarBinding

class Registrar : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        binding.btnSelecPerfil.setOnClickListener{
            seleccionarImagen()
        }

    }

    private fun abrirLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun registrarUsuario() {
        val codigoUsuario = binding.etCodigoUsuario.text.toString().trim()
        val nombreUsuario = binding.etNombreUsuario.text.toString().trim()
        val correoUsuario = binding.etCorreo.text.toString().trim()
        val contraseñaUsuario = binding.etContrasena.text.toString().trim()
        val descripcionUsuario = binding.etDescripcionUsuario.text.toString().trim()

        if (codigoUsuario.isEmpty() || nombreUsuario.isEmpty() || correoUsuario.isEmpty() || contraseñaUsuario.isEmpty() || descripcionUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = ModelRegistrarUsuario(
            codigoUsuario, nombreUsuario, correoUsuario, contraseñaUsuario, "", descripcionUsuario
        )

        enviarUsuario(usuario)
    }

    private fun enviarUsuario(usuario: ModelRegistrarUsuario) {
        val json = """
        {
            "codigoUsuario": "${usuario.codigoUsuario}",
            "nombreUsuario": "${usuario.nombreUsuario}",
            "correoUsuario": "${usuario.correoUsuario}",
            "contraseñaUsuario": "${usuario.contraseñaUsuario}",
            "imagenUsuario": "${usuario.imagenUsuario}",
            "descripcionUsuario": "${usuario.descripcionUsuario}",
        }
    """.trimIndent()

        Fuel.post("http://www.micocina.somee.com/api/Usuarios_API")
            .header("Content-Type" to "application/json")
            .body(json)
            .response { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        runOnUiThread {
                            Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                            Log.d("codigo usuario=",usuario.codigoUsuario)
                            Log.d("nombre usuario=",usuario.nombreUsuario)
                            Log.d("correo usuario=",usuario.correoUsuario)
                            Log.d("contraseña usuario=",usuario.contraseñaUsuario)
                            Log.d("imagen usuario=",usuario.imagenUsuario)
                            Log.d("descripcion usuario=",usuario.descripcionUsuario)

                        }
                    }

                    is Result.Success -> {
                        runOnUiThread {
                            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_LONG)
                                .show()
                            abrirLogin()
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
            binding.imgUsuario.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_GET = 1
    }

}