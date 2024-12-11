package com.example.first

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.exp
import kotlin.math.sqrt
import java.util.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ссылки на элементы интерфейса
        val meanEditText = findViewById<EditText>(R.id.mean_val)
        val varianceEditText = findViewById<EditText>(R.id.variance_value)
        val generateButton = findViewById<Button>(R.id.get_random_num)
        val resultTextView = findViewById<TextView>(R.id.random_number_result)

        // Обработчик нажатия кнопки
        generateButton.setOnClickListener {
            try {
                val meanText = meanEditText.text.toString()
                val varianceText = varianceEditText.text.toString()

                // Проверяем ввод
                if (meanText.isEmpty() || varianceText.isEmpty()) {
                    resultTextView.text = "Пожалуйста, заполните оба поля."
                    return@setOnClickListener
                }

                val mean = meanText.toDoubleOrNull()
                val variance = varianceText.toDoubleOrNull()

                if (mean == null || variance == null || variance < 0) {
                    resultTextView.text = "Ошибка: некорректные значения µ или σ²."
                    return@setOnClickListener
                }

                // Генерация числа и вывод результата
                val randomValue = generateLogNormal(mean, sqrt(variance))
                resultTextView.text = "Сгенерированное число: $randomValue"

                // Сохранение последнего результата
                lastGeneratedNumber = resultTextView.text.toString()

            } catch (e: Exception) {
                resultTextView.text = "Произошла ошибка: ${e.message}"
            }
        }
    }

    // Генерация логнормального числа
    private fun generateLogNormal(mean: Double, stdDev: Double): Double {
        val normalValue = Random().nextGaussian() * stdDev + mean
        return exp(normalValue)
    }

    // Переменная для хранения последнего сгенерированного числа
    private var lastGeneratedNumber: String? = null

    // Сохранение состояния при повороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("LAST_NUMBER", lastGeneratedNumber)
    }

    // Восстановление состояния после поворота экрана
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastGeneratedNumber = savedInstanceState.getString("LAST_NUMBER")
        val resultTextView = findViewById<TextView>(R.id.random_number_result)
        resultTextView.text = lastGeneratedNumber ?: "Результат будет здесь"
    }
}
