package eltex.test.presentation.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eltex.test.databinding.UserPermissionItemBinding

class UserPermissionsAdapter : ListAdapter<UserPermissionsAdapter.Item, UserPermissionsAdapter.UserPermissionViewHolder>(
    object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == oldItem
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserPermissionViewHolder(
        UserPermissionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: UserPermissionViewHolder, position: Int) = holder.bind(getItem(position))

    class UserPermissionViewHolder(private val binding: UserPermissionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) = with(binding) {
            permissionTitleTextView.text = item.permissionTitle
        }
    }

    data class Item(
        val id: Int,
        val permissionTitle: String
    )
}