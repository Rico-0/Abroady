package com.reve.abroady.ui.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemHotBoardBinding
import com.reve.abroady.model.data.post.HotBoard

class HotBoardAdapter (
    private var hotBoardList : List<HotBoard>
) : RecyclerView.Adapter<HotBoardAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemHotBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(hotBoard : HotBoard) {
            binding.hotBoardPostTitle.text = hotBoard.title
            binding.hotBoardTitle.text = hotBoard.boardName
            binding.hotBoardWrittenDate.text = hotBoard.writtenDate
            binding.hotBoardLikeData.text = hotBoard.like.toString()
            binding.hotBoardCommentData.text = hotBoard.comment.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotBoardAdapter.ViewHolder {
        val binding =
            ItemHotBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotBoardAdapter.ViewHolder, position: Int) {
        holder.bindView(hotBoardList[position])
    }

    override fun getItemCount(): Int = hotBoardList.size

}