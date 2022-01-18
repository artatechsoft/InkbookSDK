package pl.goltstein.staticscrollexample

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.global_main.*
import pl.inkcompat.PagingRecyclerView
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.global_main.list_container as pagingRecyclerView

class MainActivity : AppCompatActivity() {


    private val rvAdapter: RVLanguageAdapter by lazy {
        RVLanguageAdapter(ArrayList(getExampleList(17))).apply { addEmptyView(findViewById(R.id.empty_view)) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.global_main)

        PagingRecyclerView.isDebuggable = true
        setSupportActionBar(toolbar_global)

        pagingRecyclerView.setAdapter(rvAdapter)
        pagingRecyclerView.layoutManager = GridLayoutManager(this, 3)

    }

    private fun getExampleList(size: Int): ArrayList<String> {
        val list = ArrayList<String>()

        for (i in 0 until size) {
            list.add("Message nr.$i")
        }

        return list
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_dark_navbar -> {
                pagingRecyclerView.setNavigationBarColor(resources.getColor(R.color.colorPrimary))
            }
            R.id.action_light_navbar -> {
                pagingRecyclerView.setNavigationBarColor(Color.WHITE)
            }
            R.id.action_new_items -> {
                addListItems(7)
            }
            R.id.action_new_item -> {
                addListItems(1)
            }
            R.id.action_remove_one_item -> {
                removeOneItem()
            }
            R.id.action_remove_items -> {
                removeAllItems()
            }
            R.id.action_resize_view -> {
                pagingRecyclerView.layoutParams.height = 700
                pagingRecyclerView.requestLayout()
            }
            R.id.action_resize_view_to_parent -> {
                pagingRecyclerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                pagingRecyclerView.requestLayout()
            }
        }


        return super.onOptionsItemSelected(item)
    }


    private fun addListItems(count: Int) {
        val listContent = ArrayList(rvAdapter.data)
        for (i in 0 until count) {
            if(i == 30){
                listContent.add("Swap item")
            }else
                listContent.add(listContent.size.toString())

        }
        rvAdapter.swap(listContent)
    }

    private fun removeOneItem() {
        val listContent = ArrayList(rvAdapter.data)
        listContent.removeAt(listContent.size - 1)
        rvAdapter.swap(listContent)
    }

    private fun removeAllItems() {
        val listContent = ArrayList(rvAdapter.data)
        listContent.clear()
        rvAdapter.swap(listContent)
    }


}
