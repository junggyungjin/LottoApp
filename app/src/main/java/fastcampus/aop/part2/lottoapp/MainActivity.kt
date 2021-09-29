package fastcampus.aop.part2.lottoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import fastcampus.aop.part2.lottoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()
    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            binding.ball1,
            binding.ball2,
            binding.ball3,
            binding.ball4,
            binding.ball5,
            binding.ball6
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 45

        initRandomButton()
        initAddButton()
        initClearButton()
    }

    private fun initRandomButton() {
        binding.randomButton.setOnClickListener {
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)
            }
        }
    }

    private fun initAddButton() {
        binding.addButton.setOnClickListener {
            if (didRun) {
                Toast.makeText(this@MainActivity, "초기화 후 시도해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 6) {
                Toast.makeText(this@MainActivity, "번호는 6개까지만 선택가능합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(binding.numberPicker.value)) {
                Toast.makeText(this@MainActivity, "이미 선택한 번호입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView =
                numberTextViewList[pickNumberSet.size]

            textView.isVisible = true
            textView.text = binding.numberPicker.value.toString()

            setNumberBackground(binding.numberPicker.value, textView)

            pickNumberSet.add(binding.numberPicker.value)
        }
    }

    private fun initClearButton() {
        binding.clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun setNumberBackground(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_yellow)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_blue)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_red)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_gray)
            else -> textView.background =
                ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_green)
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue
                    }
                    this.add(i)
                }
            }

        numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(
            0,
            6 - pickNumberSet.size
        )
        return newList.sorted()
    }
}