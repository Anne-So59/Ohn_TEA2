package com.example.tea1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
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

class ShowListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    private val items: MutableList<ItemToDo> = mutableListOf()

    private lateinit var editTextNouvelItem: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val listTitle = intent.getStringExtra("maListe") ?: ""
        findViewById<TextView>(R.id.textViewTitre).text = listTitle

        recyclerView = findViewById(R.id.recyclerViewItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemAdapter = ItemAdapter(this, items)
        recyclerView.adapter = itemAdapter

        val listId = intent.getStringExtra("listId") ?: ""
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val hash = sharedPreferences.getString("hash", "")

        if (listId.isNotEmpty() && !hash.isNullOrEmpty()) {
            recupererListItems(listId, hash)
        } else {
            Toast.makeText(this, "Liste ou hash non-existant", Toast.LENGTH_SHORT).show()
        }

        val buttonOkItem: Button

        editTextNouvelItem = findViewById(R.id.editTextNouvelItem)
        buttonOkItem = findViewById(R.id.buttonOkItem)
        buttonOkItem.setOnClickListener {
            val newItemLabel = editTextNouvelItem.text.toString().trim()
            if (newItemLabel.isNotEmpty()) {
                ajoutItemToList(newItemLabel)
            } else {
                Toast.makeText(this@ShowListActivity, "Veuillez compléter le champ", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun recupererListItems(listId: String, hash: String) {
        val call = DataProvider.apiService.getItems(listId, hash)
        call.enqueue(object : Callback<ItemDataClass> {
            override fun onResponse(call: Call<ItemDataClass>, response: Response<ItemDataClass>) {
                if (response.isSuccessful) {
                    val listItems = response.body()?.items?.map {
                        ItemToDo(it.label, it.checked == "1")
                    } ?: emptyList()
                    items.clear()
                    items.addAll(listItems)
                    itemAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ShowListActivity, "Echec de la requête", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ItemDataClass>, t: Throwable) {
                Toast.makeText(this@ShowListActivity, "Echec de la requête: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ajoutItemToList(label: String) {
        val listId = intent.getStringExtra("listId") ?: ""
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val hash = sharedPreferences.getString("hash", "")

        if (listId.isNotEmpty() && !hash.isNullOrEmpty()) {
            val call = DataProvider.apiService.ajoutItem(listId, label, hash)
            call.enqueue(object : Callback<NewItemDataClass> {
                override fun onResponse(call: Call<NewItemDataClass>, response: Response<NewItemDataClass>) {
                    if (response.isSuccessful) {
                        val newItem = response.body()?.item
                        if (newItem != null) {
                            items.add(newItem)
                            itemAdapter.notifyItemInserted(items.size - 1)
                            recyclerView.scrollToPosition(items.size - 1)
                            editTextNouvelItem.text.clear()
                        } else {
                            Toast.makeText(this@ShowListActivity, "Echec de l'ajout", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<NewItemDataClass>, t: Throwable) {
                    Toast.makeText(this@ShowListActivity, "Echec de la requête: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}

private fun <E> MutableList<E>.add(newItem: ItemItem) {
    this.add(newItem)
}

class ItemAdapter(private val context: Context, private val items: List<ItemToDo>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.description
        holder.checkBox.isChecked = item.fait

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            item.fait = isChecked
        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewTitre)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }
}
