package in.tanu.moneymanager.service;


import in.tanu.moneymanager.dto.ExpenseDTO;
import in.tanu.moneymanager.dto.IncomeDTO;
import in.tanu.moneymanager.dto.RecentTransactionDTO;
import in.tanu.moneymanager.entity.ProfileEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData(){
        ProfileEntity profile=profileService.getCurrentProfile();
        Map<String, Object> returnValue=new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes=incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses=expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactions=concat(latestIncomes.stream().map( income ->
                RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build()),latestExpenses.stream().map(expnse ->
                RecentTransactionDTO.builder()
                        .id(expnse.getId())
                        .profileId(profile.getId())
                        .icon(expnse.getIcon())
                        .name(expnse.getName())
                        .amount(expnse.getAmount())
                        .date(expnse.getDate())
                        .type("expense")
                        .build()))
                .sorted((a,b)->{
                    int cmp=b.getDate().compareTo(a.getDate());
                    if(cmp==0 && a.getCreatedAt() !=null && b.getCreatedAt() !=null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());
        returnValue.put("totalBalance",
                incomeService.getTotalIncomesForCurrentUser()
                        .subtract(expenseService.getTotalExpenseForCurrentUser()));
        returnValue.put("totalIncome", incomeService.getTotalIncomesForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("recent5Expenses",latestExpenses);
        returnValue.put("recentTransactions",recentTransactions);
        return returnValue;

    }

}
