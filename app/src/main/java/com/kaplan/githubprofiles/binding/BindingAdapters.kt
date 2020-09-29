package com.kaplan.githubprofiles.binding

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kaplan.githubprofiles.util.RecyclerViewScrollCallback
import com.kaplan.githubprofiles.util.then

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: FloatingActionButton, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.hide()
    } else {
        view.show()
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

interface BindableAdapter<T> {
    fun setData(data: T)
    fun updateData(data: T)
}

@BindingAdapter("update")
fun <T> recyclerviewDiff(recyclerView: RecyclerView, data: T) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        if (data != null)
            (recyclerView.adapter as BindableAdapter<T>).updateData(data)
    }
}

@BindingAdapter("textOrNot")
fun bindText(view: TextView, textValue: String?) {
    var text = textValue.isNullOrEmpty().not() then textValue.toString() ?: "No Information"
    text = view.tag.toString() + ": " + text
    view.text = text
}


@BindingAdapter("renderHtml")
fun bindRenderHtml(view: TextView, description: String?) {
    if (description != null) {
        view.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        view.movementMethod = LinkMovementMethod.getInstance()
    } else {
        view.text = ""
    }
}

@BindingAdapter(value = ["visibleThreshold", "resetLoadingState", "onScrolledToBottom"],
    requireAll = false)
fun setRecyclerViewScrollCallback(recyclerView: RecyclerView, visibleThreshold: Int,
                                  resetLoadingState: Boolean,
                                  onScrolledListener: RecyclerViewScrollCallback.OnScrolledListener) {
    val callback = recyclerView.layoutManager?.let {
        RecyclerViewScrollCallback
            .Builder(it)
            .visibleThreshold(visibleThreshold)
            .onScrolledListener(onScrolledListener)
            .resetLoadingState(resetLoadingState)
            .build()
    }

    recyclerView.clearOnScrollListeners()
    callback?.let { recyclerView.addOnScrollListener(it) }
}

