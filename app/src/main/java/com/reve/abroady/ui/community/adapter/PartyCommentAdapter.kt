package com.reve.abroady.ui.community.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.R
import com.reve.abroady.databinding.ItemPartyCommentBinding
import com.reve.abroady.model.data.party.PartyComment

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