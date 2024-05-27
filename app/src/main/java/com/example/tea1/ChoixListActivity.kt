package com.example.tea1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val context: Context, private val mesListes: List<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(context)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setPadding(16, 16, 16, 16)
        textView.setOnClickListener {
            val position = it.tag as Int
            val maListe = mesListes[position]
            val intent = Intent(context, ShowListActivity::class.java)
            intent.putExtra("maListe", maListe)
            context.startActivity(intent)
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = mesListes[position]
        holder.itemView.tag = position
    }

    override fun getItemCount(): Int {
        return mesListes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView as TextView
    }
}

class ChoixListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        val pseudo = intent.getStringExtra("pseudo") ?: ""
        val mesListes = intent.getStringArrayListExtra("mesListes") ?: arrayListOf()

        val textViewPseudo = findViewById<TextView>(R.id.textViewPseudo)
        textViewPseudo.text = pseudo

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CustomAdapter(this, mesListes)
    }
}