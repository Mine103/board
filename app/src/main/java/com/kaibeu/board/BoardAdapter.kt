package com.kaibeu.board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaibeu.board.model.BoardData

class BoardAdapter(private val context: Context): RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    var datas = mutableListOf<BoardData>()

    interface OnClickListener {
        fun onItemClick(v: View, data: BoardData, pos: Int)
    }

    private var listener: OnClickListener? = null
    fun setOnItemClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reclyer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.title)
        private val creator: TextView = view.findViewById(R.id.creator)
        private val date: TextView = view.findViewById(R.id.date)

        @SuppressLint("SimpleDateFormat")
        fun bind(item: BoardData) {
            title.text = item.title
            creator.text = item.creator
            date.text = item.date

            val pos = adapterPosition
            if(pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }

    }
}