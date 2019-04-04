package com.example.myapplication.ui.searchPhotos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import kotlinx.android.synthetic.main.list_item_search.view.*

class PastSearchAdapter(private val context: Context, private var searchedPhotoTextList: MutableList<String>,
                        private val searchInterface: SearchInterface) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return searchedPhotoTextList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tvSearch.text = searchedPhotoTextList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_search,
                parent, false))
    }

    fun updateList(temp: ArrayList<String>) {
        searchedPhotoTextList = temp
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.tvSearch.setOnClickListener {
                searchInterface.onSearchTextClick(searchedPhotoTextList[adapterPosition])
            }
        }
    }

    interface SearchInterface {
        fun onSearchTextClick(searchText: String)
    }

}


