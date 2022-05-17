package br.com.vinicius.netflixremake.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.vinicius.netflixremake.R
import br.com.vinicius.netflixremake.model.Category
import br.com.vinicius.netflixremake.model.Movie
import br.com.vinicius.netflixremake.util.CategoryTask
import br.com.vinicius.netflixremake.util.ImageDownloaderTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie.view.image_view_cover_movie
import kotlinx.android.synthetic.main.activity_movie.view.text_view_title
import kotlinx.android.synthetic.main.category_item.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = arrayListOf<Category>()
        mainAdapter = MainAdapter(categories)
        rv_main.adapter = mainAdapter

        rv_main.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader {
            mainAdapter.categories.clear()
            mainAdapter.categories.addAll(it)
            mainAdapter.notifyDataSetChanged()
        }
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(val categories: MutableList<Category>) : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
            return CategoryHolder(layoutInflater.inflate(R.layout.category_item, parent, false))
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int = categories.size

    }

    private inner class MovieAdapter(val movies: List<Movie>, private val listener: ((Movie) -> Unit)?) : RecyclerView.Adapter<MovieHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(layoutInflater.inflate(R.layout.movie_item, parent, false),
            listener
            )
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies[position]
            holder.bind(movie)
        }

        override fun getItemCount(): Int = movies.size

    }

    private inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) = with(itemView) {
            text_view_title.text = category.name
            rv_movie.adapter = MovieAdapter(category.movies) { movie ->
                if (movie.id > 3) {
                    Toast.makeText(this@MainActivity,
                    "Não foi implementado essa funcionalidade",
                    Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this@MainActivity, MovieActivity::class.java)
                    intent.putExtra("id", movie.id)
                    startActivity(intent)
                }
            }
            rv_movie.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
        }
    }

    private class MovieHolder(itemView: View, val onClick: ((Movie) -> Unit)?) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) = with(itemView) {
            ImageDownloaderTask(image_view_cover_movie)
                .execute(movie.coverUrl)
            image_view_cover_movie.setOnClickListener {
                onClick?.invoke(movie)
            }
        }
    }
}