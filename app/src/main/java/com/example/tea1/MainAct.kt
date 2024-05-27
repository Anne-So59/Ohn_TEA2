package com.example.tea1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var refBtnOK: Button? = null
    var refEdtNom: EditText? = null
    lateinit var sharedPreferences: SharedPreferences

    fun alerter(s: String?) {
        Log.i(PMR, s!!)
        val myToast = Toast.makeText(this, s, Toast.LENGTH_LONG)
        myToast.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        refBtnOK = findViewById(R.id.button)
        refEdtNom = findViewById(R.id.edtNom)

        //tentative de remplissage automatique du champ de saisie
        sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE)
        val pseudoEnMemoire = sharedPreferences.getString("pseudo", "")
        refEdtNom?.setText(pseudoEnMemoire)

        // tentative de gestion de l'arrière-plan sur la MainActivity
        /*val arrierePlan = sharedPreferences.getBoolean("background_color_switch", false)
        if (arrierePlan) {
            findViewById<ConstraintLayout>(R.id.main).setBackgroundColor(Color.parseColor("#FFD700"))
        } else {
            findViewById<ConstraintLayout>(R.id.main).setBackgroundColor(Color.WHITE)
        }*/

        refBtnOK?.setOnClickListener(this)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun defaultProfile(pseudo: String): ProfilListeToDo {
        val item1 = ItemToDo("Item 1")
        val item2 = ItemToDo("Item 2")
        val item3 = ItemToDo("Item 3")

        val liste1 = ListeToDo()
        liste1.titreListeToDo = "Liste 1"
        liste1.changerLesItems(listOf(item1, item2, item3))

        val liste2 = ListeToDo()
        liste2.titreListeToDo = "Liste 2"
        liste2.changerLesItems(listOf(item1, item2, item3))

        val liste3 = ListeToDo()
        liste3.titreListeToDo = "Liste 3"
        liste3.changerLesItems(listOf(item1, item2, item3))

        val profile = ProfilListeToDo(pseudo, listOf(liste1, liste2, liste3))

        val gson = Gson()
        val json = gson.toJson(profile)
        val editor = sharedPreferences.edit()
        editor.putString("profil_$pseudo", json)
        editor.apply()

        return profile
    }

    override fun onClick(view: View) {
        if (view.id == R.id.button) {
            val pseudo = refEdtNom!!.text.toString()
            alerter("contenu : $pseudo")

            val editor = sharedPreferences.edit()
            editor.putString("pseudo", pseudo)
            editor.apply()

            val profile = defaultProfile(pseudo)

            val mesListes = ArrayList(profile.mesListesToDo.map { it.titreListeToDo })

            val myIntent = Intent(this@MainActivity, ChoixListActivity::class.java).apply {
                putExtra("pseudo", pseudo)
                putStringArrayListExtra("mesListes", mesListes)
            }
            startActivity(myIntent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_pref -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val PMR = "pmr2024"
    }
}
