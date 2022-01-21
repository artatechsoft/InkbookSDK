package com.example.inkbook_screen_demo.ui.dashboard

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.artatech.inkbooksdk.InkBookSDK
import com.example.inkbook_screen_demo.R
import com.example.inkbook_screen_demo.databinding.FragmentDashboardBinding
import com.example.inkbook_screen_demo.ui.UIModeInterface

class VideoFragment : Fragment() , UIModeInterface {

    private lateinit var videoViewModel: VideoViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        videoViewModel =
                ViewModelProvider(this).get(VideoViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val videoView: VideoView = binding.videoView
        val path = "android.resource://" + context?.packageName + "/" + R.raw.filmik;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun pressNormalMode() {
        InkBookSDK.refreshView(binding.videoView, 1)

    }

    override fun pressA2Mode() {
        InkBookSDK.refreshView(binding.videoView, 2)
    }

    override fun changeMode(mode: Int) {
        InkBookSDK.refreshView(binding.videoView, mode)
    }
}