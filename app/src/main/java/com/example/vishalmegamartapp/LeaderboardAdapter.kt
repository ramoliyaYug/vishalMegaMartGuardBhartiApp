package com.example.vishalmegamartapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vishalmegamartapp.databinding.ItemLeaderboardBinding

class LeaderboardAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemLeaderboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, position + 1)
    }

    override fun getItemCount(): Int = userList.size

    class LeaderboardViewHolder(private val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, rank: Int) {
            binding.tvRank.text = rank.toString()
            binding.tvName.text = user.name
            binding.tvScore.text = "${user.quizScore}/${user.totalQuestions}"
        }
    }
}
