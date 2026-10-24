package com.example.gamerent.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamerent.R
import com.example.gamerent.domain.model.VideoGame

class VideoGameAdapter(
    private val onGameClick: (VideoGame) -> Unit,
    private val onEditClick: (VideoGame) -> Unit,
    private val onDeleteClick: (VideoGame) -> Unit
) : ListAdapter<VideoGame, VideoGameAdapter.GameViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) = holder.bind(getItem(position))

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.image_game)
        private val name = itemView.findViewById<TextView>(R.id.text_game_name)
        private val details = itemView.findViewById<TextView>(R.id.text_game_details)
        private val status = itemView.findViewById<TextView>(R.id.text_game_status)
        private val price = itemView.findViewById<TextView>(R.id.text_game_price)
        private val mediaLabel = itemView.findViewById<TextView>(R.id.text_game_media)

        fun bind(game: VideoGame) {
            name.text = game.nombre
            details.text = itemView.context.getString(R.string.game_console_genre, game.consola, game.genero)
            status.setText(if (game.disponible) R.string.available else R.string.unavailable)
            price.text = itemView.context.getString(
                R.string.video_price_per_day,
                String.format(java.util.Locale.getDefault(), "%.2f", game.precioAlquiler)
            )
            if (game.mediaUri != null && game.mediaType == "IMAGE") {
                image.setImageURI(Uri.parse(game.mediaUri))
                image.visibility = View.VISIBLE
            } else {
                image.setImageDrawable(null)
                image.visibility = View.GONE
            }
            mediaLabel.visibility = if (game.mediaType == "VIDEO") View.VISIBLE else View.GONE
            itemView.setOnClickListener { onGameClick(game) }
            itemView.findViewById<Button>(R.id.button_edit_game).setOnClickListener { onEditClick(game) }
            itemView.findViewById<Button>(R.id.button_delete_game).setOnClickListener { onDeleteClick(game) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoGame>() {
            override fun areItemsTheSame(oldItem: VideoGame, newItem: VideoGame) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: VideoGame, newItem: VideoGame) = oldItem == newItem
        }
    }
}
