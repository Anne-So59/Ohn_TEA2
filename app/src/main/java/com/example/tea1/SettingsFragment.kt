package com.example.tea1

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Récupération des préférences partagées et mets le pseudo saisi dans le champ correspondant
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val pseudoPreference = findPreference<EditTextPreference>("pseudo")
        pseudoPreference?.summary = "Pseudo actuel: ${sharedPreferences.getString("pseudo", "")}"

        // Gestion de l'URL de base de l'API
        val urlPreference = findPreference<EditTextPreference>("url_api")
        urlPreference?.summary = "URL de l'API: ${sharedPreferences.getString("url_api", "")}"

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "pseudo" -> {
                val newPseudoValue = sharedPreferences?.getString(key, "")
                val editor = sharedPreferences?.edit()
                editor?.putString(key, newPseudoValue)
                editor?.apply()

                val pseudoPreference = findPreference<EditTextPreference>(key)
                pseudoPreference?.summary = "Pseudo actuel: $newPseudoValue"
            }
            "url_api" -> {
                val nouvelUrl = sharedPreferences?.getString(key, "")
                val urlPreference = findPreference<EditTextPreference>(key)
                urlPreference?.summary = "URL de l'API: $nouvelUrl"
            }
        }
    }

}