package com.example.inkbook_screen_demo.ui.notifications

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.artatech.inkbooksdk.InkBookSDK
import com.example.inkbook_screen_demo.databinding.FragmentNotificationsBinding
import com.example.inkbook_screen_demo.ui.UIModeInterface
import java.io.IOException
import java.io.InputStream


class ReaderFragment : Fragment(), UIModeInterface{

    private lateinit var readerViewModel: ReaderViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private var prefix = "bitmap"
    private var min = 0
    private var max = 9
    private var current = 0
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        readerViewModel =
                ViewModelProvider(this).get(ReaderViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        binding.next.setOnClickListener {
            if(current < max){
                current += 1
                loadImage()
            }
        }
        binding.previous.setOnClickListener {
            if(current > 0){
                current -= 1
                loadImage()
            }
        }
        loadImage()
        return root
    }

    private fun loadImage() {
        val mImage = binding.readerView
        try {
            // get input stream
            val ims: InputStream? = context?.assets?.open("$prefix$current.png")
            // load image as Drawable
            val d = Drawable.createFromStream(ims, null)
            // set image to ImageView
            mImage.setImageDrawable(d)
            ims?.close()
        } catch (ex: IOException) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun pressNormalMode() {
        InkBookSDK.refreshView(binding.readerView, 1)
    }

    override fun pressA2Mode() {
        InkBookSDK.refreshView(binding.readerView, 2)
    }

    override fun changeMode(mode: Int) {
        InkBookSDK.refreshView(binding.readerView, mode)
    }
}