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
import com.github.kittinunf.fuel.gson.responseObject
import com.guardabarrancostudios.micocina.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar bordes de la pantalla
        enableEdgeToEdge()

        // Ajustar padding para los insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val username = binding.edtxtUserlogin.text.toString()
            val password = binding.edtxtContraseALogin.text.toString()

            // Validaciones básicas
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(username, password)
        }

        binding.btnRegistrarse.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }
    }

    private fun login(username: String, password: String) {
        val url = "http://www.micocina.somee.com/api/Usuarios_API"

        Fuel.get(url)
            .responseObject<List<ModelLogin>> { _, _, result ->
                result.fold(
                    success = { users ->
                        val user = users.find { it.nombreUsuario == username && it.contraseñaUsuario == password }

                        if (user != null) {
                            // Guardar el ID del usuario en SharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putInt("idUsuario", user.idUsuario)
                            editor.apply()

                            // Login correcto, iniciar MainActivity o la actividad deseada
                            val intent = Intent(this, Receta::class.java)
                            startActivity(intent)
                            finish() // Cierra la actividad actual
                        } else {
                            // Login error
                            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    failure = {
                        // Error de conexión con la API
                        Toast.makeText(this, "Error al conectarse con el servidor: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
    }
}
