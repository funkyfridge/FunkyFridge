package com.funkyapps.funkyfridge

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.funkyapps.funkyfridge.R
import org.w3c.dom.Text
import android.content.Intent
import android.opengl.Visibility
import android.support.constraint.ConstraintLayout
import android.support.v7.view.menu.MenuView
import android.view.MenuItem
import android.widget.*
import java.util.*
import kotlin.math.exp
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foodRepo : FoodItemRepository = FoodItemRepository(application)
        var myDataset : MutableList<FoodItem> = foodRepo.getAllFoodItems()

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset,this, foodRepo)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_fridge_feed).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val fab: View = findViewById(R.id.fab_new_item)
        fab.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }
    }
}

class MyAdapter(private val myDataset: MutableList<FoodItem>, val context : Context, val repo : FoodItemRepository) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var isExpanded : MutableList<Boolean>

    open class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }
    class ItemViewHolder(itemView : View) : MyViewHolder(itemView){
        var button : ConstraintLayout = itemView.findViewById(R.id.dropdownLayout)
        var proteinText : TextView = itemView.findViewById(R.id.proteinText)
        var sugarText : TextView = itemView.findViewById(R.id.sugarsText)
        var carbText : TextView = itemView.findViewById(R.id.carbsText)
        var fatText : TextView = itemView.findViewById(R.id.fatsText)
        var caloriesText : TextView = itemView.findViewById(R.id.caloriesText)
        var textView : TextView = itemView.findViewById(R.id.dropdownTextview)
    }
    class TitleViewHolder(itemView : View) : MyViewHolder(itemView) {

    }

    init{
        isExpanded = ArrayList<Boolean>(myDataset.size+10)
        for(i in myDataset.indices){
            isExpanded.add(false)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return if (position == 0)
            0
        else
            1
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {

        if(viewType==1){
            val dropdownView = LayoutInflater.from(parent.context)
                .inflate(R.layout.dropdown_item, parent, false)
            return ItemViewHolder(dropdownView)
            }
        else{
            val headerView = LayoutInflater.from(parent.context)
                .inflate(R.layout.title_item, parent, false)
            return TitleViewHolder(headerView)
        }
    }

    // Replace contents of view, invoked by layout manager
    override fun onBindViewHolder(bigHolder: MyViewHolder, position: Int) {
        lateinit var holder : ItemViewHolder
        if(bigHolder.adapterPosition!=0)
            holder = bigHolder as ItemViewHolder
        else
            return

        var buttonText : String = myDataset[holder.adapterPosition-1].itemName
        var date : String = myDataset[holder.adapterPosition-1].expDate.substring(5,7) + "/" +
                myDataset[holder.adapterPosition-1].expDate.substring(8,10) + "/" + myDataset[holder.adapterPosition-1].expDate.substring(0,4)

        buttonText += " (expires on " + date + ")"

        if(isExpanded.size > 0 && isExpanded[holder.adapterPosition-1]){
            holder.proteinText.visibility = View.VISIBLE
            holder.sugarText.visibility = View.VISIBLE
            holder.carbText.visibility = View.VISIBLE
            holder.fatText.visibility = View.VISIBLE
            holder.caloriesText.visibility = View.VISIBLE
            holder.proteinText.text = "Proteins: " + myDataset[holder.adapterPosition-1].proteins
            holder.carbText.text = "Carbs: " + myDataset[holder.adapterPosition-1].carbs
            holder.sugarText.text = "Sugars: " + myDataset[holder.adapterPosition-1].sugars
            holder.fatText.text = "Fats: " + myDataset[holder.adapterPosition-1].fats
            holder.caloriesText.text = "Calories: " + myDataset[holder.adapterPosition-1].calories
        }

        holder.textView.text = buttonText

        holder.button.setOnClickListener {
            if(isExpanded.size > 0 && !isExpanded[holder.adapterPosition-1]){
                isExpanded[holder.adapterPosition-1] = true
                holder.proteinText.visibility = View.VISIBLE
                holder.sugarText.visibility = View.VISIBLE
                holder.carbText.visibility = View.VISIBLE
                holder.fatText.visibility = View.VISIBLE
                holder.caloriesText.visibility = View.VISIBLE
                holder.proteinText.text = "Proteins: " + myDataset[holder.adapterPosition-1].proteins
                holder.carbText.text = "Carbs: " + myDataset[holder.adapterPosition-1].carbs
                holder.sugarText.text = "Sugars: " + myDataset[holder.adapterPosition-1].sugars
                holder.fatText.text = "Fats: " + myDataset[holder.adapterPosition-1].fats
                holder.caloriesText.text = "Calories: " + myDataset[holder.adapterPosition-1].calories
            }
            else{
                if(isExpanded.size > 0)
                    isExpanded[holder.adapterPosition-1] = false
                holder.proteinText.visibility = View.GONE
                holder.sugarText.visibility = View.GONE
                holder.carbText.visibility = View.GONE
                holder.fatText.visibility = View.GONE
                holder.caloriesText.visibility = View.GONE
            }
        }

        holder.button.setOnLongClickListener(View.OnLongClickListener {
            //Creating the instance of PopupMenu
            val popup = PopupMenu(context, holder.button)
            //Inflating the Popup using xml file
            popup.menuInflater
                .inflate(R.menu.hold_button_menu, popup.menu)
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                val temp = item.title.toString()
                if (temp.compareTo("Edit Name") == 0) {
                    val input = EditText(context)
                    val alertBuilder = AlertDialog.Builder(context).setTitle("Enter Item").setMessage("Enter a new name for the item")
                        .setView(input).setPositiveButton("Ok") { dialog, whichButton -> }
                        .setNegativeButton(
                            "Cancel"
                        ) { dialog, which -> }

                    val alert = alertBuilder.create()
                    alert.setOnShowListener {
                        val b = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                        b.setOnClickListener(View.OnClickListener {
                            if (input.text.toString().trim { it <= ' ' }.isEmpty()) {
                                Toast.makeText(context, "Enter the name of your item", Toast.LENGTH_SHORT).show()
                                return@OnClickListener
                            }
                            else {
                                myDataset[holder.adapterPosition-1].itemName = input.text.toString()
                                notifyDataSetChanged()
                                alert.cancel()
                            }
                        })
                    }
                    alert.show()
                }
                else if (temp.compareTo("Edit Expiration Date")==0){
                    val c = Calendar.getInstance()
                    var year = c.get(Calendar.YEAR)
                    var month = c.get(Calendar.MONTH)
                    var day = c.get(Calendar.DAY_OF_MONTH)


                    val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view2, thisYear, thisMonth, thisDay ->
                        // Display Selected date in textbox
                        year = thisMonth + 1
                        month = thisDay
                        day = thisYear

                        val newDate:Calendar =Calendar.getInstance()
                        newDate.set(thisYear, thisMonth, thisDay)
                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        val currentDate = formatter.format(newDate.time)
                        myDataset[holder.adapterPosition-1].expDate = currentDate // setting new date
                        repo.update(myDataset[holder.adapterPosition-1])
                        notifyDataSetChanged()
                    }, year, month, day)
                    dpd.show()
                }
                else if (temp.compareTo("Delete") == 0) {
                    repo.delete(myDataset[holder.adapterPosition-1])
                    isExpanded.removeAt(holder.adapterPosition-1)
                    myDataset.removeAt(holder.adapterPosition-1)
                    notifyDataSetChanged()
                    return@OnMenuItemClickListener true
                }
                false
            })
            popup.show()
            true
        })
    }

    // return size of dataset (invoked by layout manager)
    override fun getItemCount() = myDataset.size+1

}
