package com.example.qlchitieu

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.qlchitieu.databinding.ActivityMainBinding
import com.example.qlchitieu.fragment.BudgetFragment
import com.example.qlchitieu.fragment.StatisticsFragment
import com.example.qlchitieu.fragment.TransactionFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_transactions -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            TransactionFragment()
                        )
                        .commit()
                    true
                }

                R.id.nav_statistics -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            StatisticsFragment()
                        )
                        .commit()
                    true
                }

                R.id.nav_budget -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BudgetFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}
