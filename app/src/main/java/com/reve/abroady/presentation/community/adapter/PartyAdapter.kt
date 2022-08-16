package com.reve.abroady.presentation.community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemPartyBinding
import com.reve.abroady.data.entity.party.Party

class PartyAdapter (
    private var partyList : List<Party>
) : RecyclerView.Adapter<PartyAdapter.ViewHolder>() {

    private var onPartyClickListener: (() -> Unit)? = null

    fun setOnPartyClickListener(listener : (() -> Unit)) {
        this.onPartyClickListener = listener
    }

    inner class ViewHolder(private val binding : ItemPartyBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bindView(party : Party) {
           /* binding.root.setOnClickListener {
                onPartyClickListener?.invoke()
            } */
            binding.partyTime.text = party.time
            binding.partyLocation.text = party.location
            binding.partyContent.text = party.content
            binding.partyPlace.text = party.restaurant
            binding.partyPeople.text = party.people
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyAdapter.ViewHolder {
        val binding =
            ItemPartyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartyAdapter.ViewHolder, position: Int) {
        holder.bindView(partyList[position])
    }

    override fun getItemCount(): Int = partyList.size

}
