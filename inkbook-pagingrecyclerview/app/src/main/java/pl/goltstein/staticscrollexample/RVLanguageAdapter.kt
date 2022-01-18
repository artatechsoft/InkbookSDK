package pl.goltstein.staticscrollexample

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.marcin_item.view.*
import pl.inkcompat.PagingRecyclerView

/**
 * Created by Goltstein on 14/12/2017.
 */

class RVLanguageAdapter internal constructor( var data: ArrayList<String>) : PagingRecyclerView.Adapter<RVLanguageAdapter.LanguageHolder>() {

    private var queryString: String = ""

    val internalList: ArrayList<String>
        get() {
            return ArrayList(if (queryString == "") data
            else data.filter { it == "Message nr.$queryString" })
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder = LanguageHolder(View.inflate(parent.context, R.layout.marcin_item, null))

    override fun getItemCount(): Int = internalList.size

    override fun onBindViewHolder(holder: LanguageHolder, position: Int) = holder.bind(position)


    fun swap(content: ArrayList<String>) {
        val diff = calculateDiff(data, content)

        data = content
        notifyDataSetChanged()
    }

    fun search(query: String) {
        queryString = query
        println("Filtered list: ${data.filter { it == queryString }}")
        notifyDataSetChanged()
    }


    inner class LanguageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "position: " + getPositionInDataSource(adapterPosition), Toast.LENGTH_SHORT).show()
            }

            itemView.setOnLongClickListener {
                val position = getPositionInDataSource(adapterPosition)
                data[position] = "Message changed"
                notifyItemChanged(position)
                return@setOnLongClickListener true
            }
        }

        fun bind(position: Int) {
            itemView.lang_title.text = internalList[position]
            itemView.lang_content.text = "inView: $adapterPosition\ninAdapter: ${getPositionInDataSource(adapterPosition)}"
        }

    }


}