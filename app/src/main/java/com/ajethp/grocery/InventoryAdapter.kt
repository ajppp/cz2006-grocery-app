package com.ajethp.grocery

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajethp.grocery.classes.Food

class InventoryAdapter(
    private val context: Context,
    private val userInventory: List<Food>,
    val itemClick: (Int) -> Unit) :
        RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

        companion object {
            private const val TAG = "InventoryAdapter"
        }

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                // TODO("the expiry date text should change color as it gets closer to expiry date")
                private val nameText = itemView.findViewById<TextView>(R.id.inventoryFoodItem)
                private val quantityText = itemView.findViewById<TextView>(R.id.inventoryQuantity)
                private val expiryDateText = itemView.findViewById<TextView>(R.id.inventoryExpiryDate)
                private val minusButton = itemView.findViewById<ImageView>(R.id.minusButton)

                fun bind(position: Int) {

                    // set text for the inventory items
                    nameText.text = userInventory[position].foodName
                    quantityText.text = userInventory[position].quantity.toString()
                    expiryDateText.text = userInventory[position].expiryDate

                    // handles what happens when the minus button for a particular item is clicked
                    // the function that is passed in to this class is then called
                    // in this case, the function is in Inventory
                    minusButton.setOnClickListener {
                        itemClick(position)
                    }

                    // can set more onClickListener here in case anything else in the inventory is clickable
                    // set onLongClickListener to delete item
                    // long hold to delete button
                    quantityText.setOnLongClickListener{
                        // TODO("delete item on long hold")
                        Log.i(TAG, "long click")
                        true
                    }
                }
            }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = userInventory.size

}
