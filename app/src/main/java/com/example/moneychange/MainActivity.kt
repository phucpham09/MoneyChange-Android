package com.example.moneychange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSourceAmount: EditText
    private lateinit var editTextTargetAmount: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các thành phần trong layout
        editTextSourceAmount = findViewById(R.id.editTextSourceAmount)
        editTextTargetAmount = findViewById(R.id.editTextTargetAmount)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerTargetCurrency = findViewById(R.id.spinnerTargetCurrency)

        // Khởi tạo danh sách các loại tiền tệ
        val currencyList = listOf("USD", "EUR", "JPY", "VND")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSourceCurrency.adapter = adapter
        spinnerTargetCurrency.adapter = adapter

        // Lắng nghe sự kiện thay đổi dữ liệu
        editTextSourceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateConversion()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Sự kiện thay đổi lựa chọn của Spinner
        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                calculateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerTargetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                calculateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun calculateConversion() {
        val sourceAmountText = editTextSourceAmount.text.toString()
        if (sourceAmountText.isEmpty()) {
            editTextTargetAmount.setText("")
            return
        }

        // Lấy số tiền nguồn và loại tiền
        val sourceAmount = sourceAmountText.toDoubleOrNull() ?: return
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val targetCurrency = spinnerTargetCurrency.selectedItem.toString()

        // Chuyển đổi dựa trên tỷ giá mẫu (giả sử tỷ giá giữa các đồng tiền là cố định)
        val conversionRate = getConversionRate(sourceCurrency, targetCurrency)
        val targetAmount = sourceAmount * conversionRate
        editTextTargetAmount.setText("%.2f".format(targetAmount))
    }

    // Hàm trả về tỷ giá giả lập (có thể cập nhật lại bằng API nếu cần)
    private fun getConversionRate(sourceCurrency: String, targetCurrency: String): Double {
        // Ví dụ tỷ giá giả lập
        val rates = mapOf(
            Pair("USD", "VND") to 24000.0,
            Pair("VND", "USD") to 0.000042,
            Pair("EUR", "USD") to 1.1,
            Pair("JPY", "VND") to 200.0
            // Bổ sung thêm tỷ giá khác nếu cần
        )
        return rates[Pair(sourceCurrency, targetCurrency)] ?: 1.0
    }
}
