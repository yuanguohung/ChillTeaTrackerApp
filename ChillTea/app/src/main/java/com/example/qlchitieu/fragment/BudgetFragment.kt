package com.example.qlchitieu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qlchitieu.R
import com.example.qlchitieu.adapter.TransactionAdapter
import com.example.qlchitieu.database.Transaction

class BudgetFragment : Fragment(R.layout.fragment_budget) {

    private var currentAmount: Double = 0.0 // Biến lưu trữ số dư hiện tại
    private val transactions = mutableListOf<Transaction>() // Danh sách giao dịch

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var currentAmountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        currentAmountTextView = view.findViewById(R.id.currentAmountTextView)
        val transactionRecyclerView: RecyclerView = view.findViewById(R.id.transactionRecyclerView)

        // Cấu hình RecyclerView
        transactionAdapter = TransactionAdapter()
        transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        transactionRecyclerView.adapter = transactionAdapter

        // Hiển thị số dư hiện tại
        updateCurrentAmount()

        return view
    }

    /**
     * Hàm cập nhật số tiền hiện tại.
     */
    private fun updateCurrentAmount() {
        currentAmount = transactions.sumOf { transaction ->
            if (transaction.type == "Thu nhập") transaction.amount else -transaction.amount
        }
        currentAmountTextView.text = "Số tiền: ₫${String.format("%.2f", currentAmount)}"
    }

    /**
     * Thêm giao dịch mới.
     */
    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction) // Thêm giao dịch vào danh sách
        transactionAdapter.addTransaction(transaction) // Cập nhật RecyclerView
        updateCurrentAmount() // Cập nhật số dư hiện tại
    }
}
