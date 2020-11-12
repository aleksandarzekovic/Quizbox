package me.aleksandarzekovic.quizbox.utils.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import me.aleksandarzekovic.quizbox.BR

class RecyclerViewAdapter<T>(
    @LayoutRes val resource: Int,
    var data: List<T>,
    val listener: Any? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflator, resource, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerViewAdapter<*>.MyViewHolder) {
            val item = data[position]
            if (item != null)
                holder.setupData(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setupData(model: Any) {
            binding.setVariable(BR.model, model)
            if (listener != null) {
                binding.setVariable(BR.listener, listener)
            }
        }
    }

    fun updateData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }


}