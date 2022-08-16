package com.reve.abroady.presentation.community.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemPartyCommentBinding
import com.reve.abroady.data.entity.party.PartyComment

class PartyCommentAdapter (
    private var activity : Activity,
    private var partyList : List<PartyComment>
) : RecyclerView.Adapter<PartyCommentAdapter.ViewHolder>() {

    private var onPartyClickListener: (() -> Unit)? = null

    fun setOnPartyClickListener(listener : (() -> Unit)) {
        this.onPartyClickListener = listener
    }

    inner class ViewHolder(private val binding : ItemPartyCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(comment : PartyComment) {
            /* binding.root.setOnClickListener {
                 onPartyClickListener?.invoke()
             } */
            binding.partyCommentUserName.text = comment.userName
            binding.partyCommentWrittenTime.text = comment.writtenTime
            binding.partyCommentContent.text = comment.content
            if (comment.isHaveRootComment) {
                activity.runOnUiThread {
                    setLeftMargin(binding.partyCommentUserProfileImage, 60)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyCommentAdapter.ViewHolder {
        val binding =
            ItemPartyCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartyCommentAdapter.ViewHolder, position: Int) {
        holder.bindView(partyList[position])
    }

    override fun getItemCount(): Int = partyList.size

    private fun setLeftMargin(view : View, left : Int) {
        val viewLayoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        viewLayoutParams.setMargins(viewLayoutParams.leftMargin + left, viewLayoutParams.topMargin, viewLayoutParams.rightMargin, viewLayoutParams.bottomMargin)
        view.layoutParams = viewLayoutParams
    }

}