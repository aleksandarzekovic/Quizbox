package me.aleksandarzekovic.quizbox.ui.quizmenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.aleksandarzekovic.quizbox.data.database.quizmenu.QuizTypeDB
import me.aleksandarzekovic.quizbox.databinding.ItemQuizTypeBinding

class QuizMenuAdapter(var quizMenuClickListener: QuizMenuClickListener) :
    ListAdapter<QuizTypeDB, QuizMenuAdapter.ViewHolder>(QuizMenuDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    interface QuizMenuClickListener {
        fun onItemClick(f: QuizTypeDB)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemQuizTypeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemQuizTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizTypeDB) {
            binding.model = item
            binding.listener = quizMenuClickListener
            binding.executePendingBindings()
        }
    }
}

class QuizMenuDiffCallback : DiffUtil.ItemCallback<QuizTypeDB>() {

    override fun areItemsTheSame(oldItem: QuizTypeDB, newItem: QuizTypeDB): Boolean {
        return oldItem.quizId == newItem.quizId
    }

    override fun areContentsTheSame(oldItem: QuizTypeDB, newItem: QuizTypeDB): Boolean {
        return oldItem == newItem
    }
}