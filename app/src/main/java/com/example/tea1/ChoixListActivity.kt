package com.example.tea1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomAdapter(private val context: Context, private val mesListes: List<ListList>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_lists, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val maListe = mesListes[position]
        holder.textView.text = maListe.label
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShowListActivity::class.java).apply {
                putExtra("maListe", maListe.label)
                putExtra("listId", maListe.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = mesListes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewListTitle)
    }
}

class ChoixListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customAdapter: CustomAdapter
    private val mesListes: MutableList<ListList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        val pseudo = intent.getStringExtra("pseudo") ?: ""

        val textViewPseudo = findViewById<TextView>(R.id.textViewPseudo)
        textViewPseudo.text = pseudo

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        customAdapter = CustomAdapter(this, mesListes)
        recyclerView.adapter = customAdapter

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val hash = sharedPreferences.getString("hash", "")

        if (!hash.isNullOrEmpty()) {
            getLists(hash)
        } else {
            Toast.makeText(this, "Le hash n'existe pas", Toast.LENGTH_SHORT).show()
        }

        val editTextNouvelleListe = findViewById<EditText>(R.id.editTextNouvelleListe)
        val buttonOkListe = findViewById<Button>(R.id.buttonOkListe)

        buttonOkListe.setOnClickListener {
            val newListLabel = editTextNouvelleListe.text.toString()
            if (newListLabel.isNotEmpty()) {
                if (hash != null) {
                    ajoutList(hash, newListLabel)
                }
            }
        }
    }


    private fun getLists(hash: String) {
        val call = DataProvider.apiService.getLists(hash)
        call.enqueue(object : Callback<ListDataClass> {
            override fun onResponse(call: Call<ListDataClass>, response: Response<ListDataClass>) {
                if (response.isSuccessful) {
                    val lists = response.body()?.lists ?: emptyList()
                    mesListes.clear()
                    mesListes.addAll(lists)
                    customAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ListDataClass>, t: Throwable) {
                Toast.makeText(this@ChoixListActivity, "Echec de la requête: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ajoutList(hash: String, label: String) {
        val call = DataProvider.apiService.ajoutList(hash, label)
        call.enqueue(object : Callback<NewListDataClass> {
            override fun onResponse(call: Call<NewListDataClass>, response: Response<NewListDataClass>) {
                if (response.isSuccessful) {
                    val newList = response.body()?.list ?: emptyList()
                    mesListes.addAll(newList)
                    customAdapter.notifyItemInserted(mesListes.size - newList.size)
                }
            }

            override fun onFailure(call: Call<NewListDataClass>, t: Throwable) {
                Toast.makeText(this@ChoixListActivity, "Revenir en arrière et rouvrir la page pour afficher la liste", Toast.LENGTH_SHORT).show()
            }
        })
    }
}