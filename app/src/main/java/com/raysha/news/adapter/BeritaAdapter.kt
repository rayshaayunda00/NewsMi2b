package com.raysha.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raysha.news.DetailBeritaActivity
import com.raysha.news.R
import com.raysha.news.models.BeritaResponse
import com.squareup.picasso.Picasso

class BeritaAdapter (
    val dataBerita: ArrayList<BeritaResponse.ListItem>

): RecyclerView.Adapter<BeritaAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        //inisialisasi widget//
        val imgBerita = view.findViewById<ImageView>(R.id.imgBerita)
        val tvJudul = view.findViewById<TextView>(R.id.tvJudul)
        val tvTglBerita = view.findViewById<TextView>(R.id.tvTglBerita)
        val tvRating = view.findViewById<TextView>(R.id.tvRating)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_news,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return dataBerita.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //tampilkan data///
        val hasilResponse = dataBerita[position]
        Picasso.get().load(hasilResponse.gambar).into(holder.imgBerita)
        holder.tvJudul.text = hasilResponse.judul
        holder.tvTglBerita.text = hasilResponse.tgl_indonesia_berita
        holder.tvRating.text = "${hasilResponse.rating}"
        holder.ratingBar.rating = hasilResponse.rating.toFloat()

        //klik item berita
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,DetailBeritaActivity::class.java).apply {
                putExtra("gambar",hasilResponse.gambar)
                putExtra("judul",hasilResponse.judul)
                putExtra("tgl_berita",hasilResponse.tgl_indonesia_berita)
                putExtra("rating",hasilResponse.rating)
                putExtra("isi",hasilResponse.isi)

            }
            holder.imgBerita.context.startActivity(intent)
        }

        }
    fun setData(data:List<BeritaResponse.ListItem>){
        dataBerita.clear()
        dataBerita.addAll(data)
    }

}

