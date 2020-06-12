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
package arsip.surat.dao;

import arsip.surat.util.ConnectionUtil;
import arsip.surat.util.PasswordUtil;
import arsip.surat.util.PreferencedHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class LoginDataAccessService implements LoginDao {

    private final Connection connection = ConnectionUtil.getConnection();
    
    @Override
    public int loginProcess(String username, String password, boolean stayLogin) {
        final String sql = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String salt = resultSet.getInt("id_user") + " " + resultSet.getString("username");
                byte[] encryptSalt = salt.getBytes();
                salt = Base64.getEncoder().encodeToString(encryptSalt);
                boolean isMatch = PasswordUtil.veriifyPassword(password, resultSet.getString("password"), salt);
                if (isMatch) {
                    saveUser(resultSet);
                    PreferencedHelper.setLogin(stayLogin);
                    return 1;
                } else {
                    return 0;
                }
            }
            return -1;
        } catch (SQLException ex) {
            Logger.getLogger(LoginDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
            return 2;
        }
    }
    
    private void saveUser(ResultSet resultSet) throws SQLException {
        PreferencedHelper.setId(resultSet.getInt("id_user"));
        PreferencedHelper.setUsername(resultSet.getString("username"));
        PreferencedHelper.setPassword(resultSet.getString("password"));
        PreferencedHelper.setLevel(resultSet.getString("level"));
    }
    
}
