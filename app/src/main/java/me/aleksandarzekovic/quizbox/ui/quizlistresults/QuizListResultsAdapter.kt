package me.aleksandarzekovic.quizbox.ui.quizlistresults

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.aleksandarzekovic.quizbox.R
import me.aleksandarzekovic.quizbox.data.database.quiz_result.QuizResultDB
import me.aleksandarzekovic.quizbox.databinding.ItemQuizListResultsBinding

class QuizListResultsAdapter :
    ListAdapter<QuizResultDB, QuizListResultsAdapter.ViewHolder>(QuizListResultsDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val itemFormatted = holder.itemView.context.getString(
            R.string.best_score_results,
            item.quizName,
            item.correctAnswers,
            item.totalAnswers
        )
        holder.bind(itemFormatted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemQuizListResultsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemQuizListResultsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.bestResults = item
            binding.executePendingBindings()
        }
    }
}

class QuizListResultsDiffCallback : DiffUtil.ItemCallback<QuizResultDB>() {

    override fun areItemsTheSame(
        oldItem: QuizResultDB,
        newItem: QuizResultDB
    ): Boolean {
        return oldItem.documentId == newItem.documentId
    }

    override fun areContentsTheSame(
        oldItem: QuizResultDB,
        newItem: QuizResultDB
    ): Boolean {
        return oldItem == newItem
    }
}