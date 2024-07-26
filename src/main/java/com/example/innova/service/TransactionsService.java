package com.example.innova.service;

import com.example.innova.model.Transaction;
import com.example.innova.model.User;
import com.example.innova.repository.TransactionRepository;
import com.example.innova.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class TransactionsService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionsService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class) // rollbackFor parametresi ile belirli istisnalar için rollback yapılır
    public void processTransaction(Long userId, int amount) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // İşlem yapmadan önce kullanıcı bakiyesini kontrol edin (örneğin, yeterli bakiye olup olmadığını)
        if (user.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Kullanıcının bakiyesini güncelleyin
        user.setBalance(user.getBalance() - amount); // Bakiyeyi güncelle
        userRepository.save(user); // Değişiklikleri kaydet

        // Yeni işlem kaydı oluşturun
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now()); // Sadece tarih kısmını alır
        transactionRepository.save(transaction);
    }
}
