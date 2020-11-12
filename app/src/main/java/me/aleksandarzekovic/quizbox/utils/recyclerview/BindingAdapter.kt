package me.aleksandarzekovic.quizbox.utils.recyclerview

import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.aleksandarzekovic.quizbox.R

@BindingAdapter(value = ["data", "itemList", "itemListener"], requireAll = false)
fun <T> setAdapter(
    recyclerView: RecyclerView,
    data: MutableLiveData<List<T>>,
    @LayoutRes itemList: Int,
    itemListener: Any
) {
    if (recyclerView.adapter == null) {
        recyclerView.adapter =
            RecyclerViewAdapter(
                itemList,
                data.value ?: listOf(),
                itemListener
            )
    } else {
        if (recyclerView.adapter is RecyclerViewAdapter<*>) {
            val items = data.value ?: listOf()
            (recyclerView.adapter as RecyclerViewAdapter<T>).updateData(items)
        }
    }
}

@BindingAdapter("image")
fun loadImage(imageView: ImageView, imageURL: String) {
    Glide.with(imageView.context)
        .load(imageURL)
        .placeholder(R.drawable.back_image)
        .into(imageView)
}