package com.ssh.common.core.datasource;

public class BasicDataSource extends org.apache.commons.dbcp2.BasicDataSource {

    private static final long serialVersionUID = 20160117L;

    /**
     * @param password encrypt password
     */
    @Override
    public void setPassword(String password) {
        try {
            super.setPassword(AESCipher.decrypt(password, "CHINESE SOFTWARE"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
