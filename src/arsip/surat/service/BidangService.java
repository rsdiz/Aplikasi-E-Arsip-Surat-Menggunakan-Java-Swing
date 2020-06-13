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
package arsip.surat.service;

import arsip.surat.dao.BidangDao;
import arsip.surat.dao.BidangDataAccessService;
import arsip.surat.model.Bidang;
import java.util.List;
import java.util.Optional;

/**
 * Service untuk BidangDataAccessService
 * 
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class BidangService {
    
    private static BidangService bidangService;
    private final BidangDao bidangDao;
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    private BidangService() {
        this.bidangDao = new BidangDataAccessService();
    } // </editor-fold>

    /** <editor-fold defaultstate="collapsed" desc="Singleton pattern">
     * Singleton Pattern
     * @return 
     */
    public static BidangService getInstance() {
        if (bidangService == null) {
            bidangService = new BidangService();
        }
        return bidangService;
    } // </editor-fold>
    
    public int insertBidang(Bidang bidang) {
        return bidangDao.insertBidang(bidang);
    }
    
    public int updateBidangById(int id_bidang, Bidang bidang) {
        return bidangDao.updateBidangById(id_bidang, bidang);
    }
    
    public int deleteBidangById(int id_bidang) {
        return bidangDao.deleteBidangById(id_bidang);
    }
    
    public List<Bidang> selectAllBidang() {
        return bidangDao.selectAllBidang();
    }
    
    public Optional<Bidang> selectBidangById(int id_bidang) {
        return bidangDao.selectBidangById(id_bidang);
    }
}
