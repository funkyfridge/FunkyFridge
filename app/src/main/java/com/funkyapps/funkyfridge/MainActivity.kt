package com.funkyapps.funkyfridge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.funkyapps.funkyfridge.R
import org.w3c.dom.Text
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_fridge_feed).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
*/
        val fab: View = findViewById(R.id.fab_new_item)
        fab.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }
    }
}
/*
class MyAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {

        // creates new view, inflated from my_text_view
        // TODO: inflate all views of layout
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false) as TextView

        //TODO: set view's size, margins, etc

        return MyViewHolder(textView)
    }

    // Replace contents of view, invoked by layout manager
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // TODO: get element from dataset
        // TODO: replace contents of view with element

        holder.textView.text = myDataset[position]
    }

    // return size of dataset (invoked by layout manager)
    override fun getItemCount() = myDataset.size
    }
}
*/