package com.example.myapplication.ui.searchPhotos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.data.localstore.AppDatabase
import com.example.myapplication.utility.Constants
import kotlinx.android.synthetic.main.activity_search_past.*

class PastSearchActivity : AppCompatActivity(), PastSearchAdapter.SearchInterface, View.OnClickListener {

    private lateinit var database: AppDatabase
    private lateinit var pastSearchAdapter: PastSearchAdapter
    private var pastSearchList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_past)
        init()
        setListeners()
        setPastSearchAdapter()
        setVisibility()


    }

    private fun setVisibility() {


        if (pastSearchList.size > 0) {
            tvPastSearchLabel.visibility = View.VISIBLE
        } else {
            tvPastSearchLabel.visibility = View.GONE

        }
    }

    private fun setListeners() {
        ivSearch.setOnClickListener(this)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filterSearch(s)
            }

        })
    }

    private fun filterSearch(s: CharSequence) {
        val temp = ArrayList<String>()
        for (d in pastSearchList) {
            if (d.toLowerCase().contains(s)) {
                temp.add(d)
            }
            pastSearchAdapter.updateList(temp)
        }
    }

    private fun init() {
        database = AppDatabase.getInstance(this)


    }

    override fun onSearchTextClick(searchText: String) {
        val intent = Intent()
        intent.putExtra(Constants.SEARCHED_TEXT, searchText)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private fun setPastSearchAdapter() {
        pastSearchList = database.userDao().loadSearchText()
        rvPastSearch.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )
        pastSearchAdapter = PastSearchAdapter(this, pastSearchList, this)
        rvPastSearch.adapter = pastSearchAdapter
        rlPastSearch.visibility = View.VISIBLE

    }

    override fun onClick(v: View?) {
        if (!etSearch.text.toString().trim().isEmpty())
            onSearchTextClick(etSearch.text.toString().trim())
    }
}
