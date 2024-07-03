package com.guardabarrancostudios.micocina

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.guardabarrancostudios.micocina.databinding.ActivityInicioBinding
import com.guardabarrancostudios.micocina.databinding.ActivityLoginBinding

class Inicio : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnInicio.setOnClickListener{
            val paginaBuscar = Intent(this, Inicio::class.java)
            startActivity(paginaBuscar)
        }

        binding.btnCrear.setOnClickListener{
            val paginaCrear = Intent(this, Receta::class.java)
            startActivity(paginaCrear)
        }

        binding.btnPerfil.setOnClickListener{
            val paginaPerfil = Intent(this, Perfil::class.java)
            startActivity(paginaPerfil)
        }

        binding.btnBuscar.setOnClickListener{
            val paginaBuscar = Intent(this, Buscar::class.java)
            startActivity(paginaBuscar)
        }
    }
}
