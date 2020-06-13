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
package arsip.surat.view.panel;

import arsip.surat.model.Bidang;
import arsip.surat.model.Petugas;
import arsip.surat.service.BidangService;
import arsip.surat.service.PetugasService;
import arsip.surat.util.PasswordUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Base64;
import java.util.Optional;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;

/**
 * Panel untuk Menu Petugas
 *
 * @author Muhammad Rosyid Izzulkhaq (rsdiz)
 */
public class PanelMenuPetugas extends javax.swing.JPanel {

    private String selectedPetugas = "";

    private final PetugasService petugasService = PetugasService.getInstance();
    private final BidangService bidangService = BidangService.getInstance();
    private Thread thread;
    private int totalPetugas;
    private int totalBidang;
    private List<Petugas> listPetugas;
    private List<Bidang> listBidang;

    /**
     * Creates new form PanelMenuPetugas
     */
    public PanelMenuPetugas() {
        initComponents();
        thread = new Thread("thread-petugas") {
            @Override
            public void run() {
                syncListBidang();
                setModelBidang();
                syncListPetugas();
                setModelPetugas();
            }
        };
        startThread();
    }

    // <editor-fold defaultstate="collapsed" desc="Fungsi untuk mengelola data bidang">
    /**
     * Fungsi untuk mensinkronisasi data bidang dari database kemudian menyimpan
     * data tersebut ke List listBidang.
     */
    private synchronized void syncListBidang() {
        listBidang = bidangService.selectAllBidang();
        totalBidang = listBidang.size();
    }

