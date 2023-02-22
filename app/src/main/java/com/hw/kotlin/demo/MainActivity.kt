package com.hw.kotlin.demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.hw.kotlin.demo.structs.Person
import com.hw.kotlin.demo.structs.PersonView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.FileReader
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private val mainScope = MainScope()

    private lateinit var loadImage: ImageView
    private lateinit var avatarImg: ImageView
    private lateinit var companyImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadImage = findViewById(R.id.load_img)
        avatarImg = findViewById(R.id.avatar_img)
        companyImg = findViewById(R.id.company_img)

        println("factorial 10 = ${factorial(10)}")

//        sendEmail(Person("Jason", 1), "Hello, Json")
//
//        val personView = PersonView()
//
//        getPerson(null, 1)?.let { personView.showPerson(it) } ?: personView.showError()
//
//        getPerson("Hick", 1)?.let { personView.showPerson(it) } ?: personView.showError()

//        runBlocking {
//            suspendingGetImage()
//        }
//
//        GlobalScope.launch {
//            suspendingGetImage()
//        }

        mainScope.launch {
            printThreadName()
        }

        lifecycleScope.launch {

        }


        mainScope.launch {
            suspendPrint()
        }


        loadAndShowImage()

        loadAndShowAvatarAndCompany()

        Log.i(TAG, "a(::b) = ${a(::b)}")
        val c = ::b

        val f: Float = 10.0f
        f.pow(5)

        Log.i(TAG, "c(2) = ${c(2)}")

        Log.i(TAG, "Hello Jack.method1(1) = ${"Hello Jack".method1(1)}")

        val a = "hello"::method1.invoke(2)

        val b = (String::method1)("Hi", 3)

        val d = String::method1.invoke("Haha", 4)

        Log.i(TAG, "a = $a" +
                ", b = $b" +
                ", d = $d")

        val e: String.(Int) -> String = String::method1

        val h = "oohh".e(5)
        val i = e("hw", 6)
        val j = e.invoke("kdkd", 7)

        Log.i(TAG, "h = $h" +
                ", i = $i" +
                ", j = $j")
    }

    private fun loadAndShowImage() {
        mainScope.launch {
            val bitmap = getBitmap("https://t7.baidu.com/it/u=2991714299,2133773995&fm=193&f=GIF")
            bitmap?.let {
                loadImage.setImageBitmap(it)
            }

        }
    }

    private fun loadAndShowAvatarAndCompany() {
        mainScope.launch {
            val avatar: Deferred<Bitmap?> = async { getBitmap("https://img2.baidu.com/it/u=1994380678,3283034272&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500")  }
            val companyLogo: Deferred<Bitmap?> = async { getBitmap("https://img1.baidu.com/it/u=3779791596,46636643&fm=253&fmt=auto&app=138&f=JPG?w=330&h=250") }
            showAvatarAndLogo(avatar.await(), companyLogo.await())
        }

    }

    private fun showAvatarAndLogo(await: Bitmap?, companyLogo: Bitmap?) {
        Log.i(TAG, "showAvatarAndLogo await = $await" +
                ", companyLogo = $companyLogo")
        await?.let {
            avatarImg.setImageBitmap(it)
        }
        companyLogo?.let {
            companyImg.setImageBitmap(it)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    /**
     * 计算一个数的阶乘
     */
    fun factorial(n: Int) : Long {
        require(n >= 0) {
            "Cannot calculate factorial of $n because of it is smaller than 0"
        }
        return if (n <= 1) 1 else factorial(n-1) * n
    }

    fun sendEmail(person: Person, text: String?) {
        Log.i(TAG, "sendEmail person = $person" +
                ", text = $text")
//        val email: String = person.email ?: return
        val email: String = person.email ?: run {
            Log.i(TAG, "sendEmail email not send because of email is empty")
            return
        }
        Log.i(TAG, "sendEmail email = $email")

    }

    fun countCharactorInFile(path: String): Int {
//        val reader = BufferedReader(FileReader(path))
//        reader.use {
//            return reader.lineSequence().sumOf { it.length }
//        }

        BufferedReader(FileReader(path)).useLines { lines ->
            return lines.sumOf { it.length }
        }
    }

    fun getPerson(name: String?, sex: Int) : Person? {
        name?.run {
            return Person(this, sex)
        }?: run {
            return null
        }
    }


    private suspend fun suspendingGetImage(){
        val coroutineScope = CoroutineScope(coroutineContext)
        coroutineScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {

            }

        }
        coroutineScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {

            }

        }
        withContext(Dispatchers.IO){

        }
    }


    private suspend fun suspendPrint() {
        Log.i(TAG, "suspendPrint current Thred is ${Thread.currentThread().name}")
    }

    private suspend fun printThreadName() {
        val coroutineScope = CoroutineScope(coroutineContext)
        coroutineScope.launch(Dispatchers.IO) {
            Log.i(TAG, "current Thream name is : ${Thread.currentThread().name}")
        }
    }


    private suspend fun getBitmap(imageUrl: String): Bitmap? = withContext(Dispatchers.IO){
        var inputStream: InputStream? = null
        var outputStream: ByteArrayOutputStream? = null
        var url: URL? = null
        try {
            url = URL(imageUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.readTimeout = 2000
            httpURLConnection.connectTimeout = 5000
            httpURLConnection.connect()
            if (httpURLConnection.responseCode == 200) {
                inputStream = httpURLConnection.inputStream
                outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024 * 8)
                var len = -1
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                val bu = outputStream.toByteArray()
                return@withContext BitmapFactory.decodeByteArray(bu, 0, bu.size)
            } else {
                Log.e(TAG, "网络连接失败----" + httpURLConnection.responseCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "连接异常 e = ${e.stackTraceToString()}")
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                outputStream?.close()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        return@withContext null
    }

    fun a(funParam: (Int) -> String) : String {
        return funParam(1)
    }

    fun b(p: Int) :String {
        return p.plus(1).toString()
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}