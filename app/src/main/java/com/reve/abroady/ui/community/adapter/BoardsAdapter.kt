package com.reve.abroady.ui.community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemMyBoardsRecommendedBinding
import com.reve.abroady.model.data.board.BoardInfo

class BoardsAdapter (
    private var boardInfoList: List<BoardInfo>
) : RecyclerView.Adapter<BoardsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMyBoardsRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(boardInfo: BoardInfo) {
            binding.boardTitle.text = boardInfo.boardTitle
            if (boardInfo.isNewPostWritten) binding.boardNewPostIcon.visibility = View.VISIBLE
            binding.boardDescription.text = boardInfo.boardDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardsAdapter.ViewHolder {
        val binding =
            ItemMyBoardsRecommendedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardsAdapter.ViewHolder, position: Int) {
        holder.bindView(boardInfoList[position])
    }

    override fun getItemCount(): Int = boardInfoList.size

}