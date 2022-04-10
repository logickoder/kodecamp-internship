package dev.logickoder.mycanva

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myCanvaView = MyCanvaView(this)
        myCanvaView.contentDescription = getString(R.string.content_description)
        setContentView(myCanvaView)
    }
}
