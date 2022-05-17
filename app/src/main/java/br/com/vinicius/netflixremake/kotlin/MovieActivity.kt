package br.com.vinicius.netflixremake.kotlin

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.vinicius.netflixremake.R
import br.com.vinicius.netflixremake.model.Movie
import br.com.vinicius.netflixremake.util.ImageDownloaderTask
import br.com.vinicius.netflixremake.util.MovieDetailTask
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_movie.view.*

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        intent.extras?.let {
            val id = it.getInt("id")
            val task = MovieDetailTask(this)
            task.setMovieDetailLoader {

            }
            task.execute("https://tiagoaguiar.co/api/netflix/$id")

            setSupportActionBar(toolbar)

            supportActionBar?.let { toolbar ->
                toolbar.setDisplayHomeAsUpEnabled(true)
                toolbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
                toolbar.title = null
            }
        }
    }

    private inner class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
            MovieHolder(
                layoutInflater.inflate(R.layout.movie_item_similar, parent, false)
            )

        override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position])

        override fun getItemCount(): Int = movies.size

    }

    private class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                ImageDownloaderTask(image_view_cover_movie).execute(movie.coverUrl)
            }
        }
    }
}