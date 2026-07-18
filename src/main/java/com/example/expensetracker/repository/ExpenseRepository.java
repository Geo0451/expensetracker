package com.example.expensetracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.expensetracker.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Basic CRUD operations (save, findAll, findById, delete) are inherited automatically

    List<Expense> findByDescriptionContainingIgnoreCase(String keyword);
}