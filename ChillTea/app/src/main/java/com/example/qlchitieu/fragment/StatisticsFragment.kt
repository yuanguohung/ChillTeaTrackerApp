package com.example.qlchitieu.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.qlchitieu.R
import com.example.qlchitieu.database.Transaction
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.*

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private lateinit var barChart: BarChart
    private lateinit var databaseRef: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo BarChart
        barChart = view.findViewById(R.id.barChart)

        // Khởi tạo Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("transactions")

        // Lấy dữ liệu từ Firebase
        fetchTransactionsFromFirebase()
    }

    private fun fetchTransactionsFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactions = mutableListOf<Transaction>()

                for (data in snapshot.children) {
                    val transaction = data.getValue(Transaction::class.java)
                    if (transaction != null) {
                        transactions.add(transaction)
                    }
                }

                // Hiển thị dữ liệu lên biểu đồ
                displayChartData(transactions)
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi khi truy cập Firebase
            }
        })
    }

    private fun displayChartData(transactions: List<Transaction>) {
        val entries = mutableListOf<BarEntry>()

        // Tạo các bản đồ cho thu nhập và chi tiêu theo ngày
        val incomeByDate = mutableMapOf<Long, Double>()
        val expenseByDate = mutableMapOf<Long, Double>()

        transactions.forEach { transaction ->
            val date = transaction.date
            if (transaction.type == "Chi tiêu") {
                expenseByDate[date] = expenseByDate.getOrDefault(date, 0.0) + transaction.amount
            } else {
                incomeByDate[date] = incomeByDate.getOrDefault(date, 0.0) + transaction.amount
            }
        }

        // Chuyển dữ liệu thành các BarEntry
        var index = 0f
        incomeByDate.forEach { (_, amount) ->
            entries.add(BarEntry(index, amount.toFloat()))
            index++
        }
        expenseByDate.forEach { (_, amount) ->
            entries.add(BarEntry(index, -amount.toFloat()))
            index++
        }

        // Tạo dataset và hiển thị biểu đồ
        val dataSet = BarDataSet(entries, "Thu/Chi")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.invalidate()
    }
}
