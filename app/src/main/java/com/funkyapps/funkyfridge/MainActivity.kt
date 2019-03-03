package com.funkyapps.funkyfridge

import android.app.Activity
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
import android.graphics.Color
import android.opengl.Visibility
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.view.menu.MenuView
import android.view.MenuItem
import android.widget.*
import java.util.*
import kotlin.math.exp
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val addRequestCode : Int = 1
    lateinit var foodRepo : FoodItemRepository
    lateinit var myDataset : MutableList<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        foodRepo = FoodItemRepository(application)
        myDataset = foodRepo.getAllFoodItems()

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset,this, foodRepo)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_fridge_feed).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val fab: View = findViewById(R.id.fab_new_item)
        fab.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivityForResult(intent,addRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == addRequestCode && resultCode == Activity.RESULT_OK) {
            val prodUPCCode : String = data!!.getStringExtra("upc")
            val calories : Int = data?.getIntExtra("calories",-1) as Int
            val total_fat : Int = data?.getIntExtra("fat",-1)
            val total_carbs : Int = data?.getIntExtra("carbs",-1)
            val sugars : Int = data?.getIntExtra("sugars",-1)
            val protein : Int = data?.getIntExtra("protein",-1)
            val prodName : String = data.getStringExtra("name")
            //val expDate : String = data.getStringExtra("expDate")
            val foodItem : FoodItem = FoodItem(prodName,prodName,prodUPCCode,"3333-33-33",total_carbs,protein,sugars,total_fat,calories)
            foodRepo.insert(foodItem)
            viewAdapter.insert(foodItem)
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

    public fun update(item : FoodItem){
        myDataset.add(item)
        isExpanded.add(false)
        notifyDataSetChanged()
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
        //Gets date from text
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val convertedDate = sdf.parse(myDataset[holder.adapterPosition-1].expDate)
        //Gets current date
        val currentDate : Date = Calendar.getInstance().time
        //Gets date a week from now
        val calendar : Calendar = Calendar.getInstance()
        calendar.isLenient = true
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+7)
        val weekFromNow : Date = calendar.time

        var buttonText : String = myDataset[holder.adapterPosition-1].itemName
        var date : String = myDataset[holder.adapterPosition-1].expDate.substring(5,7) + "/" +
                myDataset[holder.adapterPosition-1].expDate.substring(8,10) + "/" + myDataset[holder.adapterPosition-1].expDate.substring(0,4)


        if(currentDate.after(convertedDate) || currentDate.equals(convertedDate)) {
            buttonText += " (expired on " + date + ")"
            holder.textView.setTextColor(Color.RED)
        }
        else if(weekFromNow.after(convertedDate) || weekFromNow.after(convertedDate)){
            buttonText += " (expires soon on " + date + ")"
            holder.textView.setTextColor(ResourcesCompat.getColor(context.resources,R.color.darkOrange,null))
        }
        else {
            buttonText += " (expires on " + date + ")"
            holder.textView.setTextColor(Color.BLACK)
        }

        if(isExpanded.size > 0 && isExpanded[holder.adapterPosition-1]){
            holder.proteinText.visibility = View.VISIBLE
            holder.sugarText.visibility = View.VISIBLE
            holder.carbText.visibility = View.VISIBLE
            holder.fatText.visibility = View.VISIBLE
            holder.caloriesText.visibility = View.VISIBLE
            holder.proteinText.text = if(myDataset[holder.adapterPosition-1].proteins<0) "Proteins: Unknown" else "Proteins: " + myDataset[holder.adapterPosition-1].proteins + "g"
            holder.carbText.text = if(myDataset[holder.adapterPosition-1].carbs<0) "Carbs: Unknown" else "Carbs: " + myDataset[holder.adapterPosition-1].carbs + "g"
            holder.sugarText.text = if(myDataset[holder.adapterPosition-1].sugars<0) "Sugars: Unknown" else "Sugars: " + myDataset[holder.adapterPosition-1].sugars + "g"
            holder.fatText.text =  if(myDataset[holder.adapterPosition-1].fats<0) "Fats: Unknown" else "Fats: " + myDataset[holder.adapterPosition-1].fats + "g"
            holder.caloriesText.text = if(myDataset[holder.adapterPosition-1].calories<0) "Calories: Unknown" else "Calories: " + myDataset[holder.adapterPosition-1].calories + "g"
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
                holder.proteinText.text = if(myDataset[holder.adapterPosition-1].proteins<0) "Proteins: Unknown" else "Proteins: " + myDataset[holder.adapterPosition-1].proteins + "g"
                holder.carbText.text = if(myDataset[holder.adapterPosition-1].carbs<0) "Carbs: Unknown" else "Carbs: " + myDataset[holder.adapterPosition-1].carbs + "g"
                holder.sugarText.text = if(myDataset[holder.adapterPosition-1].sugars<0) "Sugars: Unknown" else "Sugars: " + myDataset[holder.adapterPosition-1].sugars + "g"
                holder.fatText.text =  if(myDataset[holder.adapterPosition-1].fats<0) "Fats: Unknown" else "Fats: " + myDataset[holder.adapterPosition-1].fats + "g"
                holder.caloriesText.text = if(myDataset[holder.adapterPosition-1].calories<0) "Calories: Unknown" else "Calories: " + myDataset[holder.adapterPosition-1].calories + "g"
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

    fun insert(food : FoodItem){
        myDataset.add(food)
        isExpanded.add(false)
        notifyDataSetChanged()
    }

    // return size of dataset (invoked by layout manager)
    override fun getItemCount() = myDataset.size+1

}
