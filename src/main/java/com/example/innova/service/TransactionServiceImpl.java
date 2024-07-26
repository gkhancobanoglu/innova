package com.example.innova.service;

import com.example.innova.model.Transaction;
import com.example.innova.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getAllTransactions() {
        // Veritabanından tüm işlemleri çekin
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        // ID'ye göre işlemi çekin
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        // İşlemi veritabanına kaydedin
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        // İşlemi ID'ye göre silin
        transactionRepository.deleteById(id);
    }

    @Override
    public double getTotalExpensesByUserId(Long userId) {
        // Kullanıcı ID'sine göre işlemleri çekin
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        // Harcama miktarlarını toplayarak toplam harcamayı hesaplayın
        return transactions.stream()
                .mapToDouble(Transaction::getAmount) // Harcama miktarını toplamak için
                .sum(); // Toplam harcamayı döndür
    }
}

