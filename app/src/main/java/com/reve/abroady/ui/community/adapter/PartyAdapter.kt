package com.reve.abroady.ui.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reve.abroady.databinding.ItemPartyBinding
import com.reve.abroady.model.data.Party

class PartyAdapter (
    private var partyList : List<Party>
) : RecyclerView.Adapter<PartyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding : ItemPartyBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bindView(party : Party) {
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