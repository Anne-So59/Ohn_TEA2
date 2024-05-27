package com.example.tea1

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Tentative de récupération des préférences partagées et mets le pseudo saisi dans le champ correspondant
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val pseudoPreference = findPreference<EditTextPreference>("pseudo")
        pseudoPreference?.summary = "Pseudo actuel: ${sharedPreferences.getString("pseudo", "")}"
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
            "background_color_switch" -> {
                val arrierePlan = sharedPreferences?.getBoolean(key, false) ?: false
                val editor = sharedPreferences?.edit()
                editor?.putBoolean(key, arrierePlan)
                editor?.apply()

                if (arrierePlan) {
                    activity?.findViewById<ConstraintLayout>(R.id.settings_container)?.setBackgroundColor(Color.parseColor("#FFD700"))
                } else {
                    activity?.findViewById<ConstraintLayout>(R.id.settings_container)?.setBackgroundColor(Color.WHITE)
                }
            }
        }
    }

}