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
import java.util.List;
import java.util.Optional;

/**
 * Interface untuk Bidang
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public interface BidangDao {
    
    /**
     * Fungsi untuk insert Bidang baru
     * @param bidang
     * @return 0=Gagal, 1=Sukses
     */
    int insertBidang(Bidang bidang);
    
    /**
     * Fungsi untuk menngubah Bidang berdasarkan id_bidang
     * @param id_bidang
     * @param bidang
     * @return 0=Gagal, 1=Sukses
     */
    int updateBidangById(int id_bidang, Bidang bidang);
    
    /**
     * Fungsi untuk menghapus Bidang berdasarkan id_bidang
     * @param id_bidang
     * @return 0=Gagal, 1=Sukses
     */
    int deleteBidangById(int id_bidang);
    
    /**
     * Fungsi untuk memilih semua Bidang
     * @return List of Bidang
     */
    List<Bidang> selectAllBidang();
    
    /**
     * Fungsi untuk men-select salah satu Bidang berdasarkan id_bidang
     * @param id_bidang
     * @return Optional of Bidang
     */
    Optional<Bidang> selectBidangById(int id_bidang);
    
}
