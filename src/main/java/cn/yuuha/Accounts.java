package cn.yuuha;

import java.util.*;

public class Accounts {
    private final List<String> accounts;

    public Accounts() {
        accounts = new ArrayList<>();
    }

    public void remove(String name) {
        accounts.remove(name);
    }

    public void setFirst(String name) {
        if (accounts.contains(name)) {
            accounts.remove(name);
            accounts.add(0, name);
        }
    }

    public void addFirst(String name) {
        if (!accounts.contains(name)) {
            accounts.add(0, name);
        } else {
            setFirst(name);
        }
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(String names) {
        this.accounts.clear();
        this.accounts.addAll(Arrays.asList(names.split(",")));
    }

    @Override
    public String toString() {
        String accountString = accounts.toString();
        return accountString.substring(1, accountString.length() - 1).replaceAll(" ", "");
    }
}
