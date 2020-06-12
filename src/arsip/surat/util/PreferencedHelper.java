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

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class PreferencedHelper {
    
    public static Preferences prefs = Preferences.userNodeForPackage(arsip.surat.App.class);
    private static final String ID_USER = "id_user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String LEVEL = "level";
    private static final String STAY_LOGIN = "stay_login";

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public PreferencedHelper() {
        prefs.node("Arsip-Surat-Login");
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Fungsi membersihkan Preferences">
    public static void clear() {
        try {
            prefs.clear();
        } catch (BackingStoreException ex) {
            Logger.getLogger(PreferencedHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getter and Setter ID_USER">
    public static int getId() {
        return prefs.getInt(ID_USER, -1);
    }
    
    public static void setId(int id_user) {
        prefs.putInt(ID_USER, id_user);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getter and Setter USERNAME">
    public static String getUsername() {
        return prefs.get(USERNAME, "");
    }
    
    public static void setUsername(String username) {
        prefs.put(USERNAME, username);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getter and Setter PASSWORD">
    public static String getPassword() {
        return prefs.get(PASSWORD, "");
    }
    
    public static void setPassword(String password) {
        prefs.put(PASSWORD, password);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getter and Setter LEVEL">
    public static String getLevel() {
        return prefs.get(LEVEL, "");
    }
    
    public static void setLevel(String level) {
        prefs.put(LEVEL, level);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getter and Setter STAY_LOGIN">
    public static boolean getLogin() {
        return prefs.getBoolean(STAY_LOGIN, false);
    }
    
    public static void setLogin(boolean login) {
        prefs.putBoolean(STAY_LOGIN, login);
    }
    // </editor-fold>
    
}