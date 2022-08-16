package com.reve.abroady.presentation.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reve.abroady.databinding.ItemAllBoardBinding
import com.reve.abroady.data.entity.post.OnePostGetModel

class BoardPostAdapter(
    private val context : Context,
) : RecyclerView.Adapter<BoardPostAdapter.ViewHolder>() {

    private var _postList : ArrayList<OnePostGetModel> = ArrayList()

    inner class ViewHolder(private val binding: ItemAllBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(postGetModel: OnePostGetModel) {
            binding.boardName.text = "Random"
            if (postGetModel.isAnonymous)
                binding.userName.text = "Anonymous"
            else binding.userName.text = "Terry"
            binding.writtenDate.text = "07/17"
            binding.postTitle.text = postGetModel.title
            postGetModel.image?.let { // 추후 로딩 이미지 등 설정 필요
                binding.postImage.visibility = View.VISIBLE
                Glide.with(context)
                    .load(postGetModel.image)
                    .into(binding.postImage)
            }
            binding.postContent.text = postGetModel.content
            binding.imageCount.text = "1"
            binding.likeData.text = "5"
            binding.commentData.text = "10"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardPostAdapter.ViewHolder {
        val binding =
            ItemAllBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardPostAdapter.ViewHolder, position: Int) {
        holder.bindView(_postList[position])
    }

    override fun getItemCount(): Int = _postList.size

    fun update(newPostList: List<OnePostGetModel>) {
        val diffResult =
            DiffUtil.calculateDiff(ContentDiffUtil(_postList, newPostList), false)
        diffResult.dispatchUpdatesTo(this)
        _postList.clear()
        _postList.addAll(newPostList)
    }

    class ContentDiffUtil(
        private val oldList: List<OnePostGetModel>,
        private val currentList: List<OnePostGetModel>
    ) : DiffUtil.Callback() {

        //1. 아이템의 고유 id 값이 같은지
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].postId == currentList[newItemPosition].postId
        }

        //2. id 가 같다면, 내용물도 같은지 확인 equals()
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == currentList[newItemPosition]
        }

        //변화하기 전 데이터셋 사이즈
        override fun getOldListSize(): Int = oldList.size

        //변화한 후 데이터셋 사이즈
        override fun getNewListSize(): Int = currentList.size
    }

}