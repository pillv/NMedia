import ru.netology.nmedia.repository.PostRepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type

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


class PostRepositoryFiles(private val context: Context) : PostRepository {


    private val gson = Gson()
    private val postsType: Type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"

    private val data = MutableLiveData<List<Post>>()
    @Volatile
    private var posts: List<Post> = emptyList()
        set(value) {
            field = value
            data.postValue(field)
            writeToFileSafely(field)
        }

    @Volatile
    private var nextId: Long = 1L

    init {
        posts = readFromFile()
        nextId = (posts.maxOfOrNull { it.id } ?: 0L) + 1L
        data.value = posts
    }

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map { post ->
            if (post.id != id) post
            else {
                val liked = !post.likeByMe
                post.copy(
                    likeByMe = liked,
                    likes = if (liked) post.likes + 1 else (post.likes - 1).coerceAtLeast(0)
                )
            }
        }
    }

    override fun repost(id: Long) {
        posts = posts.map { post ->
            if (post.id != id) post
            else post.copy(
                repostByMe = !post.repostByMe,
                reposts = post.reposts + 1
            )
        }
    }

    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newPost = post.copy(
                id = nextId++,
                author = "Me",
                published = "Now"
            )
            posts = listOf(newPost) + posts
        } else {
            posts = posts.map { if (it.id == post.id) post else it }
        }
    }

    override fun cancelEdit(post: Post) {
    }


    private fun readFromFile(): List<Post> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return emptyList()

        return try {
            FileReader(file).use { reader ->
                gson.fromJson(reader, postsType) ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun writeToFileSafely(posts: List<Post>) {
        val dir = context.filesDir
        val tmpFile = File(dir, "$fileName.tmp")
        val targetFile = File(dir, fileName)


        try {
            FileWriter(tmpFile).use { writer ->
                gson.toJson(posts, postsType, writer)
            }
            if (!tmpFile.renameTo(targetFile)) {
                FileWriter(targetFile).use { writer ->
                    gson.toJson(posts, postsType, writer)
                }
                if (tmpFile.exists()) tmpFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (tmpFile.exists()) tmpFile.delete()
        }
    }
}
