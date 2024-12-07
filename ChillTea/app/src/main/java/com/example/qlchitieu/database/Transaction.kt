package com.example.qlchitieu.database

data class Transaction(
    var id: String = "", // Giữ lại để Firebase có thể ánh xạ dữ liệu
    val amount: Double = 0.0,
    val description: String = "",
    val type: String = "",
    val date: Long = 0L
) {
    // Constructor không tham số cần thiết cho Firebase
    constructor() : this("", 0.0, "", "", 0L)
}
