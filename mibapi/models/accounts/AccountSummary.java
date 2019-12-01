package mv.com.bml.mibapi.models.accounts;

import java.util.ArrayList;
import java.util.Iterator;

public class AccountSummary {
    public ArrayList<AccountCASA> acctSummaryCASAList;
    public double acctSummaryCASATotalBalance;
    public ArrayList<AccountCredit> acctSummaryCreditCardList;
    public double acctSummaryCreditCardTotalBalance;
    public String acctSummaryFormattedCASATotalBalance;
    public String acctSummaryFormattedCreditCardTotalBalance;
    public String acctSummaryFormattedLoanTotalBalance;
    public String acctSummaryFormattedTDTotalBalance;
    public ArrayList<AccountLoan> acctSummaryLoanList;
    public double acctSummaryLoanTotalBalance;
    public double acctSummaryLoanTotalOverdueAmount;
    public String ccyTotalBalance;
    public String customerId;
    public String formattedTotalBalance;
    public double totalBalance;
    public String userName;

    public AccountLoan getLoanAccount(String str) {
        Iterator it = this.acctSummaryLoanList.iterator();
        while (it.hasNext()) {
            AccountLoan accountLoan = (AccountLoan) it.next();
            if (accountLoan.accountNo.equalsIgnoreCase(str)) {
                return accountLoan;
            }
        }
        return null;
    }
}
