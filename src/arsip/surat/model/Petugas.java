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
package arsip.surat.model;

/**
 * Model untuk Petugas PNS + user
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class Petugas {
    
    private String nip;
    private String nama;
    private String jabatan;
    private String username;
    private String password;
    private String level;
    private String nama_bidang;

    public Petugas() {
    }

    public Petugas(String nip, String nama, String jabatan, String username, String password, String level, String nama_bidang) {
        this.nip = nip;
        this.nama = nama;
        this.jabatan = jabatan;
        this.username = username;
        this.password = password;
        this.level = level;
        this.nama_bidang = nama_bidang;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNama_bidang() {
        return nama_bidang;
    }

    public void setNama_bidang(String nama_bidang) {
        this.nama_bidang = nama_bidang;
    }
    
}
