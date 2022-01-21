package com.example.inkbook_screen_demo.ui.inputText

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.artatech.inkbooksdk.InkBookSDK
import com.example.inkbook_screen_demo.R
import com.example.inkbook_screen_demo.databinding.FragmentHomeBinding
import com.example.inkbook_screen_demo.databinding.FragmentInputtextBinding
import com.example.inkbook_screen_demo.ui.UIModeInterface
import com.example.inkbook_screen_demo.ui.home.ScrollViewModel
import java.io.IOException
import java.io.InputStream

class InputTextFragment : Fragment() , UIModeInterface {

    private var _binding: FragmentInputtextBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInputtextBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun pressNormalMode() {
        InkBookSDK.refreshView(binding.inputText, 1)
    }

    override fun pressA2Mode() {
        InkBookSDK.refreshView(binding.inputText, 2)
    }

    override fun changeMode(mode: Int) {
        InkBookSDK.refreshView(binding.inputText, mode)
    }
}