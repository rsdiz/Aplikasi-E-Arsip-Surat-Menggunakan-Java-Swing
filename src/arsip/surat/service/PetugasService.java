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

import arsip.surat.dao.PetugasDao;
import arsip.surat.dao.PetugasDataAccessService;
import arsip.surat.model.Petugas;
import java.util.List;
import java.util.Optional;

/**
 * Service untuk PetugasDataAccessService
 * 
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class PetugasService {
    
    private static PetugasService petugasService;
    private final PetugasDao petugasDao;
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    private PetugasService() {
        this.petugasDao = new PetugasDataAccessService();
    } // </editor-fold>
    
    /** <editor-fold defaultstate="collapsed" desc="Singleton pattern">
     * Singleton pattern
     * @return 
     */
    public static PetugasService getInstance() {
        if (petugasService == null) {
            petugasService = new PetugasService();
        }
        return petugasService;
    } // </editor-fold>
    
    public int insertPetugas(Petugas petugas) {
        return petugasDao.insertPetugas(petugas);
    }
    
    public int updatePetugasByNIP(String nip, Petugas petugas) {
        return petugasDao.updatePetugasByNIP(nip, petugas);
    }
    
    public int updatePasswordByNIP(String nip, String password) {
        return petugasDao.updatePasswordByNIP(nip, password);
    }
    
    public int deletePetugasByNIP(String nip) {
        return petugasDao.deletePetugasByNIP(nip);
    }
    
    public List<Petugas> selectAllPetugas() {
        return petugasDao.selectAllPetugas();
    }
    
    public Optional<Petugas> selectPetugasByNIP(String nip) {
        return petugasDao.selectPetugasByNIP(nip);
    }
    
}