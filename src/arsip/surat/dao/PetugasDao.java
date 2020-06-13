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
import java.util.List;
import java.util.Optional;

/**
 * Interface untuk Petugas
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public interface PetugasDao {
    
    /**
     * Fungsi untuk insert Petugas baru
     * @param petugas
     * @return 0=Gagal, 1=Sukses
     */
    int insertPetugas(Petugas petugas);
    
    /**
     * Fungsi untuk mengubah Data Petugas berdasarkan NIP
     * @param nip
     * @param petugas
     * @return 0=Gagal, 1=Sukses
     */
    int updatePetugasByNIP(String nip, Petugas petugas);
    
    /**
     * Fungsi untuk menghapus Petugas berdasarkan NIP
     * @param nip
     * @return 0=Gagal, 1=Sukses
     */
    int deletePetugasByNIP(String nip);
    
    /**
     * Fungsi untuk memilih semua Petugas
     * @return List of Petugas
     */
    List<Petugas> selectAllPetugas();
    
    /**
     * Fungsi untuk men-select salah satu Petugas berdasarkan NIP
     * @param nip
     * @return Optional of Petugas
     */
    Optional<Petugas> selectPetugasByNIP(String nip);
    
}
