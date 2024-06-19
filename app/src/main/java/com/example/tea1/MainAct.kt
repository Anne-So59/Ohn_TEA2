package com.example.tea1

import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var refBtnOK: Button? = null
    var refEdtNom: EditText? = null
    var refEdtMdp: EditText? = null
    private lateinit var sharedPreferences: SharedPreferences

    fun alerter(s: String?) {
        Log.i(PMR, s!!)
        val myToast = Toast.makeText(this, s, Toast.LENGTH_LONG)
        myToast.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val urlApi = sharedPreferences.getString("api_base_url", "http://tomnab.fr/todo-api")

        refBtnOK = findViewById(R.id.button)
        refEdtNom = findViewById(R.id.edtNom)
        refEdtMdp = findViewById(R.id.edtMdp)

        //remplissage automatique du champ de saisie
        val pseudoEnMemoire = sharedPreferences.getString("pseudo", "")
        refEdtNom?.setText(pseudoEnMemoire)


        //vérification du réseau
        val reseau = verifReseau()
        refBtnOK?.isEnabled = reseau

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
        val profile = ProfilListeToDo(pseudo, emptyList())

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

            val mdp = refEdtMdp!!.text.toString()
            alerter("contenu : $mdp")

            val editor = sharedPreferences.edit()
            editor.putString("pseudo", pseudo)
            editor.apply()

            if (pseudo.isNotEmpty() && mdp.isNotEmpty()) {
                authentificationUser(pseudo, mdp)
            } else {
                Toast.makeText(this, "Pseudo et mdp manquants", Toast.LENGTH_SHORT).show()
            }

            val profile = defaultProfile(pseudo)

            val mesListes = ArrayList(profile.mesListesToDo.map { it.titreListeToDo })

            val myIntent = Intent(this@MainActivity, ChoixListActivity::class.java).apply {
                putExtra("pseudo", pseudo)
                putStringArrayListExtra("mesListes", mesListes)
            }
        }
    }

    private fun authentificationUser(user: String, password: String) {
        val call = DataProvider.apiService.authenticateUser(user, password)
        call.enqueue(object : Callback<AuthenticateDataClass> {
            override fun onResponse(call: Call<AuthenticateDataClass>, response: Response<AuthenticateDataClass>) {
                if (response.isSuccessful) {
                    val authenticateData = response.body()
                    val hash = authenticateData?.hash ?: ""
                    Toast.makeText(this@MainActivity, "Hash: $hash", Toast.LENGTH_SHORT).show()

                    // sauvegarde du hash dans les préférences partagées
                    sharedPreferences.edit().putString("hash", hash).apply()

                    val profile = defaultProfile(user)
                    val mesListes = ArrayList(profile.mesListesToDo.map { it.titreListeToDo })

                    val myIntent = Intent(this@MainActivity, ChoixListActivity::class.java).apply {
                        putExtra("pseudo", user)
                        putStringArrayListExtra("mesListes", mesListes)
                    }
                    startActivity(myIntent)
                }
            }

            override fun onFailure(call: Call<AuthenticateDataClass>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Echec de la requête: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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

    fun verifReseau(): Boolean {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        val cnMngr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cnMngr.activeNetworkInfo

        var sType = "Aucun réseau détecté"
        var bStatut = false
        if (netInfo != null) {
            val netState = netInfo.state

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true
                val netType = netInfo.type
                when (netType) {
                    ConnectivityManager.TYPE_MOBILE -> sType = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> sType = "Réseau wifi détecté"
                }
            }
        }

        Log.i("PMR", sType!!)
        return bStatut
    }

    companion object {
        private const val PMR = "pmr2024"
    }

}
