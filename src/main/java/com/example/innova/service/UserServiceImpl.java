package com.example.innova.service;

import com.example.innova.exception.UserNotFoundException;
import com.example.innova.model.Transaction;
import com.example.innova.model.User;
import com.example.innova.repository.TransactionRepository;
import com.example.innova.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;



    @Override
    public List<User> getAllUsers() {
        // Veritabanından tüm kullanıcıları çekin
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        // ID'ye göre kullanıcıyı çekin
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        // Kullanıcıyı veritabanına kaydedin
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        // Kullanıcıyı ID'ye göre silin
        userRepository.deleteById(id);
    }

    @Override
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByMail(email);
        if (user == null) {
            return false; // Kullanıcı bulunamadı
        }
        // Şifreleri düz metin olarak karşılaştır
        return password.equals(user.getPassword());
    }

    @Override
    public User getUserOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    // Kullanıcının toplam harcamasını hesaplayan yöntem
    public void updateUserBalance(Long userId) {
        User user = getUserOrThrow(userId);

        // Kullanıcının tüm işlemlerini getir
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        // Toplam harcamayı hesapla
        double totalExpenses = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Kullanıcının bakiyesini güncelle
        user.setBalance((int) totalExpenses);
        userRepository.save(user);
    }

    @Override
    public Long getTotalAmountForUser(Long userId) {
        return transactionRepository.sumAmountByUserId(userId);
    }


}
