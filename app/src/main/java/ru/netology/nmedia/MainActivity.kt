package ru.netology.nmedia

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

fun formatNumber(number: Int): String {
    return when {
        number < 1000 -> number.toString()
        number < 10000 -> {
            val thousands = number / 1000
            val hundreds = (number % 1000) / 100
            "$thousands.$hundreds" + "к"
        }
        number < 1000000 -> {
            (number / 1000).toString() + "K"
        }
        else -> {
            val millions = number / 1000000
            val hundredThousands = (number % 1000000) / 100000
            "$millions.$hundredThousands" + "M"
        }
    }
}


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 999,
            reposts = 1199,
            views = 7,
        )
        fun updateUI(currentPost: Post) {
            with(binding) {
                author.text = currentPost.author
                published.text = currentPost.published
                content.text = currentPost.content

                favorite.setImageResource(
                    if (currentPost.likeByMe) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24
                )
                likes.text = formatNumber(currentPost.likes)
                reposts.text = formatNumber(currentPost.reposts)
                views.text = formatNumber(currentPost.views)
            }
        }

        updateUI(post)

        with(binding) {
            root.setOnClickListener {
                Log.d("stuff", "stuff")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            favorite.setOnClickListener {
                Log.d("stuff", "like")
                post.likeByMe = !post.likeByMe // Переключаем состояние лайка

                if (post.likeByMe) {
                    post.likes++
                } else {
                    post.likes--
                }
                updateUI(post)
            }

            arrow.setOnClickListener {
                Log.d("stuff", "repost")
                post.reposts++
                updateUI(post)
            }
        }
    }
}