    /**
     * Fungsi untuk mengubah model dari combo box bidang
     */
    private synchronized void setModelBidang() {
        cb_editBidang.removeAllItems();
        cb_tambahBidang.removeAllItems();
        listBidang.forEach(bidang -> {
            cb_editBidang.addItem(bidang.getNama_bidang());
            cb_tambahBidang.addItem(bidang.getNama_bidang());
        });
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fungsi untuk mengelola data petugas"> 
    /**
     * Fungsi untuk mensinkronisasi data petugas dari database kemudian
     * menyimpan data tersebut ke List listPetugas.
     */
    private synchronized void syncListPetugas() {
        listPetugas = petugasService.selectAllPetugas();
        totalPetugas = listPetugas.size();
        textTotalPetugas.setText("Total Petugas: " + totalPetugas);
    }
    /**
     * Variabel untuk penomoran pada tabel Petugas
     */
    private int indexPetugas;

    /**
     * Fungsi untuk mengubah model dari tabel bidang
     */
    private synchronized void setModelPetugas() {
        indexPetugas = 1;
        hideHeaderTable();
        DefaultTableModel modelPetugas = (DefaultTableModel) tabelPetugas.getModel();
        modelPetugas.setRowCount(0);
        listPetugas.forEach(petugas -> {
            Object[] data = new Object[]{
                indexPetugas,
                petugas.getNama(),
                petugas.getJabatan(),
                petugas.getNama_bidang(),
                petugas.getLevel(),
                petugas.getNip()
            };
            modelPetugas.addRow(data);
            indexPetugas++;
        });
        tabelPetugas.setModel(modelPetugas);
        tabelPetugas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JTable selected = (JTable) e.getSource();
                    Point point = e.getPoint();
                    int rowSelected = selected.rowAtPoint(point);
                    buttonEdit.setVisible(true);
                    buttonHapus.setVisible(true);
                    selectedPetugas = selected.getModel().getValueAt(rowSelected, 5).toString();
                }
            }
        });
        setTableAlignment(JLabel.CENTER);
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fungsi untuk menjalankan thread">
    /**
     * Fungsi untuk menjalankan thread
     */
    private void startThread() {
        thread.start();
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fungsi untuk menghilangkan header pada tabel petugas">
    /**
     * Fungsi untuk menghilangkan header pada tabel petugas
     */
    private void hideHeaderTable() {
        scrollPanelTabelPetugas.getColumnHeader().setVisible(false);
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Fungsi untuk mengubah tata letak paragraf pada kolom di tabel">
    /**
     * Fungsi untuk mengubah tata letak parafgraf pada kolom di tabel
     *
     * @param alignment [ JLabel.RIGHT | JLabel.CENTER | JLabel.LEFT ]
     */
    private void setTableAlignment(int alignment) {
        DefaultTableCellRenderer alignmentRenderer = new DefaultTableCellRenderer();
        alignmentRenderer.setHorizontalAlignment(alignment);
        for (int i = 0; i < tabelPetugas.getColumnModel().getColumnCount(); i++) {
            tabelPetugas.getColumnModel().getColumn(i).setCellRenderer(alignmentRenderer);
        }
    } // </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radioGroupLevel = new javax.swing.ButtonGroup();
        basePanel = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        textMenu = new javax.swing.JLabel();
        separatorTop = new javax.swing.JSeparator();
        panelMainButton = new javax.swing.JPanel();
        buttonInfo = new javax.swing.JPanel();
        iconInfo = new javax.swing.JLabel();
        textInfo = new javax.swing.JLabel();
        buttonTambahData = new javax.swing.JPanel();
        iconTambahData = new javax.swing.JLabel();
        textTambahData = new javax.swing.JLabel();
        buttonRefreshData = new javax.swing.JPanel();
        iconRefreshData = new javax.swing.JLabel();
        textRefreshData = new javax.swing.JLabel();
        panelBantuan = new javax.swing.JPanel();
        buttonKembaliBantuan = new javax.swing.JButton();
        textMenu1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelDaftarPetugas = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        headNomer = new javax.swing.JPanel();
        headTextNomor = new javax.swing.JLabel();
        headNama = new javax.swing.JPanel();
        headTextNama = new javax.swing.JLabel();
        headJabatan = new javax.swing.JPanel();
        headTextJabatan = new javax.swing.JLabel();
        headBidang = new javax.swing.JPanel();
        headTextBidang = new javax.swing.JLabel();
        headLevel = new javax.swing.JPanel();
        headTextLevel = new javax.swing.JLabel();
        scrollPanelTabelPetugas = new javax.swing.JScrollPane();
        tabelPetugas = new javax.swing.JTable();
        buttonEdit = new javax.swing.JButton();
        buttonHapus = new javax.swing.JButton();
        textTotalPetugas = new javax.swing.JLabel();
        panelManagePetugas = new javax.swing.JPanel();
        buttonKembaliEdit = new javax.swing.JButton();
        textTitlePanel = new javax.swing.JLabel();
        imgEditPetugas = new javax.swing.JLabel();
        panelTambahPetugas = new javax.swing.JPanel();
        buttonTambah = new javax.swing.JButton();
        teksBidang1 = new javax.swing.JTextField();
        tf_panelBidang1 = new javax.swing.JPanel();
        cb_tambahBidang = new javax.swing.JComboBox<>();
        teksLevel1 = new javax.swing.JTextField();
        tf_panelLevel1 = new javax.swing.JPanel();
        rbTambah_admin = new javax.swing.JRadioButton();
        rbTambah_petugas = new javax.swing.JRadioButton();
        tf_tambahUsername = new javax.swing.JTextField();
        tf_tambahNip = new javax.swing.JTextField();
        tf_tambahJabatan = new javax.swing.JTextField();
        tf_tambahNama = new javax.swing.JTextField();
        jpf_tambahPassword = new javax.swing.JPasswordField();
        buttonShowPassword1 = new javax.swing.JButton();
        panelEditPetugas = new javax.swing.JPanel();
        panelEditPassword = new javax.swing.JPanel();
        jpf_editPassword = new javax.swing.JPasswordField();
        buttonShowPassword = new javax.swing.JButton();
        buttonSimpanPassword = new javax.swing.JButton();
        buttonUbahPassword = new javax.swing.JButton();
        buttonSimpan = new javax.swing.JButton();
        teksBidang = new javax.swing.JTextField();
        tf_panelBidang = new javax.swing.JPanel();
        cb_editBidang = new javax.swing.JComboBox<>();
        teksLevel = new javax.swing.JTextField();
        tf_panelLevel = new javax.swing.JPanel();
        rb_admin = new javax.swing.JRadioButton();
        rb_petugas = new javax.swing.JRadioButton();
        tf_editUsername = new javax.swing.JTextField();
        tf_editNip = new javax.swing.JTextField();
        tf_editJabatan = new javax.swing.JTextField();
        tf_editNama = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(990, 710));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        basePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setBackground(new java.awt.Color(222, 222, 222));
        panelMenu.setPreferredSize(new java.awt.Dimension(990, 250));
        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textMenu.setFont(new java.awt.Font("monogram", 0, 36)); // NOI18N
        textMenu.setForeground(new java.awt.Color(0, 51, 51));
        textMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textMenu.setText("Menu Kelola Petugas");
        panelMenu.add(textMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 970, 50));
        panelMenu.add(separatorTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 890, 10));

        panelMainButton.setBackground(new java.awt.Color(222, 222, 222));
        panelMainButton.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonInfo.setBackground(new java.awt.Color(232, 232, 232));
        buttonInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonInfo.setPreferredSize(new java.awt.Dimension(100, 100));
        buttonInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonInfoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonInfoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonInfoMouseExited(evt);
            }
        });
        buttonInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImageIcon imageIconInfo = new ImageIcon(getClass().getResource("/arsip/surat/assets/images/info.png"));
        Image imgInfo = imageIconInfo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        iconInfo.setIcon(new ImageIcon(imgInfo));
        iconInfo.setPreferredSize(new java.awt.Dimension(70, 70));
        buttonInfo.add(iconInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 70, 70));

        textInfo.setFont(new java.awt.Font("Monofonto", 0, 14)); // NOI18N
        textInfo.setForeground(new java.awt.Color(0, 51, 102));
        textInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textInfo.setText("Bantuan");
        textInfo.setIconTextGap(0);
        buttonInfo.add(textInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 100, -1));

        panelMainButton.add(buttonInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 120, 120));

        buttonTambahData.setBackground(new java.awt.Color(232, 232, 232));
        buttonTambahData.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonTambahData.setPreferredSize(new java.awt.Dimension(100, 100));
        buttonTambahData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonTambahDataMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonTambahDataMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonTambahDataMouseExited(evt);
            }
        });
        buttonTambahData.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconTambahData.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImageIcon imageIconAdd = new ImageIcon(getClass().getResource("/arsip/surat/assets/images/add.png"));
        Image imgAdd = imageIconAdd.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        iconTambahData.setIcon(new ImageIcon(imgAdd));
        iconTambahData.setPreferredSize(new java.awt.Dimension(70, 70));
        buttonTambahData.add(iconTambahData, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 70, 70));

        textTambahData.setFont(new java.awt.Font("Monofonto", 0, 14)); // NOI18N
        textTambahData.setForeground(new java.awt.Color(0, 51, 102));
        textTambahData.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textTambahData.setText("Tambah Data");
        textTambahData.setIconTextGap(0);
        buttonTambahData.add(textTambahData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 100, -1));

        panelMainButton.add(buttonTambahData, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 120, 120));

        buttonRefreshData.setBackground(new java.awt.Color(232, 232, 232));
        buttonRefreshData.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        buttonRefreshData.setPreferredSize(new java.awt.Dimension(100, 100));
        buttonRefreshData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonRefreshDataMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonRefreshDataMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonRefreshDataMouseExited(evt);
            }
        });
        buttonRefreshData.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconRefreshData.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ImageIcon imageIconReload = new ImageIcon(getClass().getResource("/arsip/surat/assets/images/reload.png"));
        Image imgReload = imageIconReload.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        iconRefreshData.setIcon(new ImageIcon(imgReload));
        iconRefreshData.setPreferredSize(new java.awt.Dimension(70, 70));
        buttonRefreshData.add(iconRefreshData, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 15, 70, 70));

        textRefreshData.setFont(new java.awt.Font("Monofonto", 0, 14)); // NOI18N
        textRefreshData.setForeground(new java.awt.Color(0, 51, 102));
        textRefreshData.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textRefreshData.setText("Refresh Data");
        textRefreshData.setIconTextGap(0);
        buttonRefreshData.add(textRefreshData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 92, 100, -1));

        panelMainButton.add(buttonRefreshData, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 40, 120, 120));

        panelMenu.add(panelMainButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 990, 190));

        panelBantuan.setBackground(new java.awt.Color(222, 222, 222));
        panelBantuan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonKembaliBantuan.setFont(new java.awt.Font("monogram", 0, 24)); // NOI18N
        buttonKembaliBantuan.setForeground(new java.awt.Color(0, 103, 103));
        buttonKembaliBantuan.setText("<< KEMBALI");
        buttonKembaliBantuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonKembaliBantuanActionPerformed(evt);
            }
        });
        panelBantuan.add(buttonKembaliBantuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 150, 50));

        textMenu1.setFont(new java.awt.Font("monogram", 0, 36)); // NOI18N
        textMenu1.setForeground(new java.awt.Color(0, 51, 51));
        textMenu1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textMenu1.setText("BANTUAN");
        panelBantuan.add(textMenu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 150, 60));

        jLabel1.setFont(new java.awt.Font("monogramextended", 0, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(100, 100, 100));
        jLabel1.setText("<html><p>Menu ini merupakan menu untuk mengelola petugas,<br>\nuntuk mengedit/menghapus petugas, silahkan pilih salah satu petugas pada tabel terlebih dahulu,<br>\nuntuk menambahkan data petugas, silahkan klik tombol tambah petugas,<br>\nuntuk merefresh data petugas, silahkan klik tombol refresh data.</p></html>");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        panelBantuan.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 700, 130));

        panelMenu.add(panelBantuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 990, 190));
        panelBantuan.setVisible(false);

        basePanel.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        panelDaftarPetugas.setBackground(new java.awt.Color(0, 143, 143));
        panelDaftarPetugas.setPreferredSize(new java.awt.Dimension(990, 460));
        panelDaftarPetugas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headNomer.setBackground(new java.awt.Color(0, 112, 112));
        headNomer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headNomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headTextNomor.setFont(new java.awt.Font("Monofonto", 0, 18)); // NOI18N
        headTextNomor.setForeground(new java.awt.Color(255, 255, 255));
        headTextNomor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headTextNomor.setText("No.");
        headNomer.add(headTextNomor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 20));

        jPanel3.add(headNomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 50, 40));

        headNama.setBackground(new java.awt.Color(0, 112, 112));
        headNama.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headNama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headTextNama.setFont(new java.awt.Font("Monofonto", 0, 18)); // NOI18N
        headTextNama.setForeground(new java.awt.Color(255, 255, 255));
        headTextNama.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headTextNama.setText("Nama");
        headNama.add(headTextNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 230, 20));

        jPanel3.add(headNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 5, 255, 40));

        headJabatan.setBackground(new java.awt.Color(0, 112, 112));
        headJabatan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headJabatan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headTextJabatan.setFont(new java.awt.Font("Monofonto", 0, 18)); // NOI18N
        headTextJabatan.setForeground(new java.awt.Color(255, 255, 255));
        headTextJabatan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headTextJabatan.setText("Jabatan");
        headJabatan.add(headTextJabatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 170, 20));

        jPanel3.add(headJabatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 5, 190, 40));

        headBidang.setBackground(new java.awt.Color(0, 112, 112));
        headBidang.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headBidang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headTextBidang.setFont(new java.awt.Font("Monofonto", 0, 18)); // NOI18N
        headTextBidang.setForeground(new java.awt.Color(255, 255, 255));
        headTextBidang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headTextBidang.setText("Bidang");
        headBidang.add(headTextBidang, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 20));

        jPanel3.add(headBidang, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 5, 200, 40));

        headLevel.setBackground(new java.awt.Color(0, 112, 112));
        headLevel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        headLevel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headTextLevel.setFont(new java.awt.Font("Monofonto", 0, 18)); // NOI18N
        headTextLevel.setForeground(new java.awt.Color(255, 255, 255));
        headTextLevel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headTextLevel.setText("Level");
        headLevel.add(headTextLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 140, 20));

        jPanel3.add(headLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 5, 175, 40));

        panelDaftarPetugas.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 20, 900, 50));

        scrollPanelTabelPetugas.setBackground(new java.awt.Color(0, 102, 102));
        scrollPanelTabelPetugas.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tabelPetugas.setBackground(new java.awt.Color(0, 102, 102));
        tabelPetugas.setFont(new java.awt.Font("monogram", 0, 24)); // NOI18N
        tabelPetugas.setForeground(new java.awt.Color(230, 230, 230));
        tabelPetugas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama", "Jabatan", "Bidang", "Level", "NIP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelPetugas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelPetugas.setGridColor(new java.awt.Color(0, 143, 143));
        tabelPetugas.setRowHeight(50);
        tabelPetugas.setRowMargin(0);
        tabelPetugas.setSelectionBackground(new java.awt.Color(0, 130, 130));
        tabelPetugas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelPetugas.setShowGrid(false);
        tabelPetugas.getTableHeader().setResizingAllowed(false);
        tabelPetugas.getTableHeader().setReorderingAllowed(false);
        scrollPanelTabelPetugas.setViewportView(tabelPetugas);
        tabelPetugas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tabelPetugas.getColumnModel().getColumnCount() > 0) {
            tabelPetugas.getColumnModel().getColumn(0).setResizable(false);
            tabelPetugas.getColumnModel().getColumn(0).setPreferredWidth(60);
            tabelPetugas.getColumnModel().getColumn(1).setResizable(false);
            tabelPetugas.getColumnModel().getColumn(1).setPreferredWidth(250);
            tabelPetugas.getColumnModel().getColumn(2).setResizable(false);
            tabelPetugas.getColumnModel().getColumn(2).setPreferredWidth(190);
            tabelPetugas.getColumnModel().getColumn(3).setResizable(false);
            tabelPetugas.getColumnModel().getColumn(3).setPreferredWidth(200);
            tabelPetugas.getColumnModel().getColumn(4).setResizable(false);
            tabelPetugas.getColumnModel().getColumn(4).setPreferredWidth(160);
            tabelPetugas.getColumnModel().getColumn(5).setResizable(false);
            tabelPetugas.getColumnModel().getColumn(5).setPreferredWidth(0);
        }

        panelDaftarPetugas.add(scrollPanelTabelPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 70, 900, 300));

        buttonEdit.setBackground(new java.awt.Color(0, 112, 112));
        buttonEdit.setFont(new java.awt.Font("monogram", 0, 32)); // NOI18N
        buttonEdit.setForeground(new java.awt.Color(0, 143, 143));
        buttonEdit.setText("EDIT");
        buttonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditActionPerformed(evt);
            }
        });
        panelDaftarPetugas.add(buttonEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(795, 390, 150, 50));
        buttonEdit.setVisible(false);

        buttonHapus.setBackground(new java.awt.Color(0, 112, 112));
        buttonHapus.setFont(new java.awt.Font("monogram", 0, 32)); // NOI18N
        buttonHapus.setForeground(new java.awt.Color(255, 0, 51));
        buttonHapus.setText("HAPUS");
        buttonHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonHapusActionPerformed(evt);
            }
        });
        panelDaftarPetugas.add(buttonHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 390, 150, 50));
        buttonHapus.setVisible(false);

        textTotalPetugas.setFont(new java.awt.Font("monogram", 0, 24)); // NOI18N
        textTotalPetugas.setForeground(new java.awt.Color(150, 230, 230));
        textTotalPetugas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textTotalPetugas.setText("Total Petugas:");
        panelDaftarPetugas.add(textTotalPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 390, 300, 40));

        basePanel.add(panelDaftarPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, -1));

        panelManagePetugas.setBackground(new java.awt.Color(0, 153, 153));
        panelManagePetugas.setPreferredSize(new java.awt.Dimension(990, 460));
        panelManagePetugas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonKembaliEdit.setFont(new java.awt.Font("monogram", 0, 28)); // NOI18N
        buttonKembaliEdit.setForeground(new java.awt.Color(0, 103, 103));
        buttonKembaliEdit.setText("<< KEMBALI");
        buttonKembaliEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonKembaliEditActionPerformed(evt);
            }
        });
        panelManagePetugas.add(buttonKembaliEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 370, 150, 50));

        textTitlePanel.setFont(new java.awt.Font("monogram", 0, 48)); // NOI18N
        textTitlePanel.setForeground(new java.awt.Color(200, 255, 255));
        textTitlePanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textTitlePanel.setText("<html>\n<center>\nEDIT<br>PETUGAS\n</center>\n</html>");
        panelManagePetugas.add(textTitlePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 170, 90));

        imgEditPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arsip/surat/assets/images/petugas.png"))); // NOI18N
        panelManagePetugas.add(imgEditPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, -1));

        panelTambahPetugas.setBackground(new java.awt.Color(0, 143, 143));
        panelTambahPetugas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonTambah.setFont(new java.awt.Font("monogram", 0, 36)); // NOI18N
        buttonTambah.setForeground(new java.awt.Color(0, 103, 103));
        buttonTambah.setText("TAMBAH");
        buttonTambah.setNextFocusableComponent(buttonUbahPassword);
        buttonTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTambahActionPerformed(evt);
            }
        });
        panelTambahPetugas.add(buttonTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 360, 130, 50));

        teksBidang1.setBackground(new java.awt.Color(0, 143, 143));
        teksBidang1.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        teksBidang1.setForeground(new java.awt.Color(150, 255, 255));
        teksBidang1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teksBidang1.setText("Bidang");
        teksBidang1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelTambahPetugas.add(teksBidang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 50, 20));

        tf_panelBidang1.setBackground(new java.awt.Color(0, 143, 143));
        tf_panelBidang1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_panelBidang1.setPreferredSize(new java.awt.Dimension(270, 40));
        tf_panelBidang1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cb_tambahBidang.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        cb_tambahBidang.setForeground(new java.awt.Color(0, 102, 102));
        cb_tambahBidang.setMaximumRowCount(4);
        cb_tambahBidang.setBorder(null);
        cb_tambahBidang.setNextFocusableComponent(rb_admin);
        tf_panelBidang1.add(cb_tambahBidang, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 15, 280, 30));

        panelTambahPetugas.add(tf_panelBidang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 290, 50));

        teksLevel1.setBackground(new java.awt.Color(0, 143, 143));
        teksLevel1.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        teksLevel1.setForeground(new java.awt.Color(150, 255, 255));
        teksLevel1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teksLevel1.setText("Level");
        teksLevel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelTambahPetugas.add(teksLevel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 247, 50, 20));

        tf_panelLevel1.setBackground(new java.awt.Color(0, 143, 143));
        tf_panelLevel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_panelLevel1.setPreferredSize(new java.awt.Dimension(270, 40));
        tf_panelLevel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbTambah_admin.setBackground(new java.awt.Color(0, 143, 143));
        radioGroupLevel.add(rbTambah_admin);
        rbTambah_admin.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rbTambah_admin.setForeground(new java.awt.Color(255, 255, 255));
        rbTambah_admin.setText("Admin");
        rbTambah_admin.setNextFocusableComponent(rb_petugas);
        tf_panelLevel1.add(rbTambah_admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 140, 40));

        rbTambah_petugas.setBackground(new java.awt.Color(0, 143, 143));
        radioGroupLevel.add(rbTambah_petugas);
        rbTambah_petugas.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rbTambah_petugas.setForeground(new java.awt.Color(255, 255, 255));
        rbTambah_petugas.setText("Petugas");
        rbTambah_petugas.setNextFocusableComponent(buttonSimpan);
        tf_panelLevel1.add(rbTambah_petugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 5, 130, 40));

        panelTambahPetugas.add(tf_panelLevel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 257, 290, 50));

        tf_tambahUsername.setBackground(new java.awt.Color(0, 143, 143));
        tf_tambahUsername.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_tambahUsername.setForeground(new java.awt.Color(255, 255, 255));
        tf_tambahUsername.setText("Username");
        tf_tambahUsername.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_tambahUsername.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_tambahUsername.setNextFocusableComponent(cb_editBidang);
        panelTambahPetugas.add(tf_tambahUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, 290, 60));

        tf_tambahNip.setBackground(new java.awt.Color(0, 143, 143));
        tf_tambahNip.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_tambahNip.setForeground(new java.awt.Color(255, 255, 255));
        tf_tambahNip.setText("NIP");
        tf_tambahNip.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "NIP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_tambahNip.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_tambahNip.setNextFocusableComponent(tf_editNama);
        panelTambahPetugas.add(tf_tambahNip, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 290, 60));

        tf_tambahJabatan.setBackground(new java.awt.Color(0, 143, 143));
        tf_tambahJabatan.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_tambahJabatan.setForeground(new java.awt.Color(255, 255, 255));
        tf_tambahJabatan.setText("Jabatan");
        tf_tambahJabatan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Jabatan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_tambahJabatan.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_tambahJabatan.setNextFocusableComponent(tf_editUsername);
        panelTambahPetugas.add(tf_tambahJabatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 290, 60));

        tf_tambahNama.setBackground(new java.awt.Color(0, 143, 143));
        tf_tambahNama.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_tambahNama.setForeground(new java.awt.Color(255, 255, 255));
        tf_tambahNama.setText("Nama");
        tf_tambahNama.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Nama", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_tambahNama.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_tambahNama.setNextFocusableComponent(tf_editJabatan);
        panelTambahPetugas.add(tf_tambahNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 290, 60));

        jpf_tambahPassword.setBackground(new java.awt.Color(0, 143, 143));
        jpf_tambahPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jpf_tambahPassword.setForeground(new java.awt.Color(255, 255, 255));
        jpf_tambahPassword.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        panelTambahPetugas.add(jpf_tambahPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 150, 290, 60));

        buttonShowPassword1.setBackground(new java.awt.Color(0, 140, 140));
        buttonShowPassword1.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        buttonShowPassword1.setText("Tampilkan password");
        buttonShowPassword1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        buttonShowPassword1.setPreferredSize(new java.awt.Dimension(290, 23));
        buttonShowPassword1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowPassword1ActionPerformed(evt);
            }
        });
        panelTambahPetugas.add(buttonShowPassword1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 208, -1, -1));

        panelManagePetugas.add(panelTambahPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 710, 640));

        panelEditPetugas.setBackground(new java.awt.Color(0, 143, 143));
        panelEditPetugas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelEditPassword.setBackground(new java.awt.Color(0, 120, 120));
        panelEditPassword.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelEditPassword.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpf_editPassword.setBackground(new java.awt.Color(0, 120, 120));
        jpf_editPassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jpf_editPassword.setForeground(new java.awt.Color(255, 255, 255));
        jpf_editPassword.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        panelEditPassword.add(jpf_editPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 20, 240, 60));

        buttonShowPassword.setBackground(new java.awt.Color(0, 140, 140));
        buttonShowPassword.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        buttonShowPassword.setText("Tampilkan password");
        buttonShowPassword.setMargin(new java.awt.Insets(2, 2, 2, 2));
        buttonShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowPasswordActionPerformed(evt);
            }
        });
        panelEditPassword.add(buttonShowPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 80, 240, -1));

        buttonSimpanPassword.setFont(new java.awt.Font("monogram", 0, 36)); // NOI18N
        buttonSimpanPassword.setForeground(new java.awt.Color(0, 103, 103));
        buttonSimpanPassword.setText("UBAH");
        buttonSimpanPassword.setNextFocusableComponent(buttonUbahPassword);
        buttonSimpanPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSimpanPasswordActionPerformed(evt);
            }
        });
        panelEditPassword.add(buttonSimpanPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 130, 240, 50));

        panelEditPetugas.add(panelEditPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 290, 210));
        panelEditPassword.setVisible(false);

        buttonUbahPassword.setFont(new java.awt.Font("monogram", 0, 24)); // NOI18N
        buttonUbahPassword.setForeground(new java.awt.Color(80, 80, 80));
        buttonUbahPassword.setText("<html><center>UBAH<br>PASSWORD</center></html>");
        buttonUbahPassword.setNextFocusableComponent(buttonKembaliEdit);
        buttonUbahPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUbahPasswordActionPerformed(evt);
            }
        });
        panelEditPetugas.add(buttonUbahPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, 130, 50));

        buttonSimpan.setFont(new java.awt.Font("monogram", 0, 36)); // NOI18N
        buttonSimpan.setForeground(new java.awt.Color(0, 103, 103));
        buttonSimpan.setText("SIMPAN");
        buttonSimpan.setNextFocusableComponent(buttonUbahPassword);
        buttonSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSimpanActionPerformed(evt);
            }
        });
        panelEditPetugas.add(buttonSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 360, 130, 50));

        teksBidang.setBackground(new java.awt.Color(0, 143, 143));
        teksBidang.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        teksBidang.setForeground(new java.awt.Color(150, 255, 255));
        teksBidang.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teksBidang.setText("Bidang");
        teksBidang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelEditPetugas.add(teksBidang, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 147, 50, 20));

        tf_panelBidang.setBackground(new java.awt.Color(0, 143, 143));
        tf_panelBidang.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_panelBidang.setPreferredSize(new java.awt.Dimension(270, 40));
        tf_panelBidang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cb_editBidang.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        cb_editBidang.setForeground(new java.awt.Color(0, 102, 102));
        cb_editBidang.setMaximumRowCount(4);
        cb_editBidang.setBorder(null);
        cb_editBidang.setNextFocusableComponent(rb_admin);
        tf_panelBidang.add(cb_editBidang, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 15, 280, 30));

        panelEditPetugas.add(tf_panelBidang, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 157, 290, 50));

        teksLevel.setBackground(new java.awt.Color(0, 143, 143));
        teksLevel.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        teksLevel.setForeground(new java.awt.Color(150, 255, 255));
        teksLevel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        teksLevel.setText("Level");
        teksLevel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panelEditPetugas.add(teksLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 247, 50, 20));

        tf_panelLevel.setBackground(new java.awt.Color(0, 143, 143));
        tf_panelLevel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        tf_panelLevel.setPreferredSize(new java.awt.Dimension(270, 40));
        tf_panelLevel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rb_admin.setBackground(new java.awt.Color(0, 143, 143));
        radioGroupLevel.add(rb_admin);
        rb_admin.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_admin.setForeground(new java.awt.Color(255, 255, 255));
        rb_admin.setText("Admin");
        rb_admin.setNextFocusableComponent(rb_petugas);
        tf_panelLevel.add(rb_admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 140, 40));

        rb_petugas.setBackground(new java.awt.Color(0, 143, 143));
        radioGroupLevel.add(rb_petugas);
        rb_petugas.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        rb_petugas.setForeground(new java.awt.Color(255, 255, 255));
        rb_petugas.setText("Petugas");
        rb_petugas.setNextFocusableComponent(buttonSimpan);
        tf_panelLevel.add(rb_petugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 5, 130, 40));

        panelEditPetugas.add(tf_panelLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 257, 290, 50));

        tf_editUsername.setBackground(new java.awt.Color(0, 143, 143));
        tf_editUsername.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_editUsername.setForeground(new java.awt.Color(255, 255, 255));
        tf_editUsername.setText("Username");
        tf_editUsername.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_editUsername.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_editUsername.setNextFocusableComponent(cb_editBidang);
        panelEditPetugas.add(tf_editUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, 290, 60));

        tf_editNip.setBackground(new java.awt.Color(0, 143, 143));
        tf_editNip.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_editNip.setForeground(new java.awt.Color(255, 255, 255));
        tf_editNip.setText("NIP");
        tf_editNip.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "NIP", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_editNip.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_editNip.setNextFocusableComponent(tf_editNama);
        panelEditPetugas.add(tf_editNip, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 290, 60));

        tf_editJabatan.setBackground(new java.awt.Color(0, 143, 143));
        tf_editJabatan.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_editJabatan.setForeground(new java.awt.Color(255, 255, 255));
        tf_editJabatan.setText("Jabatan");
        tf_editJabatan.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Jabatan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_editJabatan.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_editJabatan.setNextFocusableComponent(tf_editUsername);
        panelEditPetugas.add(tf_editJabatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 290, 60));

        tf_editNama.setBackground(new java.awt.Color(0, 143, 143));
        tf_editNama.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tf_editNama.setForeground(new java.awt.Color(255, 255, 255));
        tf_editNama.setText("Nama");
        tf_editNama.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Nama", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 0, 14), new java.awt.Color(150, 255, 255))); // NOI18N
        tf_editNama.setMargin(new java.awt.Insets(2, 10, 2, 10));
        tf_editNama.setNextFocusableComponent(tf_editJabatan);
        panelEditPetugas.add(tf_editNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 290, 60));

        panelManagePetugas.add(panelEditPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 710, 640));

        basePanel.add(panelManagePetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, -1, 640));
        panelManagePetugas.setVisible(false);

        add(basePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRefreshDataMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonRefreshDataMouseEntered
        buttonRefreshData.setBackground(new Color(252, 252, 252));
    }//GEN-LAST:event_buttonRefreshDataMouseEntered

    private void buttonRefreshDataMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonRefreshDataMouseExited
        buttonRefreshData.setBackground(new Color(232, 232, 232));
    }//GEN-LAST:event_buttonRefreshDataMouseExited

    private void buttonRefreshDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonRefreshDataMouseClicked
        if (!thread.isAlive()) {
            thread = new Thread("thread-refresh-petugas") {
                @Override
                public void run() {
                    syncListBidang();
                    setModelBidang();
                    syncListPetugas();
                    setModelPetugas();
                }
            };
            startThread();
        }
    }//GEN-LAST:event_buttonRefreshDataMouseClicked

    private void buttonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditActionPerformed
        panelMenu.setPreferredSize(new Dimension(990, 70));
        textTitlePanel.setText("<html> <center> EDIT<br>PETUGAS </center> </html>");
        panelManagePetugas.setVisible(true);
        panelTambahPetugas.setVisible(false);
        panelEditPetugas.setVisible(true);
        panelDaftarPetugas.setVisible(false);
        Optional<Petugas> petugas = petugasService.selectPetugasByNIP(selectedPetugas);
        setEditTextField(petugas);
    }//GEN-LAST:event_buttonEditActionPerformed

    private void buttonKembaliEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonKembaliEditActionPerformed
        panelMenu.setPreferredSize(new Dimension(990, 250));
        panelManagePetugas.setVisible(false);
        panelEditPetugas.setVisible(false);
        panelTambahPetugas.setVisible(false);
        panelDaftarPetugas.setVisible(true);
        setEditTextField();
    }//GEN-LAST:event_buttonKembaliEditActionPerformed

    private void buttonSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSimpanActionPerformed
        Petugas editedPetugas = new Petugas();                                  // membuat object petugas
        editedPetugas.setNip(tf_editNip.getText());                             // set NIP petugas
        editedPetugas.setNama(tf_editNama.getText());                           // set Nama petugas
        editedPetugas.setJabatan(tf_editJabatan.getText());                     // set Jabatan petugas
        editedPetugas.setUsername(tf_editUsername.getText());                   // set Username petugas
        editedPetugas.setLevel(rb_admin.isSelected() ? "admin" : "petugas");    // set level petugas
        editedPetugas
                .setNama_bidang(cb_editBidang.getSelectedItem().toString());    // set bidang petugas
        int updateStatus = petugasService.updatePetugasByNIP(selectedPetugas, editedPetugas);
        switch (updateStatus) {
            case 0: { // menampilkan Popup pesan gagal
                JOptionPane jOptionPane = new JOptionPane(
                        "Gagal mengupdate data, silahkan ulangi kembali!",
                        JOptionPane.ERROR_MESSAGE);
                JDialog jDialog = jOptionPane.createDialog(panelManagePetugas, "Gagal!");
                jDialog.setModal(false);
                jDialog.setVisible(true);
                new Timer(3000, (ActionEvent e) -> {
                    jDialog.setVisible(false);
                }).start();
            }
            break;
            case 1: { // menampilkan Popup pesan berhasil
                JOptionPane jOptionPane = new JOptionPane(
                        "Berhasil mengupdate data!",
                        JOptionPane.INFORMATION_MESSAGE);
                JDialog jDialog = jOptionPane.createDialog(panelManagePetugas, "Sukses!");
                jDialog.setModal(false);
                jDialog.setVisible(true);
                new Timer(3000, (ActionEvent e) -> {
                    jDialog.setVisible(false);
                }).start();
            }
            break;
        }
    }//GEN-LAST:event_buttonSimpanActionPerformed

    private void buttonUbahPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUbahPasswordActionPerformed
        if (buttonUbahPassword.getText().equalsIgnoreCase("<html><center>UBAH<br>PASSWORD</center></html>")) {
            buttonUbahPassword.setText("BATAL");
            panelEditPassword.setVisible(true);
        } else {
            buttonUbahPassword.setText("<html><center>UBAH<br>PASSWORD</center></html>");
            panelEditPassword.setVisible(false);
            jpf_editPassword.setText("");
        }
    }//GEN-LAST:event_buttonUbahPasswordActionPerformed

    private void buttonShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowPasswordActionPerformed
        if (buttonShowPassword.getText().equalsIgnoreCase("Tampilkan password")) {
            buttonShowPassword.setText("Sembunyikan password");
            jpf_editPassword.setEchoChar((char) 0);
        } else {
            buttonShowPassword.setText("Tampilkan password");
            jpf_editPassword.setEchoChar('\u25cf');
        }
    }//GEN-LAST:event_buttonShowPasswordActionPerformed

    private void buttonSimpanPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSimpanPasswordActionPerformed
        String newPassword = String.valueOf(jpf_editPassword.getPassword());
        petugasService.updatePasswordByNIP(selectedPetugas, newPassword);
    }//GEN-LAST:event_buttonSimpanPasswordActionPerformed

    private void buttonTambahDataMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonTambahDataMouseEntered
        buttonTambahData.setBackground(new Color(252, 252, 252));
    }//GEN-LAST:event_buttonTambahDataMouseEntered

    private void buttonTambahDataMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonTambahDataMouseExited
        buttonTambahData.setBackground(new Color(232, 232, 232));
    }//GEN-LAST:event_buttonTambahDataMouseExited

    private void buttonTambahDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonTambahDataMouseClicked
        panelMenu.setPreferredSize(new Dimension(990, 70));
        textTitlePanel.setText("<html> <center> TAMBAH<br>PETUGAS </center> </html>");
        panelManagePetugas.setVisible(true);
        panelTambahPetugas.setVisible(true);
        panelEditPetugas.setVisible(false);
        panelDaftarPetugas.setVisible(false);
        setEditTextField();
    }//GEN-LAST:event_buttonTambahDataMouseClicked

    private void buttonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonHapusActionPerformed
        Optional<Petugas> petugas = petugasService.selectPetugasByNIP(selectedPetugas);
        int hapus = JOptionPane.showConfirmDialog(
                panelDaftarPetugas,
                "Apakah anda yakin ingin menghapus " + petugas.get().getNama() + " dari daftar Petugas?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        switch (hapus) {
            case 0: // Yes Option
                petugasService.deletePetugasByNIP(selectedPetugas);
                break;
        }
        buttonRefreshDataMouseClicked(null);
    }//GEN-LAST:event_buttonHapusActionPerformed

    private void buttonShowPassword1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowPassword1ActionPerformed
        if (buttonShowPassword1.getText().equalsIgnoreCase("Tampilkan password")) {
            buttonShowPassword1.setText("Sembunyikan password");
            jpf_tambahPassword.setEchoChar((char) 0);
        } else {
            buttonShowPassword1.setText("Tampilkan password");
            jpf_tambahPassword.setEchoChar('\u25cf');
        }
    }//GEN-LAST:event_buttonShowPassword1ActionPerformed

    private void buttonTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTambahActionPerformed
        Petugas newPetugas = new Petugas();                                     // Membuat object Petugas baru
        newPetugas.setNip(tf_tambahNip.getText());                              // set NIP petugas baru
        newPetugas.setNama(tf_tambahNama.getText());                            // set Nama petugas baru
        newPetugas.setJabatan(tf_tambahJabatan.getText());                      // set Jabatan petugas baru
        newPetugas.setUsername(tf_tambahUsername.getText());                    // set Username petugas baru
        String plainPassword = 
                String.valueOf(jpf_tambahPassword.getPassword());               // ambil password dari Password Field
        String salt = newPetugas.getUsername() + " " + plainPassword;           // buat salt dari username dan password
        byte[] encryptSalt = salt.getBytes();                                   // konversi string menjadi byte[]
        salt = Base64.getEncoder().encodeToString(encryptSalt);                 // encode salt dengan base64
        String encryptPassword = 
                PasswordUtil.generateSecurePassword(plainPassword, salt);       // membuat inputan password dienkripsi
        newPetugas.setPassword(encryptPassword);                                // set Password petugas baru
        newPetugas
                .setNama_bidang(cb_tambahBidang.getSelectedItem().toString());  // set bidang petugas baru
        newPetugas.setLevel(rbTambah_admin.isSelected() ? "admin" : "petugas"); // set level petugas baru
        int statusTambah = petugasService.insertPetugas(newPetugas);
        switch (statusTambah) {
            case 0: { // menampilkan popup pesan gagal
                JOptionPane jOptionPane = new JOptionPane(
                        "Gagal menambah data, silahkan ulangi kembali!",
                        JOptionPane.ERROR_MESSAGE);
                JDialog jDialog = jOptionPane.createDialog(panelManagePetugas, "Gagal!");
                jDialog.setModal(false);
                jDialog.setVisible(true);
                new Timer(3000, (ActionEvent e) -> {
                    jDialog.setVisible(false);
                }).start();
            }
            break;
            case 1: { // menampilkan popup pesan berhasil
                JOptionPane jOptionPane = new JOptionPane(
                        "Berhasil menambah data!",
                        JOptionPane.INFORMATION_MESSAGE);
                JDialog jDialog = jOptionPane.createDialog(panelManagePetugas, "Sukses!");
                jDialog.setModal(false);
                jDialog.setVisible(true);
                new Timer(3000, (ActionEvent e) -> {
                    jDialog.setVisible(false);
                }).start();
            }
            break;
        }
    }//GEN-LAST:event_buttonTambahActionPerformed

    private void buttonKembaliBantuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonKembaliBantuanActionPerformed
        panelBantuan.setVisible(false);
        panelMainButton.setVisible(true);
    }//GEN-LAST:event_buttonKembaliBantuanActionPerformed

    private void buttonInfoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonInfoMouseEntered
        buttonInfo.setBackground(new Color(252, 252, 252));
    }//GEN-LAST:event_buttonInfoMouseEntered

    private void buttonInfoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonInfoMouseExited
        buttonInfo.setBackground(new Color(232, 232, 232));
    }//GEN-LAST:event_buttonInfoMouseExited

    private void buttonInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonInfoMouseClicked
        panelMainButton.setVisible(false);
        panelBantuan.setVisible(true);
    }//GEN-LAST:event_buttonInfoMouseClicked

    // <editor-fold defaultstate="collapsed" desc="Fungsi untuk mengelola Text Field pada panel Edit Petugas">
    /**
     * Fungsi untuk mengubah text field menjadi default
     */
    private void setEditTextField() {
        tf_editJabatan.setText("jabatan");
        tf_editNama.setText("nama");
        tf_editNip.setText("nip");
        tf_editUsername.setText("username");
        tf_tambahNama.setText("");
        tf_tambahJabatan.setText("");
        tf_tambahNip.setText("");
        tf_tambahUsername.setText("");
        cb_tambahBidang.setSelectedIndex(-1);
        radioGroupLevel.clearSelection();
        cb_editBidang.setSelectedIndex(-1);
    }

    /**
     * Fungsi untuk mengubah text field sesuai data petugas
     *
     * @param petugas
     */
    private void setEditTextField(Optional<Petugas> petugas) {
        tf_editJabatan.setText(petugas.get().getJabatan());
        tf_editNama.setText(petugas.get().getNama());
        tf_editNip.setText(petugas.get().getNip());
        tf_editUsername.setText(petugas.get().getUsername());
        switch (petugas.get().getLevel()) {
            case "admin":
                rb_admin.setSelected(true);
                break;
            case "petugas":
                rb_petugas.setSelected(true);
                break;
            default:
                radioGroupLevel.clearSelection();
        }
        cb_editBidang.setSelectedItem(petugas.get().getNama_bidang());
    } // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Deklarasi Variabel">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel basePanel;
    private javax.swing.JButton buttonEdit;
    private javax.swing.JButton buttonHapus;
    private javax.swing.JPanel buttonInfo;
    private javax.swing.JButton buttonKembaliBantuan;
    private javax.swing.JButton buttonKembaliEdit;
    private javax.swing.JPanel buttonRefreshData;
    private javax.swing.JButton buttonShowPassword;
    private javax.swing.JButton buttonShowPassword1;
    private javax.swing.JButton buttonSimpan;
    private javax.swing.JButton buttonSimpanPassword;
    private javax.swing.JButton buttonTambah;
    private javax.swing.JPanel buttonTambahData;
    private javax.swing.JButton buttonUbahPassword;
    private javax.swing.JComboBox<String> cb_editBidang;
    private javax.swing.JComboBox<String> cb_tambahBidang;
    private javax.swing.JPanel headBidang;
    private javax.swing.JPanel headJabatan;
    private javax.swing.JPanel headLevel;
    private javax.swing.JPanel headNama;
    private javax.swing.JPanel headNomer;
    private javax.swing.JLabel headTextBidang;
    private javax.swing.JLabel headTextJabatan;
    private javax.swing.JLabel headTextLevel;
    private javax.swing.JLabel headTextNama;
    private javax.swing.JLabel headTextNomor;
    private javax.swing.JLabel iconInfo;
    private javax.swing.JLabel iconRefreshData;
    private javax.swing.JLabel iconTambahData;
    private javax.swing.JLabel imgEditPetugas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jpf_editPassword;
    private javax.swing.JPasswordField jpf_tambahPassword;
    private javax.swing.JPanel panelBantuan;
    private javax.swing.JPanel panelDaftarPetugas;
    private javax.swing.JPanel panelEditPassword;
    private javax.swing.JPanel panelEditPetugas;
    private javax.swing.JPanel panelMainButton;
    private javax.swing.JPanel panelManagePetugas;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelTambahPetugas;
    private javax.swing.ButtonGroup radioGroupLevel;
    private javax.swing.JRadioButton rbTambah_admin;
    private javax.swing.JRadioButton rbTambah_petugas;
    private javax.swing.JRadioButton rb_admin;
    private javax.swing.JRadioButton rb_petugas;
    private javax.swing.JScrollPane scrollPanelTabelPetugas;
    private javax.swing.JSeparator separatorTop;
    private javax.swing.JTable tabelPetugas;
    private javax.swing.JTextField teksBidang;
    private javax.swing.JTextField teksBidang1;
    private javax.swing.JTextField teksLevel;
    private javax.swing.JTextField teksLevel1;
    private javax.swing.JLabel textInfo;
    private javax.swing.JLabel textMenu;
    private javax.swing.JLabel textMenu1;
    private javax.swing.JLabel textRefreshData;
    private javax.swing.JLabel textTambahData;
    private javax.swing.JLabel textTitlePanel;
    private javax.swing.JLabel textTotalPetugas;
    private javax.swing.JTextField tf_editJabatan;
    private javax.swing.JTextField tf_editNama;
    private javax.swing.JTextField tf_editNip;
    private javax.swing.JTextField tf_editUsername;
    private javax.swing.JPanel tf_panelBidang;
    private javax.swing.JPanel tf_panelBidang1;
    private javax.swing.JPanel tf_panelLevel;
    private javax.swing.JPanel tf_panelLevel1;
    private javax.swing.JTextField tf_tambahJabatan;
    private javax.swing.JTextField tf_tambahNama;
    private javax.swing.JTextField tf_tambahNip;
    private javax.swing.JTextField tf_tambahUsername;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
