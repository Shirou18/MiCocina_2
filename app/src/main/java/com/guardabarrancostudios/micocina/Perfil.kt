package com.guardabarrancostudios.micocina

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.guardabarrancostudios.micocina.databinding.ActivityPerfilBinding

class Perfil : AppCompatActivity() {
    private lateinit var binding: ActivityPerfilBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var idUsuario: Int = -1 // Variable para almacenar el ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnInicio.setOnClickListener {
            val paginaBuscar = Intent(this, Inicio::class.java)
            startActivity(paginaBuscar)
        }

        binding.btnCrear.setOnClickListener {
            val paginaCrear = Intent(this, Receta::class.java)
            startActivity(paginaCrear)
        }

        binding.btnPerfil.setOnClickListener {
            val paginaPerfil = Intent(this, Perfil::class.java)
            startActivity(paginaPerfil)
        }

        binding.btnRegistrarse.setOnClickListener {
            actualizarPerfil()
            //obtenerPerfil()

            // Inicializar SharedPreferences
            sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

            // Obtener el ID del usuario guardado en SharedPreferences
            idUsuario = sharedPreferences.getInt("idUsuario", -1)
        }


    }

    private fun abrirLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }



    private fun actualizarPerfil() {
        val codigoUsuario = binding.edtxtCodPerfil.text.toString().trim()
        val nombreUsuario = binding.edtxtNomPerfil.text.toString().trim()
        val correoUsuario = binding.edtxtCorrepPerfil.text.toString().trim()
        val contrasenaUsuario = binding.edtxtContraseAPerfil.text.toString().trim()
        val descripcionUsuario = binding.edtxtDescripPerfil.text.toString().trim()

        if (codigoUsuario.isEmpty() || nombreUsuario.isEmpty() || correoUsuario.isEmpty() || descripcionUsuario.isEmpty()|| contrasenaUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val perfil = ModelRegistrarPerfil(
            idUsuario, codigoUsuario, nombreUsuario, correoUsuario,contrasenaUsuario, descripcionUsuario
        )

        enviarPerfil(perfil)
    }

    private fun enviarPerfil(perfil: ModelRegistrarPerfil) {
        val json = """
        {
            "idUsuario": ${perfil.idUsuario},
            "codigoUsuario": "${perfil.codigoUsuario}",
            "nombreUsuario": "${perfil.nombreUsuario}",
            "correoUsuario": "${perfil.correoUsuario}",
            "contraseñaUsuario": "${perfil.contrasenaUsuario}",
            "descripcionUsuario": "${perfil.descripcionUsuario}"
        }
    """.trimIndent()

        Fuel.put("http://www.micocina.somee.com/api/Usuarios_API/${perfil.idUsuario}")
            .header("Content-Type" to "application/json")
            .body(json)
            .responseString() { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        runOnUiThread {
                            Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                            Log.d("codigoUsuario=", perfil.codigoUsuario)
                            Log.d("nombreUsuario=", perfil.nombreUsuario)
                            Log.d("correoUsuario=", perfil.correoUsuario)
                            Log.d("contraseñaUsuario=", perfil.contrasenaUsuario)
                            Log.d("descripcionUsuario=", perfil.descripcionUsuario)
                        }
                    }

                    is Result.Success -> {
                        runOnUiThread {
                            Toast.makeText(this, "Usuario Actualizado correctamente", Toast.LENGTH_LONG)
                                .show()
                            abrirLogin()
                        }
                    }
                }
            }
    }
}
