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
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.util.IconDictionary

class PotAdapter(
    var dataList: List<Pot>,
    private val currentPosition: Int?,
    private val listener: OnItemSelectedListener
) :
    RecyclerView.Adapter<PotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pop_up_pot_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataList[position]
        holder.potName.text = data.title
        holder.potIcon.setImageDrawable(
            ContextCompat.getDrawable(
                holder.potIcon.context, (IconDictionary.allIcons[data.icon]!!)
            )
        )
        holder.potIcon.setOnClickListener {
            listener.onItemSelected(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val potIcon: ImageView = itemView.findViewById(R.id.pot_icon)
        val potName: TextView = itemView.findViewById(R.id.pot_name)
        val potContainer: CardView = itemView.findViewById(R.id.pot_item)
    }

    interface OnItemSelectedListener {
        fun onItemSelected(pot: Pot)
    }
}


