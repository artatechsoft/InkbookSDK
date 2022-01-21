package com.example.inkbook_screen_demo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.inkbook_screen_demo.databinding.ActivityMainBinding
import com.example.inkbook_screen_demo.ui.UIModeInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding


    private var focusModes = when (Build.MODEL.toUpperCase(Locale.getDefault())) {
        "EXPLORE" -> mapOf(
            "EINK_WAVEFORM_MODE_INIT" to 0,
            "EINK_WAVEFORM_MODE_DU" to 1,
            "EINK_WAVEFORM_MODE_GC16" to 2,
            "EINK_WAVEFORM_MODE_GL16" to 3,
            "EINK_WAVEFORM_MODE_GC4" to 3,
            "EINK_WAVEFORM_MODE_REAGL" to 3,
            "EINK_WAVEFORM_MODE_GLR16" to 4,
            "EINK_WAVEFORM_MODE_GLD16" to 5,
            "EINK_WAVEFORM_MODE_ANIM" to 6,
            "EINK_WAVEFORM_MODE_DU4" to 7,
            "EINK_WAVEFORM_MODE_AUTO" to 15,
            "EINK_WAVEFORM_MODE_MASK" to 15,
            "EINK_AUTO_MODE_REGIONAL" to 0,
            "EINK_AUTO_MODE_AUTOMATIC" to 16,
            "EINK_AUTO_MODE_MASK" to 16,
            "EINK_UPDATE_MODE_PARTIAL" to 0,
            "EINK_UPDATE_MODE_FULL" to 32,
            "EINK_UPDATE_MODE_MASK" to 32,
            "EINK_WAIT_MODE_NOWAIT" to 0,
            "EINK_WAIT_MODE_WAIT" to 64,
            "EINK_WAIT_MODE_MASK" to 64,
            "EINK_COMBINE_MODE_NOCOMBINE" to 0,
            "EINK_COMBINE_MODE_COMBINE" to 128,
            "EINK_COMBINE_MODE_MASK" to 128,
            "EINK_DITHER_MODE_NODITHER" to 0,
            "EINK_DITHER_MODE_DITHER" to 256,
            "EINK_DITHER_MODE_MASK" to 256,
            "EINK_INVERT_MODE_NOINVERT" to 0,
            "EINK_INVERT_MODE_INVERT" to 512,
            "EINK_INVERT_MODE_MASK" to 512,
            "EINK_CONVERT_MODE_NOCONVERT" to 0,
            "EINK_CONVERT_MODE_CONVERT" to 1024,
            "EINK_CONVERT_MODE_MASK" to 1024,
            "EINK_DITHER_COLOR_Y4" to 0,
            "EINK_DITHER_COLOR_Y1" to 2048,
            "EINK_REAGL_MODE_REAGL" to 0,
            "EINK_REAGL_MODE_REAGLD" to 4096,
            "EINK_REAGL_MODE_MASK" to 4096,
            "EINK_DITHER_COLOR_MASK" to 2048,
            "EINK_MODE_GC16_PART" to 2,
            "EINK_MODE_DU_PART" to 1,
            "EINK_MODE_GC16_FULL" to 98,
            "EINK_MODE_A2_PART" to 6,
            "EINK_MODE_GC4_PART" to 3,
            "EINK_MODE_GL16_PART" to 3,
            "EINK_MODE_GL16_FULL_WAIT" to 4195,
            "EINK_MODE_GL16_FULL_NOWAIT" to 4131,
            "EINK_MODE_GLR16_FULL_WAIT" to 4196,
            "EINK_MODE_GLR16_FULL_NOWAIT" to 4132,
            "EINK_MODE_GLD16_FULL_WAIT" to 4197,
            "EINK_MODE_GLD16_FULL_NOWAIT" to 4133
        )
        else -> mapOf(
           // "EPD_NULL" to -1,
           // "EPD_AUTO" to 0,
            "EPD_FULL" to 1,
            "EPD_A2" to 2
          /*  "EPD_PART" to 3,
            "EPD_FULL_DITHER" to 4,
            "EPD_RESET" to 5,
            "EPD_BLACK_WHITE" to 6,
            "EPD_BG" to 7,
            "EPD_BLOCK" to 8,
            "EPD_FULL_WIN" to 9,
            "EPD_OED_PART" to 10,
            "EPD_DIRECT_PART" to 11,
            "EPD_DIRECT_A2" to 12,
            "EPD_STANDBY" to 13,
            "EPD_POWEROFF" to 14,
            "EPD_NOPOWER" to 15,
            "EPD_AUTO_BG" to 16,
            "EPD_UNBLOCK" to 17,
            "EPD_PART_GL16" to 18,
            "EPD_PART_GLR16" to 19,
            "EPD_PART_GLD16" to 20,
            "EPD_FULL_GL16" to 21,
            "EPD_FULL_GLR16" to 22,
            "EPD_FULL_GLD16" to 23*/
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val ad: ArrayAdapter<*> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            focusModes.keys.toList()
        )
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.modeSpinner.adapter = ad
        binding.modeSpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View, position: Int,
        id: Long
    ) {
        val model = focusModes.keys.toList()[position]
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
        focusModes[model]?.let {
            (navHostFragment?.childFragmentManager?.fragments?.get(0) as UIModeInterface).changeMode(
                it
            )
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
