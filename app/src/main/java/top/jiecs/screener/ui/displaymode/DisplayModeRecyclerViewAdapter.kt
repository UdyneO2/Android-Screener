package top.jiecs.screener.ui.displaymode

import DisplayModeDiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import top.jiecs.screener.databinding.FragmentDisplayModeItemBinding
import top.jiecs.screener.ui.displaymode.DisplayModeViewModel.DisplayMode

/**
 * [RecyclerView.Adapter] that can display a [DisplayMode].
 */
class DisplayModeRecyclerViewAdapter(
    private val displayModeViewModel: DisplayModeViewModel,
    private val fragmentScope: CoroutineScope
) : RecyclerView.Adapter<DisplayModeRecyclerViewAdapter.ViewHolder>() {

    private val list: MutableList<DisplayMode> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentDisplayModeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val displayMode = list[position]
        holder.displayModeText.text = displayMode.toString()

        holder.editButton.setOnClickListener {
        }
        holder.removeButton.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            displayModeViewModel.list.value?.let {
                if (currentPosition > it.size - 1) return@setOnClickListener
                it.removeAt(currentPosition)
                fragmentScope.launch {
                    DataStoreManager().save(it)
                }
            }
        }
    }

    inner class ViewHolder(binding: FragmentDisplayModeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val displayModeText: TextView = binding.displayModeText
        val editButton: Button = binding.editButton
        val removeButton: Button = binding.removeButton
    }

    fun updateList(newList: MutableList<DisplayMode>) {
        val diffCallback = DisplayModeDiffCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback, false)

        this.list.clear()
        this.list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}