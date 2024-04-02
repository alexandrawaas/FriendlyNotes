package com.example.friendlynotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.friendlynotes.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter:MainAdapter
    lateinit var repository: Repository
    lateinit var binding:ActivityMainBinding

    companion object
    {
        private var query:String?=null
    }

    private var getContentLauncherAdd=registerForActivityResult(EditFriendContract())
    { result:Friend? ->
        if(result!=null)
        {
            repository.addFriend(result)
            adapter.updateRecyclerView()

            val gson = Gson()
            val intentRefresh: Intent =
                Intent(this, ShowFriendActivity::class.java)
            intentRefresh.putExtra("friend", gson.toJson(result))
            startActivity(intentRefresh)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater);

        setContentView(binding.root)
        binding.topAppBar.menu

        setSupportActionBar(binding.topAppBar)

        repository = LocalRepository(this)

        adapter=MainAdapter(repository)

        recyclerView = binding.recyclerView
        recyclerView.adapter=adapter

        recyclerView.layoutManager=LinearLayoutManager(this).apply{orientation=LinearLayoutManager.VERTICAL}

        binding.floatingActionButtonAdd.setOnClickListener {
            getContentLauncherAdd.launch(null)
        }

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {

                val friend = adapter.listFiltered.get(position)
                val gson = Gson()
                val friendString : String = gson.toJson(friend)

                val intent = Intent(this@MainActivity, ShowFriendActivity::class.java).apply {
                    putExtra("friend", friendString)
                    putExtra("position", position)
                }
                startActivity(intent)
            }
        })
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)

        menu?.findItem(R.id.imprint)?.setOnMenuItemClickListener{ menuItem ->
            val intent: Intent = Intent(this, ImprintActivity::class.java)
            startActivity(intent)
            true
        }

        val searchItem = menu?.findItem(R.id.search)
        val searchView : SearchView = searchItem?.actionView as? SearchView ?: return false
        searchView.isIconified = false

        searchView.setOnCloseListener {
            searchView.isIconified = false
            searchView.setQuery("", true)
            true
        }

        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                searchView.requestFocusFromTouch()
                searchView.isIconified = false
                searchView.setQuery("", true)
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                searchView.setQuery("", true)
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(searchView.windowToken, 0)
                return true
            }
        })

        searchView.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                query=newText
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()

        adapter.updateRecyclerView()
        adapter.filter(MainActivity.Companion.query)
    }
}