package com.reve.abroady.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemOnePostPerBoardBinding
import com.reve.abroady.data.entity.post.OnePostPerBoard

class OnePostPerBoardAdapter (
    private var postList : List<OnePostPerBoard>
) : RecyclerView.Adapter<OnePostPerBoardAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemOnePostPerBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(post : OnePostPerBoard) {
            binding.boardName.text = post.boardName
            binding.postTitle.text = post.postTitle
            binding.postContent.text = post.postContent
            binding.placeName.text = post.locationName
            binding.placeLocation.text = post.locationAddress
            binding.userWrittenDate.text = post.userName + " | " + post.writtenTime
            binding.likeData.text = post.like.toString()
            binding.commentData.text = post.comment.toString()
            binding.bookmarkData.text = post.bookmark.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnePostPerBoardAdapter.ViewHolder {
        val binding =
            ItemOnePostPerBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnePostPerBoardAdapter.ViewHolder, position: Int) {
        holder.bindView(postList[position])
    }

    override fun getItemCount(): Int = postList.size

}