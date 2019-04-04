package com.example.myapplication.ui.viewPhotos

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.localstore.AppDatabase
import com.example.myapplication.data.localstore.PhotoData
import com.example.myapplication.data.networkResponse.Photo
import com.example.myapplication.ui.searchPhotos.PastSearchActivity
import com.example.myapplication.ui.viewSinglePhoto.ViewSinglePhotoActivity
import com.example.myapplication.utility.Constants
import com.example.myapplication.utility.ProgressBar
import com.example.myapplication.utility.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_image.view.*

class MainActivity : AppCompatActivity(), ViewPhotosContract.View, View.OnClickListener,
        PhotosAdapter.Interface {

    private var spanCount: Int = 2
    private var stateBundle: Bundle? = null
    private var viewPhotosPresenter = ViewPhotosPresenter()
    private var photosList = ArrayList<Photo?>()
    private var backPressed: Long = 0
    private var roomList = ArrayList<Photo>()
    private var dbList: MutableList<PhotoData>? = ArrayList()
    private lateinit var progressBar: ProgressBar
    private lateinit var photosAdapter: PhotosAdapter
    private var searchedText = ""
    private var isLoading = false
    private var pageNo = 1
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setExitSharedElementCallback(mCallback)
        init()
        setAdapter()
        setListeners()
        setScrollListener()
        onSearchClick()
    }
    private fun init() {
        searchedText = getString(R.string.cricket)
        tvSearch.text = searchedText
        viewPhotosPresenter.attachView(this)
        progressBar = ProgressBar(this)
        appDatabase = AppDatabase.getInstance(this)
    }

    override fun setListeners() {
        rlSearch.setOnClickListener(this)
    }

    private fun setScrollListener() {
        rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= 48) {
                    if (Utils.isConnectedToInternet(baseContext)) {
                        pbLoader.visibility = View.VISIBLE
                        isLoading = true
                        viewPhotosPresenter.hitApiGetPhotos(false, ++pageNo, searchedText)
                    }
                }
            }
        })
    }

    private fun setAdapter() {
        rvImages.layoutManager = GridLayoutManager(this, 2)
        photosAdapter = PhotosAdapter(this, photosList, this)
        rvImages.adapter = photosAdapter
    }

    //Set new layout manager based on span count
    private fun setRecyclerLayoutManager(count: Int) {
        if (spanCount != count) {
            spanCount = count
            rvImages.layoutManager = GridLayoutManager(this, count)
        }
    }

    private val mCallback = object : SharedElementCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            if (stateBundle != null) {
                val startingPosition = stateBundle?.getInt(Constants.START_ITEM_POSITION)
                val currentPosition = stateBundle?.getInt(Constants.CURRENT_ITEM_POSITION)
                if (startingPosition != currentPosition) {
                    //updating the shared element
                    val newTransitionName = currentPosition.toString()
                    val newSharedElement = currentPosition?.let { rvImages.findViewHolderForAdapterPosition(it)?.itemView?.ivImage }
                    if (newSharedElement != null) {
                        names?.clear()
                        names?.add(newTransitionName)
                        sharedElements!!.clear()
                        sharedElements[newTransitionName] = newSharedElement
                    }
                }
                stateBundle = null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityReenter(requestCode: Int, data: Intent) {
        super.onActivityReenter(requestCode, data)
        stateBundle = Bundle(data.extras)
        val startingPosition = stateBundle?.getInt(Constants.START_ITEM_POSITION)
        val currentPosition = stateBundle?.getInt(Constants.CURRENT_ITEM_POSITION)
        if (startingPosition != currentPosition) {
            currentPosition?.let { rvImages.scrollToPosition(it) }
        }
        postponeEnterTransition()
        rvImages.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rvImages.viewTreeObserver.removeOnPreDrawListener(this)
                rvImages.requestLayout()
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun showLoading() {
        progressBar.show()
    }

    override fun dismissLoading() {
        progressBar.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPhotosPresenter.detachView()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        menu?.findItem(R.id.columnCount2)?.isChecked = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.columnCount2 -> {
                item.isChecked = true
                setRecyclerLayoutManager(2)
                return true
            }
            R.id.columnCount3 -> {
                item.isChecked = true
                setRecyclerLayoutManager(3)
                return true
            }
            R.id.columnCount4 -> {
                item.isChecked = true
                setRecyclerLayoutManager(4)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlSearch -> {
                onSearchClick()
            }
        }
    }

    override fun setApiResponse(photo: ArrayList<Photo>?) {
        pbLoader.visibility = View.GONE
        isLoading = false
        photosList.addAll(photo.orEmpty())
        roomList.addAll(photo.orEmpty())
        val size = photo?.size
        if (size != null && size > 0) {
            //Adding photo to the  db
            val photos = PhotoData(searchedText, roomList)
            appDatabase.userDao().insertAll(photos)
        } else
            Toast.makeText(this, getString(R.string.error_no_photos_found), Toast.LENGTH_SHORT).show()
        photosAdapter.notifyDataSetChanged()
    }

    override fun errorToast() {
        pbLoader.visibility = View.GONE
        isLoading = false
        Toast.makeText(this, getString(R.string.error_something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    override fun onSearchClick() {
        val intent = Intent(this, PastSearchActivity::class.java)
        startActivityForResult(intent, Constants.REQUEST_CODE_SEARCH_TEXT)
    }

    private fun checkDataInDB() {
        dbList = appDatabase.userDao().loadAllByIds(searchedText)
        if (dbList?.size == 0)
            Toast.makeText(this, getString(R.string.error_no_photos), Toast.LENGTH_SHORT).show()
        else {
            dbList?.get(0)?.images?.let { photosList.addAll(it) }
            //to continue adding data from start when offline
            dbList?.get(0)?.images?.let { roomList.addAll(it) }
            photosAdapter.notifyDataSetChanged()
        }
    }



    override fun onPhotoClick(position: Int, ivImage: ImageView) {
        val intent = Intent(this, ViewSinglePhotoActivity::class.java)
        intent.putExtra(Constants.START_ITEM_POSITION, position)
        intent.putParcelableArrayListExtra(Constants.LIST, photosList)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                ivImage,
                position.toString())
        startActivity(intent, options.toBundle())
    }



    override fun onBackPressed() {
        if (backPressed + Constants.BACK_DURATION > System.currentTimeMillis()) {
            this.finishAffinity()
        } else {
            Toast.makeText(this, getString(R.string.press_back), Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_SEARCH_TEXT && resultCode == Activity.RESULT_OK) {
            searchedText = data?.getStringExtra(Constants.SEARCHED_TEXT).toString()
            tvSearch.text = searchedText
            photosList.clear()
            roomList.clear()
            photosAdapter.notifyDataSetChanged()
            if (Utils.isConnectedToInternet(this)) {
                pageNo = 1
                viewPhotosPresenter.hitApiGetPhotos(true, pageNo, searchedText)
            } else
                checkDataInDB()
        }
    }
}
