package com.view.imgurviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.view.imgurviewer.models.Image
import kotlinx.android.synthetic.main.fragment_image_preview.view.*

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Image>(){                   // using DiffUtil to calculate the differences between two lists, and to only update the difference.
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {         // compares contents of old image to new image of the same id
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)                              // async so runs in the background
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {    //recyclerview functions
        return ImageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.fragment_image_preview,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(image.link).into(ImgurImage)          // Glide library loads image from image record into the layout imageView
            ImageTitle.text = image.title
            setOnClickListener {
                onItemClickListener?.let { it(image) }      // refers to the onItemClickListener lambda function below
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Image) -> Unit)? = null

    fun setOnItemClickListener(listener: (Image) -> Unit){          // takes listener and Image to return a unit
        onItemClickListener = listener
    }
}