package com.example.qlchitieu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qlchitieu.R
import com.example.qlchitieu.adapter.TransactionAdapter
import com.example.qlchitieu.database.Transaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TransactionFragment : Fragment(R.layout.fragment_transaction) {

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        val amountEditText: EditText = view.findViewById(R.id.amountEditText)
        val typeSpinner: Spinner = view.findViewById(R.id.transactionTypeSpinner)
        val descriptionEditText: EditText = view.findViewById(R.id.descriptionEditText)
        val addTransactionButton: Button = view.findViewById(R.id.addTransactionButton)
        val recyclerView: RecyclerView = view.findViewById(R.id.transactionsRecyclerView)

        // Khởi tạo adapter và RecyclerView
        transactionAdapter = TransactionAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = transactionAdapter

        // Khởi tạo Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance("https://quanlychilltea-default-rtdb.firebaseio.com/").getReference("transactions")



        val transactionTypes = arrayOf("Thu nhập", "Chi tiêu")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, transactionTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = spinnerAdapter

        // Lắng nghe sự kiện click vào nút thêm giao dịch
        addTransactionButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull()
            val type = typeSpinner.selectedItem.toString()
            val description = descriptionEditText.text.toString()

            if (amount != null && type.isNotEmpty() && description.isNotEmpty()) {
                val transaction = Transaction(
                    id = databaseRef.push().key ?: "",
                    amount = amount,
                    description = description,
                    type = type,
                    date = System.currentTimeMillis()
                )

                // Thêm giao dịch vào Firebase
                databaseRef.child(transaction.id).setValue(transaction)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Giao dịch đã được thêm!", Toast.LENGTH_SHORT).show()
                        // Cập nhật adapter để hiển thị giao dịch mới
                        transactionAdapter.addTransaction(transaction)
                        amountEditText.text.clear()
                        descriptionEditText.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Thêm giao dịch thất bại!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }

        // Đọc dữ liệu từ Firebase và cập nhật adapter
        databaseRef.get().addOnSuccessListener { snapshot ->
            val transactions = mutableListOf<Transaction>()
            for (data in snapshot.children) {
                val transaction = data.getValue(Transaction::class.java)
                if (transaction != null) {
                    transactions.add(transaction)
                }
            }
            transactionAdapter.updateTransactions(transactions)
        }

        return view
    }
}