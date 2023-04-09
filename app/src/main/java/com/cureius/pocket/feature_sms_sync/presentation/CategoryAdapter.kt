package com.cureius.pocket.feature_sms_sync.presentation


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cureius.pocket.R
import com.cureius.pocket.feature_category.domain.model.Category
import com.cureius.pocket.feature_pot.domain.util.IconDictionary

class CategoryAdapter(var dataList: List<Category>, private val currentPosition: Int?) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pop_up_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.categoryTitle.text = data.title
        holder.categoryIcon.setImageDrawable(
            ContextCompat.getDrawable(
                holder.categoryIcon.context, (IconDictionary.allIcons[data.icon]!!)
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        val categoryTitle: TextView = itemView.findViewById(R.id.category_title)
        val categoryContainer: CardView = itemView.findViewById(R.id.category_container)
    }
}
