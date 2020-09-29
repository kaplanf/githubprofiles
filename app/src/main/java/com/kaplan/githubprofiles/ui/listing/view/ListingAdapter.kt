package com.kaplan.githubprofiles.ui.listing.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kaplan.githubprofiles.binding.BindableAdapter
import com.kaplan.githubprofiles.databinding.ListItemBinding
import com.kaplan.githubprofiles.ui.listing.data.ListingItem
import javax.inject.Inject

class ListingAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<ListingAdapter.ViewHolder>(), BindableAdapter<List<ListingItem>>{

    var listingItems = ArrayList<ListingItem>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = getItem(position)
        listItem?.let {
            holder.apply {
                bind(createOnClickListener(listItem.login), listItem)
                itemView.tag = listItem
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private fun createOnClickListener(id: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = ListingFragmentDirections.actionToDetailFragment(id)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(private val binding: ListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            listener: View.OnClickListener, item: ListingItem,
        ) {
            binding.apply {
                clickListener = listener
                listItem = item
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int = listingItems.size

    override fun setData(data: List<ListingItem>) {}

    override fun updateData(data: List<ListingItem>) {
        val diffCallback = ListingDiffCallback(listingItems, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listingItems.clear()
        this.listingItems.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int): ListingItem {
        return listingItems[position]
    }
}

private class ListingDiffCallback(private val oldList: List<ListingItem>,
                           private val newList: List<ListingItem>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int,
                                    newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }
}
