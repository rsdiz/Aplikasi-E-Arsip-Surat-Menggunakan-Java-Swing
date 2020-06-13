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

import arsip.surat.model.Bidang;
import arsip.surat.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementasi dari interface BidangDao
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class BidangDataAccessService implements BidangDao {
    
    private Connection connection;
    public static List<Bidang> DB = new ArrayList<>();

    @Override
    public int insertBidang(Bidang bidang) {
        connection = ConnectionUtil.getConnection();
        final String sql = "CALL insert_bidang(?,?)";
        try (PreparedStatement ps = connection.prepareCall(sql)) {
            ps.setInt(1, bidang.getId_bidang());
            ps.setString(2, bidang.getNama_bidang());
            ps.executeUpdate();
            DB.add(bidang);
            connection.close();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BidangDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public int updateBidangById(int id_bidang, Bidang bidang) {
        return selectBidangById(id_bidang)
                .map(oldBidang -> {
                    int indexOfBidang = DB.indexOf(oldBidang);
                    if (indexOfBidang >= 0) {
                        connection = ConnectionUtil.getConnection();
                        final String sql = "CALL update_bidang(?,?,?)";
                        try (PreparedStatement ps = connection.prepareCall(sql)) {
                            ps.setInt(1, id_bidang);
                            ps.setInt(2, bidang.getId_bidang());
                            ps.setString(3, bidang.getNama_bidang());
                            ps.executeUpdate();
                            DB.set(indexOfBidang, bidang);
                            connection.close();
                            return 1;
                        } catch (SQLException ex) {
                            Logger.getLogger(BidangDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
                            return 0;
                        }
                    }
                    return 0;
                })
                .orElse(0);
    }

    @Override
    public int deleteBidangById(int id_bidang) {
        Optional<Bidang> selectBidang = selectBidangById(id_bidang);
        if (selectBidang.isPresent()) {
            connection = ConnectionUtil.getConnection();
            final String sql = "CALL delete_bidang(?)";
            try (PreparedStatement ps = connection.prepareCall(sql)) {
                ps.setInt(1, id_bidang);
                ps.executeUpdate();
                DB.remove(selectBidang.get());
                connection.close();
                return 1;
            } catch (SQLException ex) {
                Logger.getLogger(BidangDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
        }
        return 0;
    }

    @Override
    public List<Bidang> selectAllBidang() {
        DB.clear();
        connection = ConnectionUtil.getConnection();
        final String sql = "SELECT * FROM bidang";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Bidang bidang = new Bidang(
                        resultSet.getInt("id_bidang"),
                        resultSet.getString("nama_bidang")
                );
                DB.add(bidang);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(BidangDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DB;
    }

    @Override
    public Optional<Bidang> selectBidangById(int id_bidang) {
        return DB.stream()
                .filter(bidang -> bidang.getId_bidang() == (id_bidang))
                .findFirst();
    }
    
}
