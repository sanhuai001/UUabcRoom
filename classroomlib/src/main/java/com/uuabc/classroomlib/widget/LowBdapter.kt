package com.uuabc.classroomlib.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class LowBdapter<T, H : RecyclerView.ViewHolder> : RecyclerView.Adapter<H> {
    lateinit var context: Context
    var data = mutableListOf<T>()
    lateinit var itemView: View
    var itemClickListener: OnItemClickListener? = null
    lateinit var viewHolder: RecyclerView.ViewHolder

    interface OnItemClickListener {
        fun click(position: Int)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    constructor()

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context, data: MutableList<T>) {
        this.context = context
        this.data = data
    }

    fun setDataResource(data: MutableList<T>) {
        this.data = data

    }

    fun addDataResource(data: List<T>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun getItemData(position: Int): T? {
        return if (position < this.data.size) {
            data[position]
        } else null
    }

    fun getList(): ArrayList<T> {
        return ArrayList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        itemView = LayoutInflater.from(context).inflate(getLayoutId(viewType), parent, false)
        return createHolder(itemView)
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        bindView(holder, position, data)
    }

    override fun getItemCount(): Int {
        return data.size
    }


    open class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)


    abstract fun bindView(holder: H, position: Int, data: MutableList<T>)

    abstract fun getLayoutId(viewType: Int): Int

    abstract fun createHolder(view: View): H
}