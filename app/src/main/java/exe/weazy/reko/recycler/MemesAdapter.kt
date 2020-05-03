package exe.weazy.reko.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import exe.weazy.reko.R
import exe.weazy.reko.model.Meme

class MemesAdapter(
    private var memes: List<Meme>,
    private val onFavoriteClick: (meme: Meme) -> Unit,
    private val onShareClick: (meme: Meme) -> Unit,
    private val onItemClick: (meme: Meme) -> Unit
) : RecyclerView.Adapter<MemesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_meme, parent, false)
        return Holder(view)
    }

    override fun getItemCount() = memes.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(meme = memes[position])
    }

    fun updateMemes(memes: List<Meme>) {
        val diffResult = DiffUtil.calculateDiff(MemesDiffUtils(this.memes, memes))
        diffResult.dispatchUpdatesTo(this)
        this.memes = memes.toList()
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view) {

        private var memeImageView = view.findViewById<ImageView>(R.id.memeImageView)
        private var memeTitleTextView = view.findViewById<TextView>(R.id.memeTitleTextView)
        private var favoriteButton = view.findViewById<ImageButton>(R.id.favoriteButton)
        private var shareButton = view.findViewById<ImageButton>(R.id.shareButton)

        fun bind(meme: Meme) {
            initListeners(meme)

            Glide
                .with(memeImageView)
                .load(meme.photoUrl)
                .into(memeImageView)

            memeTitleTextView.text = meme.title

            val favoriteDrawable = if (meme.isFavorite) {
                R.drawable.ic_favorite_filled_blue_24dp
            } else {
                R.drawable.ic_favorite_border_white_24dp
            }

            favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(favoriteButton.context, favoriteDrawable)
            )
        }

        private fun initListeners(meme: Meme) {
            (memeImageView.parent as View).setOnClickListener {
                onItemClick(meme)
            }

            favoriteButton.setOnClickListener {
                onFavoriteClick(meme)
            }

            shareButton.setOnClickListener {
                onShareClick(meme)
            }
        }
    }
}