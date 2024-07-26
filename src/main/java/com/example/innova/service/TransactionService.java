package com.example.innova.service;


import com.example.innova.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Optional<Transaction> getTransactionById(Long id);

    Transaction saveTransaction(Transaction transaction);

    void deleteTransaction(Long id);

    double getTotalExpensesByUserId(Long userId);
}
