package me.aleksandarzekovic.quizbox.ui.quizlistresults

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.aleksandarzekovic.quizbox.data.models.quizlistresults.QuizListResultsModel
import me.aleksandarzekovic.quizbox.databinding.ItemQuizListResultsBinding

class QuizListResultsAdapter :
    ListAdapter<QuizListResultsModel, QuizListResultsAdapter.ViewHolder>(QuizListResultsDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemQuizListResultsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemQuizListResultsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuizListResultsModel) {
            binding.model = item
            binding.executePendingBindings()
        }
    }
}

class QuizListResultsDiffCallback : DiffUtil.ItemCallback<QuizListResultsModel>() {

    override fun areItemsTheSame(
        oldItem: QuizListResultsModel,
        newItem: QuizListResultsModel
    ): Boolean {
        return oldItem.documentId == newItem.documentId
    }

    override fun areContentsTheSame(
        oldItem: QuizListResultsModel,
        newItem: QuizListResultsModel
    ): Boolean {
        return oldItem == newItem
    }
}