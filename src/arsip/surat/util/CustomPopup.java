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

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Class untuk membuat sebuah popup
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class CustomPopup {

    private static JOptionPane jOptionPane;
    private static JDialog jDialog;

    /**
     * Membuat popup pesan error dengan timer,
     * otomatis popup pesan akan hilang/close dengan sendirinya
     * timer default = 3 detik
     * @param parentComponent
     * @param message = pesan yang ingin ditampilkan
     * @param title = judul pada title bar
     */
    public static void createErrorPopup(Component parentComponent, Object message, String title) {
        jOptionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        jDialog = jOptionPane.createDialog(parentComponent, title);
        jDialog.setModal(false);
        jDialog.setVisible(true);
        setTimer();
    }

    /**
     * Membuat popup pesan error dengan timer,
     * otomatis popup pesan akan hilang/close dengan sendirinya
     * timer default = 3 detik
     * @param parentComponent
     * @param message = pesan yang ingin ditampilkan
     */
    public static void createErrorPopup(Component parentComponent, Object message) {
        jOptionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        jDialog = jOptionPane.createDialog(parentComponent, "Error!");
        jDialog.setModal(false);
        jDialog.setVisible(true);
        setTimer();
    }

    /**
     * Membuat popup pesan error dengan timer,
     * otomatis popup pesan akan hilang/close dengan sendirinya
     * timer default = 3 detik
     * @param message = pesan yang ingin ditampilkan
     */
    public static void createErrorPopup(Object message) {
        jOptionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        jDialog = jOptionPane.createDialog(null, "Error!");
        jDialog.setModal(false);
        jDialog.setVisible(true);
        setTimer();
    }

    /**
     * Membuat popup pesan informasi dengan timer,
     * otomatis popup pesan akan hilang/close dengan sendirinya
     * timer default = 3 detik
     * @param parentComponent
     * @param message = pesan yang ingin ditampilkan
     * @param title = judul pada title bar
     */
    public static void createInformationPopup(Component parentComponent, Object message, String title) {
        jOptionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        jDialog = jOptionPane.createDialog(parentComponent, title);
        jDialog.setModal(false);
        jDialog.setVisible(true);
        setTimer();
    }

    /**
     * Membuat popup pesan informasi dengan timer,
     * otomatis popup pesan akan hilang/close dengan sendirinya
     * timer default = 3 detik
     * @param parentComponent
     * @param message = pesan yang ingin ditampilkan
     */
    public static void createInformationPopup(Component parentComponent, Object message) {
        jOptionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        jDialog = jOptionPane.createDialog(parentComponent, "Sukses!");
        jDialog.setModal(false);
        jDialog.setVisible(true);
        setTimer();
    }

    /**
     * Membuat popup pesan informasi dengan timer,
     * otomatis popup pesan akan hilang/close dengan sendirinya
     * timer default = 3 detik
     * @param message = pesan yang ingin ditampilkan
     */
    public static void createInformationPopup(Object message) {
        jOptionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        jDialog = jOptionPane.createDialog(null, "Sukses!");
        jDialog.setModal(false);
        jDialog.setVisible(true);
        setTimer();
    }

    /**
     * Menampilkan popup konfirmasi dengan tombol yes/no
     * @param parentComponent
     * @param message = pesan yang ingin ditampilkan
     * @param title = judul pada title bar
     * @return 0 = yes, 1 = no
     */
    public static int showConfirmDalog(Component parentComponent, Object message, String title) {
        return JOptionPane.showConfirmDialog(parentComponent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Mengatur timer default 3 detik
     */
    private static void setTimer() {
        new Timer(3000, (ActionEvent e) -> {
            jDialog.setVisible(false);
        }).start();
    }

    /**
     * Mengatur timer sesuai dengan inputan
     * @param delay = waktu lamanya timer
     */
    private static void setTimer(int delay) {
        new Timer(delay, (ActionEvent e) -> {
            jDialog.setVisible(false);
        }).start();
    }
}
