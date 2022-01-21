package com.example.inkbook_screen_demo.ui.home

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
import com.example.inkbook_screen_demo.ui.UIModeInterface
import java.io.IOException
import java.io.InputStream

class ScrollFragment : Fragment() , UIModeInterface {

    private lateinit var scrollViewModel: ScrollViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        scrollViewModel =
                ViewModelProvider(this).get(ScrollViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val list = ArrayList<String>()
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")

        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")

        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")

        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        list.add("Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.Long text. Long text.")
        val listView: RecyclerView = binding.scrollListView
        val adapter = RecyclerViewAdapter(list, context)
        listView.adapter = adapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ScrollViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView : TextView = itemView.findViewById(R.id.scroll_view_item_title)
        var avatar : ImageView = itemView.findViewById(R.id.avatar)

    }


    class RecyclerViewAdapter(var listItems: List<String>, var context : Context?) : RecyclerView.Adapter<ScrollViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.scroll_view_item, parent, false)

            return ScrollViewHolder(view)
        }

        override fun onBindViewHolder(holder: ScrollViewHolder, position: Int) {
            val title = listItems[position]
            holder.textView.text = title
            loadImage(holder.avatar, context)
        }

        override fun getItemCount(): Int {
            return listItems.count()
        }

        private fun loadImage(avatar : ImageView, context : Context?) {
            val mImage = avatar
            try {
                // get input stream
                val ims: InputStream? = context?.assets?.open("bitmap0.png")
                // load image as Drawable
                val d = Drawable.createFromStream(ims, null)
                // set image to ImageView
                mImage.setImageDrawable(d)
                ims?.close()
            } catch (ex: IOException) {

            }
        }
    }

    override fun pressNormalMode() {
        InkBookSDK.refreshView(binding.scrollListView, 1)
    }

    override fun pressA2Mode() {
        InkBookSDK.refreshView(binding.scrollListView, 2)
    }

    override fun changeMode(mode: Int) {
        InkBookSDK.refreshView(binding.scrollListView, mode)
    }
}