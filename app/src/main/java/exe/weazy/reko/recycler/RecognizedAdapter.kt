package exe.weazy.reko.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import exe.weazy.reko.R
import exe.weazy.reko.model.Recognized
import exe.weazy.reko.util.extensions.toTitleCase
import org.ocpsoft.prettytime.PrettyTime

class RecognizedAdapter(
    private val recognized: MutableList<Recognized>,
    private val onItemClick: (Recognized) -> Unit
): RecyclerView.Adapter<RecognizedAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.view_recognized, parent, false))
    }

    override fun getItemCount() = recognized.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(recognized[position])
    }

    fun update(recognized: List<Recognized>) {
        this.recognized.clear()
        this.recognized.addAll(recognized)
        notifyDataSetChanged()
    }

    inner class Holder(view: View): RecyclerView.ViewHolder(view) {

        private var imageView = view.findViewById<ImageView>(R.id.imageView)
        private var emotionTextView = view.findViewById<TextView>(R.id.emotionTextView)
        private var dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        private var root = view.findViewById<CardView>(R.id.rootViewRecognizedItem)

        fun bind(recognized: Recognized) {
            emotionTextView.text = recognized.emotions.maxBy { it.value }?.key?.toTitleCase()
            dateTextView.text = PrettyTime().format(recognized.date)

            Glide.with(imageView.context)
                .load(recognized.image)
                .centerCrop()
                .into(imageView)

            root.setOnClickListener { onItemClick(recognized) }
        }
    }
}