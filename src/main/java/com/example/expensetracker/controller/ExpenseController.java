package com.example.expensetracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    // Constructor injection
    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public List<Expense> getAllExpenses(@RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return expenseRepository.findByDescriptionContainingIgnoreCase(search);
        }
        return expenseRepository.findAll();
    }

    @PutMapping("path/{id}")
    public String updateExpense(@PathVariable Long id, @RequestBody Expense updatedExpense) {
        return expenseRepository.findById(id)
                .map(expense -> {
                    expense.setDescription(updatedExpense.getDescription());
                    expense.setAmount(updatedExpense.getAmount());
                    expenseRepository.save(expense);
                    return "Expense updated successfully.";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + id));
    }
    @DeleteMapping("path/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteExpense(@PathVariable Long id) {
        return expenseRepository.findById(id)
                .map(expense -> {
                    expenseRepository.delete(expense);
                    return "Expense deleted successfully.";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found with id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense createExpense(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Expense> createExpensesBulk(@RequestBody List<Expense> expenses) {
        return expenseRepository.saveAll(expenses);
    }
}