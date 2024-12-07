package com.example.qlchitieu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import com.example.qlchitieu.R
import com.example.qlchitieu.database.Transaction

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactionList: MutableList<Transaction> = mutableListOf()

    // Hàm thêm một giao dịch vào danh sách và thông báo thay đổi
    fun addTransaction(transaction: Transaction) {
        transactionList.add(transaction)
        notifyItemInserted(transactionList.size - 1)
    }

    // Hàm cập nhật danh sách giao dịch
    fun updateTransactions(newTransactionList: List<Transaction>) {
        transactionList.clear()
        transactionList.addAll(newTransactionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        // Hàm bind dữ liệu vào ViewHolder
        fun bind(transaction: Transaction) {
            amountTextView.text = transaction.amount.toString()
            typeTextView.text = transaction.type

            // Đổi màu chữ dựa trên loại giao dịch
            if (transaction.type == "Thu nhập") {
                typeTextView.setTextColor(itemView.context.getColor(R.color.green)) // Màu xanh lá
                amountTextView.setTextColor(itemView.context.getColor(R.color.green))
            } else if (transaction.type == "Chi tiêu") {
                typeTextView.setTextColor(itemView.context.getColor(R.color.expense_red)) // Màu đỏ
                amountTextView.setTextColor(itemView.context.getColor(R.color.expense_red))
            }

            timestampTextView.text = DateFormat.format("dd/MM/yyyy HH:mm", transaction.date)
            descriptionTextView.text = transaction.description
        }
    }
}
