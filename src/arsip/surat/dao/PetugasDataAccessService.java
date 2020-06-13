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

import arsip.surat.model.Petugas;
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
 * Implementasi dari interface PetugasDao
 *
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class PetugasDataAccessService implements PetugasDao {

    private Connection connection;
    public static List<Petugas> DB = new ArrayList<>();

    @Override
    public int insertPetugas(Petugas petugas) {
        connection = ConnectionUtil.getConnection();
        final String sql = "CALL insert_petugas(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareCall(sql)) {
            // insert ke tabel user
            ps.setString(1, petugas.getUsername());
            ps.setString(2, petugas.getPassword());
            ps.setString(3, petugas.getLevel());
            // insert ke tabel petugas_pns
            ps.setString(4, petugas.getNip());
            ps.setString(5, petugas.getNama());
            ps.setString(6, petugas.getJabatan());
            ps.setString(7, petugas.getNama_bidang());
            ps.executeUpdate();
            DB.add(petugas);
            connection.close();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(PetugasDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public int updatePetugasByNIP(String nip, Petugas petugas) {
        return selectPetugasByNIP(nip)
                .map(oldPetugas -> {
                    int indexOfPetugas = DB.indexOf(oldPetugas);
                    if (indexOfPetugas >= 0) {
                        connection = ConnectionUtil.getConnection();
                        final String sql = "CALL update_petugas(?,?,?,?,?,?,?,?)";
                        try (PreparedStatement ps = connection.prepareCall(sql)) {
                            ps.setString(1, nip); // nip lama
                            // data petugas baru
                            ps.setString(2, petugas.getNip());
                            ps.setString(3, petugas.getNama());
                            ps.setString(4, petugas.getJabatan());
                            ps.setString(5, petugas.getUsername());
                            ps.setString(6, petugas.getPassword());
                            ps.setString(7, petugas.getLevel());
                            ps.setString(8, petugas.getNama_bidang());
                            ps.executeUpdate();
                            DB.set(indexOfPetugas, petugas);
                            connection.close();
                            return 1;
                        } catch (SQLException ex) {
                            Logger.getLogger(PetugasDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
                            return 0;
                        }
                    }
                    return 0;
                })
                .orElse(0);
    }

    @Override
    public int deletePetugasByNIP(String nip) {
        Optional<Petugas> selectPetugas = selectPetugasByNIP(nip);
        if (selectPetugas.isPresent()) {
            connection = ConnectionUtil.getConnection();
            final String sql = "CALL delete_petugas(?)";
            try (PreparedStatement ps = connection.prepareCall(sql)) {
                ps.setString(1, nip);
                ps.executeUpdate();
                DB.remove(selectPetugas.get());
                connection.close();
                return 1;
            } catch (SQLException ex) {
                Logger.getLogger(PetugasDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
        }
        return 0;
    }

    @Override
    public List<Petugas> selectAllPetugas() {
        DB.clear();
        connection = ConnectionUtil.getConnection();
        final String sql = "SELECT * FROM info_petugas";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Petugas petugas = new Petugas();
                setPetugas(petugas, resultSet);
                DB.add(petugas);
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(PetugasDataAccessService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return DB;
    }

    @Override
    public Optional<Petugas> selectPetugasByNIP(String nip) {
        return DB.stream()
                .filter(petugas -> petugas.getNip().equals(nip))
                .findFirst();
    }
    
    private void setPetugas(Petugas petugas, ResultSet resultSet) throws SQLException {
        petugas.setNip(resultSet.getString("nip"));
        petugas.setNama(resultSet.getString("nama"));
        petugas.setJabatan(resultSet.getString("jabatan"));
        petugas.setNama_bidang(resultSet.getString("nama_bidang"));
        petugas.setUsername(resultSet.getString("username"));
        petugas.setPassword(resultSet.getString("password"));
        petugas.setLevel(resultSet.getString("level"));
    }

}
