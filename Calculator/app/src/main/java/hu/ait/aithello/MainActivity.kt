package hu.ait.aithello

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hu.ait.aithello.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root) // root is the LinearLayout

//        if (binding.etName.text.isEmpty() or binding.etName2.text.isEmpty()){
//            // error message
//        }

        binding.btnAdd.setOnClickListener {
            val displayAnswer = binding.etName.text.toString().toInt() + binding.etName2.text.toString().toInt()
            binding.answer.text = "Result: $displayAnswer"
        }

        binding.btnMinus.setOnClickListener {
            val displayAnswer = binding.etName.text.toString().toInt() - binding.etName2.text.toString().toInt()
            binding.answer.text = "Result: $displayAnswer"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}