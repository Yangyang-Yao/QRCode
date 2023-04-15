package com.yangyao.dao;

public interface AccountDao {
    boolean checkDuplicate(String username);
    int checkLogin(String username, String password);
    void addAccount(String username, String password);
    boolean validUsername(String username);
}
