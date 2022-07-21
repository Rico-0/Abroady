package com.reve.abroady.ui.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemNowTrendingBinding
import com.reve.abroady.model.data.post.NowTrending

class NowTrendingAdapter (
    private val context : Context,
    private var nowTrendingList : List<NowTrending>
) : RecyclerView.Adapter<NowTrendingAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemNowTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(nowTrending : NowTrending) {
            binding.boardTitle.text = nowTrending.boardName
            // https://blog.yena.io/studynote/2020/06/10/Android-Glide.html -> glide 추가 작업 필요
            binding.nowTrendingUserName.text = nowTrending.userName
            binding.nowTrendingWrittenTime.text = nowTrending.writtenTime
            binding.nowTrendingTitle.text = nowTrending.title
            binding.nowTrendingContent.text = nowTrending.content
            binding.nowTrendingLikeData.text = nowTrending.like.toString()
            binding.nowTrendingCommentData.text = nowTrending.comment.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowTrendingAdapter.ViewHolder {
        val binding =
            ItemNowTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NowTrendingAdapter.ViewHolder, position: Int) {
        holder.bindView(nowTrendingList[position])
    }

    override fun getItemCount(): Int = nowTrendingList.size

}