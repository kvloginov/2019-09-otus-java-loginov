package ru.otus.hw06.atm;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.otus.hw06.atm.cell.Cell;
import ru.otus.hw06.banknote.Banknote;
import ru.otus.hw06.banknote.Bundle;
import ru.otus.hw06.banknote.Currency;

public class AtmEmulator {
    private Map<Banknote, Cell> cells;

    public AtmEmulator(Bundle bundle) {
        cells = new HashMap<>();
        addMoney(bundle);
    }

    public int getBalance() {
        return cells.values().stream().map(Cell::getSum).reduce(0, Integer::sum);
    }

    public String getDetailedBalance() {
        return cells.keySet().stream()
                .sorted()
                .map(x -> x.getValue() +
                        x.getCurrency().getShortName() + " " +
                        ": " + cells.get(x).getCount() + "шт. " +
                        "Сумма:" + cells.get(x).getSum()
                )
                .collect(Collectors.joining("\n"));
    }

    public void addMoney(Bundle bundle) {
        for (Banknote banknote : bundle.getBanknotes()) {
            if (cells.keySet().contains(banknote)) {
                cells.get(banknote).insertBills(bundle.getCountByBanknote(banknote));
            } else {
                cells.put(banknote, new Cell(banknote, bundle.getCountByBanknote(banknote)));
            }
        }
    }

    public void withdrawMoney(int value, Currency currency) {
        Bundle bundle;
        try {
            bundle = getBundleToWithdraw(value, currency);
        } catch (NoMoneyException ex) {
            System.out.println("К сожалению, невозможно выдать " + value + " " + currency.getShortName());
            return;
        }

        for (Cell cell : cells.values()) {
            cell.withdrawBills(bundle.getCountByBanknote(cell.getBanknote()));
        }
    }

    private Bundle getBundleToWithdraw(int value, Currency currency) {
        Bundle bundle = new Bundle();

        List<Banknote> descSortedUsedBanknotesOfCurrency = cells.keySet().stream()
                .filter(x -> x.getCurrency() == currency)
                .sorted()
                .collect(Collectors.toList());

        for (Banknote banknote : descSortedUsedBanknotesOfCurrency) {
            int countOfBillsWeWant = (value - bundle.sum()) / banknote.getValue();
            int countOfBillsWeCanWithdraw = Math.min(countOfBillsWeWant, cells.get(banknote).getCount());

            bundle.add(banknote, countOfBillsWeCanWithdraw);

            if (bundle.sum() == value) {
                return bundle;
            }
        }

        throw new NoMoneyException();
    }
}
