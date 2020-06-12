/*
 * The MIT License
 *
 * Copyright 2020 Universitas Teknologi Yogyakarta.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package arsip.surat.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitas untuk membuat koneksi ke database MySQL
 * 
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class ConnectionUtil {
    
    private static ConnectionUtil connectionUtil;
    private static final Logger LOG = Logger.getLogger(ConnectionUtil.class.getName());
    
    private static Connection DB_CONNECTION = null;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/surat_masuk_keluar";
    private static final String USER = "root";
    private static final String PASS = "";
    
    /**
     * Constructor
     */
    private ConnectionUtil() {
        try {
            // Register Driver yang akan digunakan
            Class.forName(JDBC_DRIVER);
            // Mengkoneksikan Database
            DB_CONNECTION = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException ex) {
            LOG.log(Level.WARNING, "Error: {0}", ex.getMessage());
        }
    }
    
    /**
     * Membuat instance baru apabila belum ada
     * 
     * @return <code>ConnectionUtil</code>
     */
    public static ConnectionUtil getInstance() {
        if (connectionUtil == null) {
            connectionUtil = new ConnectionUtil();
        }
        return connectionUtil;
    }
    
    /**
     * Fungsi untuk mengambil Koneksi database yang sudah dibuat.
     * @return <code>Connection</code>
     */
    public static Connection getConnection() {
        if (DB_CONNECTION == null) {
            getInstance();
        }
        
        return DB_CONNECTION;
    }
    
}