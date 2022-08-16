package com.reve.abroady.presentation.community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemBoardEditBinding
import com.reve.abroady.data.entity.board.BoardEdit

class BoardsEditAdapter (
    private var boardList: List<BoardEdit>
) : RecyclerView.Adapter<BoardsEditAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBoardEditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(board: BoardEdit) {
            binding.boardTitle.text = board.boardTitle
            if (board.isNewPostWritten) binding.boardNewPostIcon.visibility = View.VISIBLE
            binding.boardDescription.text = board.boardDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardsEditAdapter.ViewHolder {
        val binding =
            ItemBoardEditBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardsEditAdapter.ViewHolder, position: Int) {
        holder.bindView(boardList[position])
    }

    override fun getItemCount(): Int = boardList.size

}