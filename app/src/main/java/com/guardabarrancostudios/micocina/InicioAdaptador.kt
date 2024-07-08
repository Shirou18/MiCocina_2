package com.guardabarrancostudios.micocina

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class InicioAdaptador(
    private var recetas: List<ModelUsuarioConReceta>,
    private val onItemClick: (ModelUsuarioConReceta) -> Unit
) : RecyclerView.Adapter<InicioAdaptador.RecetaViewHolder>() {

    inner class RecetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitulo = itemView.findViewById<TextView>(R.id.txtTituloRecetaInicio)
        private val imagenReceta = itemView.findViewById<ImageView>(R.id.imgRecetaInicio)
        private val descripcionReceta = itemView.findViewById<TextView>(R.id.txtDescrip_RecetaInicio) // Agregado

        fun bind(receta: ModelUsuarioConReceta) {
            txtTitulo.text = receta.tituloReceta
            descripcionReceta.text = receta.descripcionReceta // Agregado
            Glide.with(itemView.context)
                .load(receta.imagenReceta)
                .into(imagenReceta)

            itemView.setOnClickListener {
                onItemClick(receta)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receta_inicio, parent, false)
        return RecetaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recetas.size
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val receta = recetas[position]
        holder.bind(receta)
    }

    // Método para actualizar la lista de recetas
    fun actualizarRecetas(nuevaListaRecetas: List<ModelUsuarioConReceta>) {
        recetas = nuevaListaRecetas
        notifyDataSetChanged()
    }
}
