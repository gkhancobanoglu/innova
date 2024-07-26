package com.example.innova.scheduler;

import com.example.innova.model.JobLog;
import com.example.innova.model.Transaction;
import com.example.innova.model.User;
import com.example.innova.repository.JobLogRepository;
import com.example.innova.repository.TransactionRepository;
import com.example.innova.repository.UserRepository;
import com.example.innova.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ScheduledJobConfig {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledJobConfig.class);
    private final JobLogRepository jobLogRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public ScheduledJobConfig(JobLogRepository jobLogRepository, UserRepository userRepository, TransactionRepository transactionRepository, UserService userService) {
        this.jobLogRepository = jobLogRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Her gün saat 00:00'da çalışır
    public void calculateDailyExpenses() {
        LocalDateTime startTime = LocalDateTime.now();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            JobLog jobLog = new JobLog("calculateDailyExpenses", "STARTED", startTime, null, null, user, 0L); // Başlangıçta totalAmount'ı 0 olarak ayarlıyoruz
            jobLogRepository.save(jobLog);

            try {
                LocalDate today = LocalDate.now(ZoneId.systemDefault());
                LocalDate startDate = today.minusDays(1); // Dün
                LocalDate endDate = today;

                List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
                double totalExpenses = transactions.stream().mapToDouble(Transaction::getAmount).sum();

                logger.info("User ID {}: Günlük harcama toplamı: {}", user.getId(), totalExpenses);

                jobLog.setTotalAmount((long) totalExpenses); // totalAmount'ı güncelle
                jobLog.setStatus("SUCCESS");
            } catch (Exception e) {
                jobLog.setStatus("FAILED");
                jobLog.setErrorMessage(e.getMessage());
                logger.error("User ID {}: Günlük harcama hesaplama işlemi başarısız oldu.", user.getId(), e);
            } finally {
                jobLog.setEndTime(LocalDateTime.now());
                jobLogRepository.save(jobLog);
            }
        }
    }



    //@Scheduled(cron = "*/30 * * * * *") // Her 30 snde bir çalışır.
    /*public void calculateDailyExpenses2() {
        LocalDateTime startTime = LocalDateTime.now();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            JobLog jobLog = new JobLog("calculateDailyExpenses", "STARTED", startTime, null, null, user);
            jobLogRepository.save(jobLog);

            try {
                // Günlük harcamaları hesapla
                double totalExpenses = calculateTotalExpenses(user.getId());
                logger.info("User ID {}: Günlük harcama toplamı: {}", user.getId(), totalExpenses);

                // İşlemin başarıyla tamamlandığını belirle
                jobLog.setStatus("SUCCESS");
            } catch (Exception e) {
                // Hata durumunda durumu ve hata mesajını kaydet
                jobLog.setStatus("FAILED");
                jobLog.setErrorMessage(e.getMessage());
                logger.error("User ID {}: Günlük harcama hesaplama işlemi başarısız oldu.", user.getId(), e);
            } finally {
                jobLog.setEndTime(LocalDateTime.now());
                jobLogRepository.save(jobLog);
            }
        }
    }*/



    @Scheduled(cron = "0 0 0 * * MON") // Her Pazartesi saat 00:00'da çalışır
    public void calculateWeeklyExpenses() {
        LocalDateTime startTime = LocalDateTime.now();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            JobLog jobLog = new JobLog("calculateWeeklyExpenses", "STARTED", startTime, null, null, user, 0L); // Başlangıçta totalAmount'ı 0 olarak ayarlıyoruz
            jobLogRepository.save(jobLog);

            try {
                LocalDate today = LocalDate.now(ZoneId.systemDefault());
                LocalDate startDate = today.minusWeeks(1); // Son 1 hafta
                LocalDate endDate = today;

                List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
                double totalExpenses = transactions.stream().mapToDouble(Transaction::getAmount).sum();

                logger.info("User ID {}: Haftalık harcama toplamı: {}", user.getId(), totalExpenses);

                jobLog.setTotalAmount((long) totalExpenses); // totalAmount'ı güncelle
                jobLog.setStatus("SUCCESS");
            } catch (Exception e) {
                jobLog.setStatus("FAILED");
                jobLog.setErrorMessage(e.getMessage());
                logger.error("User ID {}: Haftalık harcama hesaplama işlemi başarısız oldu.", user.getId(), e);
            } finally {
                jobLog.setEndTime(LocalDateTime.now());
                jobLogRepository.save(jobLog);
            }
        }
    }


    @Scheduled(cron = "0 0 0 1 * *") // Her ayın 1'inde saat 00:00'da çalışır
    public void calculateMonthlyExpenses() {
        LocalDateTime startTime = LocalDateTime.now();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            JobLog jobLog = new JobLog("calculateMonthlyExpenses", "STARTED", startTime, null, null, user, 0L); // Başlangıçta totalAmount'ı 0 olarak ayarlıyoruz
            jobLogRepository.save(jobLog);

            try {
                LocalDate today = LocalDate.now(ZoneId.systemDefault());
                LocalDate startDate = today.minusMonths(1); // Son 1 ay
                LocalDate endDate = today;

                List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
                double totalExpenses = transactions.stream().mapToDouble(Transaction::getAmount).sum();

                logger.info("User ID {}: Aylık harcama toplamı: {}", user.getId(), totalExpenses);

                jobLog.setTotalAmount((long) totalExpenses); // totalAmount'ı güncelle
                jobLog.setStatus("SUCCESS");
            } catch (Exception e) {
                jobLog.setStatus("FAILED");
                jobLog.setErrorMessage(e.getMessage());
                logger.error("User ID {}: Aylık harcama hesaplama işlemi başarısız oldu.", user.getId(), e);
            } finally {
                jobLog.setEndTime(LocalDateTime.now());
                jobLogRepository.save(jobLog);
            }
        }
    }


    @Scheduled(cron = "*/10 * * * * *") // Her 10 sn calışır
    public void updateAllUsersBalance() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            userService.updateUserBalance(user.getId());
        }
    }

    @Scheduled(cron = "*/30 * * * * *") // Her 30 sn çalışır total harcama
    public void logUserExpenses() {
        List<User> users = userService.getAllUsers(); // Tüm kullanıcıları al
        for (User user : users) {
            Long totalAmount = userService.getTotalAmountForUser(user.getId());

            JobLog jobLog = new JobLog(
                    "Expense Calculation",
                    "SUCCESS",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null,
                    user,
                    totalAmount
            );

            jobLogRepository.save(jobLog);
        }
    }


    private double calculateTotalExpenses(Long userId) {
        // Veritabanından kullanıcının harcamalarını toplamaya yönelik sorgu yapılır
        return transactionRepository.findTotalExpensesByUserId(userId);
    }

}
