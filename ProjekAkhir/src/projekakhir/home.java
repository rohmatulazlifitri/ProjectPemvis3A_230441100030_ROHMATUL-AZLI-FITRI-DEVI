/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projekakhir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.util.ArrayList;
import javax.swing.DefaultListModel;


/**
 *
 * @author azlif
 */
public class home extends javax.swing.JFrame {
    Connection conn;
//    private String namauser;
    private DefaultTableModel model;
    private DefaultTableModel model1;
    private DefaultTableModel model2;
    private JTabbedPane data;
    private ArrayList<String[]> dataList = new ArrayList<>(); // List untuk menyimpan data
//    private DefaultListModel<String> listModel = new DefaultListModel<>(); // Model untuk JList

    String nama,tgllahir,alamat,jeniskelamin,email,notlp;
    
  
    public home() {
//        this.namaUser = nama;
        initComponents();
//        namaF.setText(register.nama+ "!");
//        namaF.setText(namaUser);
        conn = (Connection) koneksi.getkoneksi();;
        
        model = new DefaultTableModel();
        tbl_rekamMedis.setModel(model);
        
        model.addColumn("ID");
        model.addColumn("RIWAYAT PENYAKIT");
        model.addColumn("OBAT YANG DIKONSUMSI");
        model.addColumn("PENYAKIT YANG PERNAH DIALAMI");
        model.addColumn("ALERGI");
        
        loadDataMedis();
        
        model1 = new DefaultTableModel();
        tbl_bb.setModel(model1);
        
        model1.addColumn("NO");
        model1.addColumn("BB");
        model1.addColumn("TB");
        model1.addColumn("GENDER");
        model1.addColumn("KRITERIA");
        model1.addColumn("REKOMENDASI");
        model1.addColumn("TANGGAL");
        
        loadDataRekomendasi();
        
        model2 = new DefaultTableModel();
        tbl_KG.setModel(model2);
        
        model2.addColumn("NO");
        model2.addColumn("KADAR GULA");
        model2.addColumn("TIPE TES");
        model2.addColumn("KATEGORI");
        model2.addColumn("TANGGAL");
        
        loadDataKadargula();
        
    }
    
//    
    
    private void loadDataMedis() {
      model.setRowCount(0);

      try {
          String sql = "SELECT * FROM rekam_medis";
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
             model.addRow(new Object[]{
             rs.getInt("id"),
             rs.getString("riwayat_penyakit"),
             rs.getString("obat_yang_dikonsumsi"),
             rs.getString("penyakit_pernah_dialami"),
             rs.getString("alergi"),
           });
          }
      } catch (SQLException e) {
         System.out.println("Error Save Data" + e.getMessage());
       }
    }
    
    private void loadDataRekomendasi() {
        try {
            String sql = "SELECT * FROM rekomendasi";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model1 = (DefaultTableModel) tbl_bb.getModel();
            model1.setRowCount(0);

            while (rs.next()) {
                model1.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getInt("BB"),
                    rs.getInt("TB"),
                    rs.getString("gender"),
                    rs.getString("kriteria"),
                    rs.getString("rekomendasi"),
                    rs.getDate("tanggal")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadDataKadargula() {
        try {
            String sql = "SELECT * FROM kadargula";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model2 = (DefaultTableModel) tbl_KG.getModel();
            model2.setRowCount(0);

            while (rs.next()) {
                model2.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("kadar"),
                    rs.getString("tipe"),
                    rs.getString("kategori"),
                    rs.getDate("tanggal")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void cetakKePDF() {
    // Tampilkan dialog input untuk memasukkan nama file
    String fileName = JOptionPane.showInputDialog(null, "Masukkan nama file PDF:", "Nama File", JOptionPane.PLAIN_MESSAGE);

    // Jika pengguna menekan "Cancel" atau tidak mengisi nama, batalkan proses
    if (fileName == null || fileName.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pembuatan PDF dibatalkan.");
        return;
    }

    if (!fileName.toLowerCase().endsWith(".pdf")) {
        fileName += ".pdf";
    }

    String filePath = "C:/Users/azlif/Downloads/" + fileName;

    Document document = new Document(PageSize.A4);

    try {
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Judul PDF
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph title = new Paragraph("Laporan Rekam Medis", fontHeader);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        // Membuat tabel dengan jumlah kolom yang sesuai
        PdfPTable table = new PdfPTable(5); 
        table.setWidthPercentage(100);

        // Menambahkan header ke tabel PDF
        String[] header = {"ID", "Riwayat Penyakit", "Obat yang Dikonsumsi", "Penyakit yang Pernah Dialami", "Alergi"};
        for (String column : header) {
            PdfPCell cell = new PdfPCell(new Phrase(column, fontHeader));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Menambahkan data dari model tabel ke tabel PDF
        for (int i = 0; i < model.getRowCount(); i++) {
            Object idObj = model.getValueAt(i, 0);
            Object riwayatPenyakitObj = model.getValueAt(i, 1);
            Object obatYangDikonsumsiObj = model.getValueAt(i, 2);
            Object penyakitPernahDialamiObj = model.getValueAt(i, 3);
            Object alergiObj = model.getValueAt(i, 4);

            // Ambil nilai-nilai dari objek sesuai dengan tipe data di database
            int id = (idObj instanceof Integer) ? (int) idObj : 0;
            String riwayatPenyakit = (riwayatPenyakitObj instanceof String) ? (String) riwayatPenyakitObj : "";
            String obatYangDikonsumsi = (obatYangDikonsumsiObj instanceof String) ? (String) obatYangDikonsumsiObj : "";
            String penyakitPernahDialami = (penyakitPernahDialamiObj instanceof String) ? (String) penyakitPernahDialamiObj : "";
            String alergi = (alergiObj instanceof String) ? (String) alergiObj : "";

            // Tambahkan data ke tabel sesuai kolom di database
            table.addCell(String.valueOf(id));
            table.addCell(riwayatPenyakit);
            table.addCell(obatYangDikonsumsi);
            table.addCell(penyakitPernahDialami);
            table.addCell(alergi);
        }

        document.add(table); // Tambahkan tabel ke dokumen
        JOptionPane.showMessageDialog(null, "PDF berhasil disimpan di lokasi: " + filePath);

    } catch (DocumentException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF: " + e.getMessage());
    } finally {
        document.close();
    }
}
    
    
    private void kosong(){
        tf_id.setText("");
        tf_riwayatP.setText("");
        tf_obat.setText("");
        tf_penyakitdialami.setText("");
        tf_alergi.setText("");
    }
    
    private void kosong1(){
        tf_nama.setText("");
        tf_tgllahir.setText("");
        tf_alamat.setText("");
        tf_tlp.setText("");
        tf_email.setText("");
        buttonGroup1.clearSelection();
        cb_setuju.setSelected(false);
    }
    
    private void resetForm() {
        tf_bb.setText("");
        tf_tb.setText("");
        tf_hasil.setText("");
        rb_laki.setSelected(false);
        rb_pr.setSelected(false);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        menupanel = new javax.swing.JPanel();
        btn_profil = new javax.swing.JButton();
        btn_kesehatan = new javax.swing.JButton();
        btn_medis = new javax.swing.JButton();
        btn_logout = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        btn_beranda = new javax.swing.JButton();
        mainpanel = new javax.swing.JPanel();
        homepanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        profilpanel = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        tf_nama = new javax.swing.JTextField();
        tf_tgllahir = new javax.swing.JTextField();
        rb_cowok = new javax.swing.JRadioButton();
        rb_cewek = new javax.swing.JRadioButton();
        tf_alamat = new javax.swing.JTextField();
        tf_tlp = new javax.swing.JTextField();
        tf_email = new javax.swing.JTextField();
        btn_simpanDataDiri = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        cb_setuju = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        btn_keluar = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tf_namaT = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        tf_tglT = new javax.swing.JTextField();
        tf_jeniskelaminT = new javax.swing.JTextField();
        tf_alamatT = new javax.swing.JTextField();
        tf_notlpT = new javax.swing.JTextField();
        tf_emailT = new javax.swing.JTextField();
        kesehatanpanel = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        kesehatanpanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        tf_bb = new javax.swing.JTextField();
        tf_tb = new javax.swing.JTextField();
        tf_hasil = new javax.swing.JTextField();
        btn_cekBB = new javax.swing.JButton();
        btn_resetBB = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        rb_laki = new javax.swing.JRadioButton();
        rb_pr = new javax.swing.JRadioButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_bb = new javax.swing.JTable();
        kesehatanpanel2 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        tf_kadargula1 = new javax.swing.JTextField();
        tf_hasilKG1 = new javax.swing.JTextField();
        btn_cekKG1 = new javax.swing.JButton();
        btn_resetKG1 = new javax.swing.JButton();
        jLabel70 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        cb_tipetes = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_KG = new javax.swing.JTable();
        rekampanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        tf_obat = new javax.swing.JTextField();
        tf_penyakitdialami = new javax.swing.JTextField();
        tf_riwayatP = new javax.swing.JTextField();
        tf_alergi = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_rekamMedis = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_cetak = new javax.swing.JButton();
        tf_id = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        berandapanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(54, 51, 76));

        menupanel.setBackground(new java.awt.Color(101, 105, 111));
        menupanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        btn_profil.setBackground(new java.awt.Color(204, 51, 0));
        btn_profil.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_profil.setForeground(new java.awt.Color(204, 204, 204));
        btn_profil.setText("PROFIL");
        btn_profil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_profil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_profilMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_profilMouseEntered(evt);
            }
        });
        btn_profil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_profilActionPerformed(evt);
            }
        });

        btn_kesehatan.setBackground(new java.awt.Color(204, 51, 0));
        btn_kesehatan.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_kesehatan.setForeground(new java.awt.Color(204, 204, 204));
        btn_kesehatan.setText("KESEHATAN HARIAN");
        btn_kesehatan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_kesehatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_kesehatanMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_kesehatanMouseEntered(evt);
            }
        });
        btn_kesehatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_kesehatanActionPerformed(evt);
            }
        });

        btn_medis.setBackground(new java.awt.Color(204, 51, 0));
        btn_medis.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_medis.setForeground(new java.awt.Color(204, 204, 204));
        btn_medis.setText("REKAM MEDIS");
        btn_medis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_medis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_medisMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_medisMouseEntered(evt);
            }
        });
        btn_medis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_medisActionPerformed(evt);
            }
        });

        btn_logout.setBackground(new java.awt.Color(204, 51, 0));
        btn_logout.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_logout.setForeground(new java.awt.Color(204, 204, 204));
        btn_logout.setText("LOGOUT");
        btn_logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_logoutMouseEntered(evt);
            }
        });
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_3-removebg-preview (1).png")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Chiller", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ayoo Cek Kesehatanmu");

        jLabel3.setFont(new java.awt.Font("Chiller", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Disinii");

        jLabel57.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_12-removebg-preview (2).png")); // NOI18N

        btn_beranda.setBackground(new java.awt.Color(204, 51, 0));
        btn_beranda.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_beranda.setForeground(new java.awt.Color(204, 204, 204));
        btn_beranda.setText("BERANDA");
        btn_beranda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_beranda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_berandaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_berandaMouseEntered(evt);
            }
        });
        btn_beranda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_berandaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menupanelLayout = new javax.swing.GroupLayout(menupanel);
        menupanel.setLayout(menupanelLayout);
        menupanelLayout.setHorizontalGroup(
            menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menupanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menupanelLayout.createSequentialGroup()
                        .addGap(0, 9, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(14, 14, 14))
                    .addGroup(menupanelLayout.createSequentialGroup()
                        .addGroup(menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_profil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_logout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_beranda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_medis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_kesehatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(menupanelLayout.createSequentialGroup()
                .addGroup(menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menupanelLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel3))
                    .addGroup(menupanelLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel2))
                    .addGroup(menupanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel57)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        menupanelLayout.setVerticalGroup(
            menupanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menupanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(btn_beranda)
                .addGap(18, 18, 18)
                .addComponent(btn_profil)
                .addGap(18, 18, 18)
                .addComponent(btn_kesehatan)
                .addGap(18, 18, 18)
                .addComponent(btn_medis)
                .addGap(18, 18, 18)
                .addComponent(btn_logout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        mainpanel.setBackground(new java.awt.Color(82, 123, 183));
        mainpanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        mainpanel.setLayout(new java.awt.CardLayout());

        homepanel.setBackground(new java.awt.Color(60, 130, 172));
        homepanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homepanelMouseClicked(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_6__1_-removebg-preview.png")); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel7.setText("SELAMAT DATANG");

        jLabel83.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel83.setText("Dii");

        jLabel84.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel84.setText("APLIKASI PEMANTAU KESEHATAN");

        javax.swing.GroupLayout homepanelLayout = new javax.swing.GroupLayout(homepanel);
        homepanel.setLayout(homepanelLayout);
        homepanelLayout.setHorizontalGroup(
            homepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homepanelLayout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addGroup(homepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel7))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(homepanelLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel84)
                .addContainerGap(83, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homepanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel83)
                .addGap(304, 304, 304))
        );
        homepanelLayout.setVerticalGroup(
            homepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homepanelLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel83)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addGap(26, 26, 26)
                .addComponent(jLabel84)
                .addContainerGap(123, Short.MAX_VALUE))
        );

        mainpanel.add(homepanel, "card2");

        profilpanel.setBackground(new java.awt.Color(54, 51, 76));

        jPanel10.setBackground(new java.awt.Color(125, 79, 79));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel7.setBackground(new java.awt.Color(135, 119, 119));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel33.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 36)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("INPUT DATA DIRI KAMU");

        jLabel39.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        jLabel40.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addComponent(jLabel39)
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(8, 8, 8)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nama");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Tanggal Lahir");

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Jenis Kelamin");

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Alamat");

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("No.Telp");

        jLabel37.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Email");

        buttonGroup1.add(rb_cowok);
        rb_cowok.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        rb_cowok.setForeground(new java.awt.Color(255, 255, 255));
        rb_cowok.setText("Laki  - Laki");

        buttonGroup1.add(rb_cewek);
        rb_cewek.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        rb_cewek.setForeground(new java.awt.Color(255, 255, 255));
        rb_cewek.setText("Perempuan");
        rb_cewek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_cewekActionPerformed(evt);
            }
        });

        btn_simpanDataDiri.setBackground(new java.awt.Color(135, 119, 119));
        btn_simpanDataDiri.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btn_simpanDataDiri.setText("SIMPAN");
        btn_simpanDataDiri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanDataDiriActionPerformed(evt);
            }
        });

        jLabel38.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_10-removebg-preview (2).png")); // NOI18N

        cb_setuju.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cb_setuju.setForeground(new java.awt.Color(255, 255, 255));
        cb_setuju.setText("Data yang saya masukkan sudah benar");
        cb_setuju.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_setujuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tf_nama, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(tf_tgllahir)
                                    .addComponent(tf_alamat)
                                    .addComponent(tf_tlp)
                                    .addComponent(tf_email)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(rb_cowok)
                                        .addGap(23, 23, 23)
                                        .addComponent(rb_cewek, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(cb_setuju, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_simpanDataDiri)
                        .addGap(113, 113, 113)))
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(tf_nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tf_tgllahir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(rb_cowok)
                            .addComponent(rb_cewek))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(tf_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(tf_tlp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_setuju)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_simpanDataDiri)
                        .addContainerGap(27, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Input Data", jPanel9);

        jPanel12.setBackground(new java.awt.Color(125, 79, 79));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel20.setBackground(new java.awt.Color(135, 119, 119));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel56.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 36)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("TAMPILAN DATA");

        jLabel60.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        jLabel61.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addComponent(jLabel56)
                .addGap(27, 27, 27)
                .addComponent(jLabel60)
                .addContainerGap(132, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addGap(25, 25, 25)
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(jLabel56))))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        btn_keluar.setBackground(new java.awt.Color(54, 51, 76));
        btn_keluar.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btn_keluar.setForeground(new java.awt.Color(255, 255, 255));
        btn_keluar.setText("KELUAR");
        btn_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_keluarActionPerformed(evt);
            }
        });

        jLabel42.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\suster_2__1_-removebg-preview.png")); // NOI18N

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("nama");

        jLabel78.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Tanggal Lahir");

        jLabel79.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Jenis Kelamin");

        jLabel80.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Alamat");

        jLabel81.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("No.Telp");

        jLabel82.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("Email");

        tf_emailT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_emailTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_notlpT, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                            .addComponent(tf_alamatT)
                            .addComponent(tf_jeniskelaminT)
                            .addComponent(tf_tglT)
                            .addComponent(tf_namaT)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel80, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel81, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel79, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel78, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tf_emailT, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_keluar)))
                .addGap(80, 80, 80))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tf_namaT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel42))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel78)
                            .addComponent(tf_tglT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_jeniskelaminT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel79))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_alamatT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel80))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_notlpT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel81))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_emailT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel82))
                        .addGap(18, 18, 18)
                        .addComponent(btn_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Data", jPanel12);

        javax.swing.GroupLayout profilpanelLayout = new javax.swing.GroupLayout(profilpanel);
        profilpanel.setLayout(profilpanelLayout);
        profilpanelLayout.setHorizontalGroup(
            profilpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        profilpanelLayout.setVerticalGroup(
            profilpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilpanelLayout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        mainpanel.add(profilpanel, "card3");

        kesehatanpanel.setBackground(new java.awt.Color(91, 183, 39));
        kesehatanpanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        kesehatanpanel1.setBackground(new java.awt.Color(91, 183, 39));
        kesehatanpanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel8.setBackground(new java.awt.Color(231, 248, 236));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel41.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel41.setText("CEK BADAN IDEAL KAMU !");

        jLabel59.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel59)
                .addContainerGap(559, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel41)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(82, 137, 90));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel51.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel51.setText("BB");

        jLabel52.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel52.setText("TB");

        jLabel53.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel53.setText("Hasil");

        tf_tb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_tbActionPerformed(evt);
            }
        });

        btn_cekBB.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_cekBB.setText("CEK");
        btn_cekBB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cekBBActionPerformed(evt);
            }
        });

        btn_resetBB.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_resetBB.setText("RESET");
        btn_resetBB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetBBActionPerformed(evt);
            }
        });

        jLabel48.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\bb (2).jpg")); // NOI18N

        jLabel49.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel49.setText("<18,5");

        jLabel54.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel54.setText("18,5-24,9");

        jLabel55.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel55.setText("KURUS");

        jLabel58.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel58.setText("25-29,9");

        jLabel64.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel64.setText("30-34,9");

        jLabel65.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel65.setText("OBESE");

        jLabel66.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel66.setText("OVERWEIGHT");

        jLabel67.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 12)); // NOI18N
        jLabel67.setText("NORMAL");

        jLabel69.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel69.setText("Gender");

        buttonGroup1.add(rb_laki);
        rb_laki.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        rb_laki.setText("Laki - Laki");
        rb_laki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_lakiActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_pr);
        rb_pr.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        rb_pr.setText("Perempuan");
        rb_pr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_prActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53)
                    .addComponent(jLabel51)
                    .addComponent(jLabel52)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addComponent(rb_laki)
                        .addGap(18, 18, 18)
                        .addComponent(rb_pr))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(btn_cekBB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_resetBB))
                    .addComponent(tf_tb, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_hasil, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tf_bb, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                                .addComponent(jLabel54)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel58)
                                .addGap(27, 27, 27))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel55)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel67)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel66)
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel65)))
                        .addGap(63, 63, 63))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel48)
                        .addGap(52, 52, 52))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel49)
                                .addComponent(jLabel54)
                                .addComponent(jLabel58))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_bb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_tb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel69)
                            .addComponent(rb_laki)
                            .addComponent(rb_pr))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_hasil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(jLabel66)
                    .addComponent(jLabel67)
                    .addComponent(jLabel55)
                    .addComponent(btn_cekBB)
                    .addComponent(btn_resetBB))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tbl_bb.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tbl_bb);

        javax.swing.GroupLayout kesehatanpanel1Layout = new javax.swing.GroupLayout(kesehatanpanel1);
        kesehatanpanel1.setLayout(kesehatanpanel1Layout);
        kesehatanpanel1Layout.setHorizontalGroup(
            kesehatanpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kesehatanpanel1Layout.createSequentialGroup()
                .addGroup(kesehatanpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kesehatanpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        kesehatanpanel1Layout.setVerticalGroup(
            kesehatanpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kesehatanpanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("BB Ideal", kesehatanpanel1);

        kesehatanpanel2.setBackground(new java.awt.Color(91, 183, 39));
        kesehatanpanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel22.setBackground(new java.awt.Color(231, 248, 236));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel43.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel43.setText("CEK KESEHATAN KAMU !");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(jLabel43)
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel43)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(82, 137, 90));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        jLabel50.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel50.setText("Cek Kadar Gula Yukkkk");

        jLabel71.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel71)
                .addGap(156, 156, 156))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel50)
                .addGap(19, 19, 19))
        );

        jLabel62.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel62.setText("Kadar Gula");

        jLabel63.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel63.setText("Hasil");

        tf_kadargula1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_kadargula1ActionPerformed(evt);
            }
        });

        tf_hasilKG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_hasilKG1ActionPerformed(evt);
            }
        });

        btn_cekKG1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_cekKG1.setText("CEK");
        btn_cekKG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cekKG1ActionPerformed(evt);
            }
        });

        btn_resetKG1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_resetKG1.setText("RESET");
        btn_resetKG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetKG1ActionPerformed(evt);
            }
        });

        jLabel70.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_12-removebg-preview (2).png")); // NOI18N

        jLabel72.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel72.setText("Tipe Tes");

        cb_tipetes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih--", "Puasa", "Setelah Makan" }));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_kadargula1)
                            .addComponent(cb_tipetes, 0, 208, Short.MAX_VALUE)
                            .addComponent(tf_hasilKG1)))
                    .addComponent(jLabel72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btn_cekKG1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_resetKG1))
                    .addComponent(jLabel70))
                .addGap(42, 42, 42))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_kadargula1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel62))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(cb_tipetes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tf_hasilKG1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel63))
                        .addGap(31, 31, 31))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_resetKG1)
                            .addComponent(btn_cekKG1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        tbl_KG.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tbl_KG);

        javax.swing.GroupLayout kesehatanpanel2Layout = new javax.swing.GroupLayout(kesehatanpanel2);
        kesehatanpanel2.setLayout(kesehatanpanel2Layout);
        kesehatanpanel2Layout.setHorizontalGroup(
            kesehatanpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kesehatanpanel2Layout.createSequentialGroup()
                .addGroup(kesehatanpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kesehatanpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(412, Short.MAX_VALUE))
        );
        kesehatanpanel2Layout.setVerticalGroup(
            kesehatanpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kesehatanpanel2Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jTabbedPane4.addTab("Gula Darah", kesehatanpanel2);

        javax.swing.GroupLayout kesehatanpanelLayout = new javax.swing.GroupLayout(kesehatanpanel);
        kesehatanpanel.setLayout(kesehatanpanelLayout);
        kesehatanpanelLayout.setHorizontalGroup(
            kesehatanpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4)
        );
        kesehatanpanelLayout.setVerticalGroup(
            kesehatanpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kesehatanpanelLayout.createSequentialGroup()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 72, Short.MAX_VALUE))
        );

        mainpanel.add(kesehatanpanel, "card4");

        rekampanel.setBackground(new java.awt.Color(106, 88, 156));
        rekampanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 1, 36)); // NOI18N
        jLabel6.setText("REKAM MEDIS PRIBADI !");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(136, 136, 136))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel6)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel44.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Riwayat Penyakit");

        jLabel45.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Obat Yang Dikonsumsi");

        jLabel46.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Penyakit Yang Pernah Dialami");

        jLabel47.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Alergi");

        tf_penyakitdialami.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_penyakitdialamiActionPerformed(evt);
            }
        });

        tf_alergi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_alergiActionPerformed(evt);
            }
        });

        tbl_rekamMedis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_rekamMedis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_rekamMedisMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_rekamMedis);

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_simpan.setBackground(new java.awt.Color(106, 88, 156));
        btn_simpan.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btn_simpan.setForeground(new java.awt.Color(204, 204, 204));
        btn_simpan.setText("SIMPAN");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_edit.setBackground(new java.awt.Color(106, 88, 156));
        btn_edit.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btn_edit.setForeground(new java.awt.Color(204, 204, 204));
        btn_edit.setText("EDIT");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_hapus.setBackground(new java.awt.Color(106, 88, 156));
        btn_hapus.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btn_hapus.setForeground(new java.awt.Color(204, 204, 204));
        btn_hapus.setText("HAPUS");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });

        btn_cetak.setBackground(new java.awt.Color(106, 88, 156));
        btn_cetak.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btn_cetak.setForeground(new java.awt.Color(204, 204, 204));
        btn_cetak.setText("CETAK");
        btn_cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 23, Short.MAX_VALUE))
        );

        tf_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_idActionPerformed(evt);
            }
        });

        jLabel73.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("id_Rekam Medis");

        jLabel74.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_5__1_-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout rekampanelLayout = new javax.swing.GroupLayout(rekampanel);
        rekampanel.setLayout(rekampanelLayout);
        rekampanelLayout.setHorizontalGroup(
            rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(rekampanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47)
                    .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel45))
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_riwayatP, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                    .addComponent(tf_id)
                    .addComponent(tf_obat)
                    .addComponent(tf_penyakitdialami)
                    .addComponent(tf_alergi))
                .addGap(32, 32, 32)
                .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        rekampanelLayout.setVerticalGroup(
            rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rekampanelLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rekampanelLayout.createSequentialGroup()
                        .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel73))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_riwayatP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_obat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_penyakitdialami, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(rekampanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_alergi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47))
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rekampanelLayout.createSequentialGroup()
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainpanel.add(rekampanel, "card5");

        berandapanel.setBackground(new java.awt.Color(150, 77, 77));

        jPanel4.setBackground(new java.awt.Color(163, 90, 90));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel4.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Kesehatan merupakan kondisi sejahtera secara fisik, mental, dan sosial yang");
        jPanel4.add(jLabel11);
        jLabel11.setBounds(180, 150, 410, 16);

        jLabel12.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("memungkinkan seseorang untuk hidup produktif dan menghadapi tantangan");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(180, 170, 415, 16);

        jLabel13.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("sehari-hari dengan baik. Kesehatan tidak hanya berarti bebas dari penyakit,");
        jPanel4.add(jLabel13);
        jLabel13.setBounds(180, 190, 411, 16);

        jLabel14.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("tetapi juga mencakup kemampuan tubuh dan pikiran untuk berfungsi secara");
        jPanel4.add(jLabel14);
        jLabel14.setBounds(180, 210, 412, 16);

        jLabel15.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("optimal dan beradaptasi dengan lingkungan.");
        jPanel4.add(jLabel15);
        jLabel15.setBounds(180, 230, 243, 16);

        jLabel16.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Dengan tubuh yang sehat, pikiran yang tenang,");
        jPanel4.add(jLabel16);
        jLabel16.setBounds(180, 250, 256, 16);

        jLabel17.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("dan keseimbangan emosi, seseorang dapat mencapai");
        jPanel4.add(jLabel17);
        jLabel17.setBounds(180, 270, 286, 16);

        jLabel18.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("kualitas hidup yang lebih baik");
        jPanel4.add(jLabel18);
        jLabel18.setBounds(180, 290, 162, 16);

        jPanel6.setBackground(new java.awt.Color(150, 77, 77));
        jPanel6.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel32.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("INFORMASI KESEHATAN");

        jLabel77.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_11-removebg-preview (1).png")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel77)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addContainerGap(197, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel32))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel6);
        jPanel6.setBounds(0, 0, 650, 100);

        jLabel68.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_10-removebg-preview (2).png")); // NOI18N
        jPanel4.add(jLabel68);
        jLabel68.setBounds(510, 270, 140, 210);

        jLabel75.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\suster_2__1_-removebg-preview.png")); // NOI18N
        jPanel4.add(jLabel75);
        jLabel75.setBounds(0, 200, 300, 267);

        jLabel76.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_12-removebg-preview (2).png")); // NOI18N
        jPanel4.add(jLabel76);
        jLabel76.setBounds(270, 360, 210, 112);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 198, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Kesehatan", jPanel3);

        jPanel2.setBackground(new java.awt.Color(163, 90, 90));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel5.setBackground(new java.awt.Color(150, 77, 77));
        jPanel5.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel20.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("APLIKASI PEMANTAU");

        jLabel21.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("KESEHATAN");

        jLabel31.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\khs_13__1_-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20)
                        .addGap(36, 36, 36)))
                .addComponent(jLabel31)
                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel31)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Aplikasi pemantau kesehatan bertujuan untuk membantu pengguna dalam");

        jLabel22.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("memantau kesehatan mereka secara rutin dan efektif.");

        jLabel23.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Pengguna dapat menyimpan informasi kesehatan pribadi,");

        jLabel24.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("seperti tekanan darah,dan berat badan. Data ini bisa dianalisis");

        jLabel25.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("untuk melihat pola kesehatan.");

        jLabel26.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Aplikasi pemantau kesehatan bisa sangat bermanfaat untuk menjaga");

        jLabel28.setIcon(new javax.swing.ImageIcon("C:\\Users\\azlif\\Downloads\\ksh_9__1_-removebg-preview.png")); // NOI18N

        jLabel29.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("kesehatan harian dan sebagai alat bantu untuk pemantauan kondisi kronis");

        jLabel27.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("serta memberikan panduan bagi pengguna untuk menjalani");

        jLabel30.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("gaya hidup lebih sehat.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22)
                    .addComponent(jLabel8)
                    .addComponent(jLabel24)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel26))
                            .addComponent(jLabel27)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(204, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addGap(11, 11, 11)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(54, 54, 54))))
        );

        jTabbedPane1.addTab("Tentang", jPanel2);

        javax.swing.GroupLayout berandapanelLayout = new javax.swing.GroupLayout(berandapanel);
        berandapanel.setLayout(berandapanelLayout);
        berandapanelLayout.setHorizontalGroup(
            berandapanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        berandapanelLayout.setVerticalGroup(
            berandapanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(berandapanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        mainpanel.add(berandapanel, "card6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menupanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menupanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_profilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_profilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_profilActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_logoutActionPerformed

    private void btn_kesehatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_kesehatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_kesehatanActionPerformed

    private void btn_medisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_medisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_medisActionPerformed

    private void btn_profilMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_profilMouseEntered
        // TODO add your handling code here:
//        btn_profil.setBackground(new Color (230, 126, 34));
    }//GEN-LAST:event_btn_profilMouseEntered

    private void btn_kesehatanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_kesehatanMouseEntered
        // TODO add your handling code here:
//        btn_kesehatan.setBackground(new Color(230, 126, 34));
    }//GEN-LAST:event_btn_kesehatanMouseEntered

    private void btn_medisMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_medisMouseEntered
        // TODO add your handling code here:
        // btn_medis.setBackground(new Color(230, 126, 34));
    }//GEN-LAST:event_btn_medisMouseEntered

    private void btn_logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logoutMouseEntered
        // TODO add your handling code here:
        // btn_logout.setBackground(new Color(230, 126, 34));
    }//GEN-LAST:event_btn_logoutMouseEntered

    private void btn_profilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_profilMouseClicked
        // TODO add your handling code here:
        mainpanel.removeAll();
//        mengambar ulang
        mainpanel.repaint();
//        validasi ulang
        mainpanel.revalidate();
        
        mainpanel.add(profilpanel);
        mainpanel.repaint();
        mainpanel.revalidate();
    }//GEN-LAST:event_btn_profilMouseClicked

    private void btn_kesehatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_kesehatanMouseClicked
        // TODO add your handling code here:
        mainpanel.removeAll();
        mainpanel.repaint();
        mainpanel.revalidate();
        
        mainpanel.add(kesehatanpanel);
        mainpanel.repaint();
        mainpanel.revalidate();
    }//GEN-LAST:event_btn_kesehatanMouseClicked

    private void btn_medisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_medisMouseClicked
        // TODO add your handling code here:
        mainpanel.removeAll();
        mainpanel.repaint();
        mainpanel.revalidate();
        
        mainpanel.add(rekampanel);
        mainpanel.repaint();
        mainpanel.revalidate();
    }//GEN-LAST:event_btn_medisMouseClicked

    private void btn_logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logoutMouseClicked
        // TODO add your handling code here:
        int dialogBtn = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this,"Apakah anda yakin ingin keluar?","PERINGATAN",dialogBtn);
        if(dialogResult == 0){
            Login n = new  Login();
            n.setVisible(true);
            this.setVisible(false);
        }else{
            
        }
    }//GEN-LAST:event_btn_logoutMouseClicked

    private void tf_penyakitdialamiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_penyakitdialamiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_penyakitdialamiActionPerformed

    private void tf_alergiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_alergiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_alergiActionPerformed

    private void homepanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homepanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_homepanelMouseClicked

    private void btn_simpanDataDiriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanDataDiriActionPerformed
        // TODO add your handling code here:
        
        nama = tf_nama.getText();
        tgllahir = tf_tgllahir.getText();

        if (rb_cowok.isSelected()) {
            jeniskelamin = rb_cowok.getText();
        } else if (rb_cewek.isSelected()) {
            jeniskelamin = rb_cewek.getText();
        }

        alamat = tf_alamat.getText();
        email = tf_email.getText();
        notlp = tf_tlp.getText();

        if (!cb_setuju.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Harap Centang dulu!", 
                "Warning", 
                JOptionPane.ERROR_MESSAGE);
        }
        else{
            tf_namaT.setText(nama);
            tf_tglT.setText(tgllahir);
            tf_jeniskelaminT.setText(jeniskelamin);
            tf_alamatT.setText(alamat);
            tf_notlpT.setText(notlp);
            tf_emailT.setText(email);

            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
        }
        kosong1();
    }//GEN-LAST:event_btn_simpanDataDiriActionPerformed

    private void rb_cewekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_cewekActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_cewekActionPerformed

    private void cb_setujuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_setujuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_setujuActionPerformed

    private void btn_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_keluarActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btn_keluarActionPerformed

    private void btn_resetKG1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetKG1ActionPerformed
        // TODO add your handling code here:
        tf_kadargula1.setText("");
        tf_hasilKG1.setText("");
        cb_tipetes.setSelectedIndex(0);
    }//GEN-LAST:event_btn_resetKG1ActionPerformed

    private void btn_cekKG1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cekKG1ActionPerformed
        // TODO add your handling code here:
         try {
            // Ambil input kadar gula darah
            double gulaDarah = Double.parseDouble(tf_kadargula1.getText());
            String tipeTes = (String) cb_tipetes.getSelectedItem();
            String kategori;
            String rekomendasi = "";

            // Validasi input
            if (gulaDarah <= 0) {
                JOptionPane.showMessageDialog(this, "Angka tidak boleh lebih kecil dari 1", "error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }else{
                // Tentukan kategori berdasarkan tipe tes
                if (tipeTes.equals("Puasa")) {
                    if (gulaDarah < 70) {
                        kategori = "Rendah";
                        rekomendasi = "Disarankan untuk segera makan sesuatu atau konsultasi dengan dokter.";
                    } else if (gulaDarah <= 99) {
                        kategori = "Normal";
                        rekomendasi = "Kadar gula darah ideal. Pertahankan pola makan sehat.";
                    } else if (gulaDarah <= 125) {
                        kategori = "Prediabetes";
                        rekomendasi = "Mulai perhatikan pola makan dan aktivitas fisik.";
                    } else {
                        kategori = "Diabetes";
                        rekomendasi = "Segera konsultasi dengan dokter untuk perawatan lebih lanjut.";
                    }
                } else { // Setelah Makan
                    if (gulaDarah < 140) {
                        kategori = "Normal";
                        rekomendasi = "Kadar gula darah ideal setelah makan.";
                    } else if (gulaDarah <= 199) {
                        kategori = "Prediabetes";
                        rekomendasi = "Perbaiki pola makan dan gaya hidup.";
                    } else {
                        kategori = "Diabetes";
                        rekomendasi = "Segera konsultasi dengan dokter untuk penanganan lebih lanjut.";
                    }
                }

                // Tampilkan hasil
                tf_hasilKG1.setText(String.format("Hasil: kategori = %s", kategori));
                tf_hasilKG1.setForeground(Color.BLACK); // Teks hitam untuk kategori normal dan lainnya

                // Tampilkan rekomendasi dalam dialog
                JOptionPane.showMessageDialog(this, rekomendasi, "Rekomendasi", JOptionPane.INFORMATION_MESSAGE);
            }
            try {
                String sql = "INSERT INTO kadargula (kadar, tipe, kategori, tanggal) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, (int) gulaDarah);
                ps.setString(2, tipeTes);
                ps.setString(3, kategori);
                ps.setDate(4, Date.valueOf(LocalDate.now()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cek Kadar Gula Berhasil Disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            loadDataKadargula();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka  yang valid!!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            tf_hasilKG1.setForeground(Color.BLACK); // Teks hitam untuk kesalahan input
        }
    }//GEN-LAST:event_btn_cekKG1ActionPerformed

    private void tf_hasilKG1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_hasilKG1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_hasilKG1ActionPerformed

    private void tf_kadargula1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_kadargula1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_kadargula1ActionPerformed

    private void tf_tbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_tbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_tbActionPerformed

    private void btn_berandaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_berandaMouseClicked
        // TODO add your handling code here:
        mainpanel.removeAll();
        mainpanel.repaint();
        mainpanel.revalidate();
        
        mainpanel.add(berandapanel);
        mainpanel.repaint();
        mainpanel.revalidate();
        
    }//GEN-LAST:event_btn_berandaMouseClicked

    private void btn_berandaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_berandaMouseEntered
        // TODO add your handling code here:
        // btn_beranda.setBackground(new Color(230, 126, 34));
    }//GEN-LAST:event_btn_berandaMouseEntered

    private void btn_berandaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_berandaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_berandaActionPerformed

    private void btn_cekBBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cekBBActionPerformed
        // TODO add your handling code here:
        try {
            double berat = Double.parseDouble(tf_bb.getText());
            double tinggiCm = Double.parseDouble(tf_tb.getText());

            if (berat <= 0 || tinggiCm <= 0) {
                JOptionPane.showMessageDialog(this, "Angka tidak boleh lebih kecil dari 1", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String gender = rb_laki.isSelected() ? "Laki-laki" : rb_pr.isSelected() ? "Perempuan" : null;
            if (gender == null) {
                JOptionPane.showMessageDialog(this, "Pilih jenis kelamin terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double tinggiM = tinggiCm / 100.0;
            double bmi;

            if (gender.equals("Laki-laki")) {
                bmi = berat / (tinggiM * tinggiM) * 1.1; // Contoh aturan tambahan
            } else {
                bmi = berat / (tinggiM * tinggiM) * 0.9; // Contoh aturan tambahan
            }

            String kategori, rekomendasi;
            if (bmi < 18.5) {
                kategori = "Kurus";
                rekomendasi = "Konsumsi makanan bergizi tinggi.";
            } else if (bmi < 24.9) {
                kategori = "Normal";
                rekomendasi = "Pertahankan gaya hidup sehat.";
            } else if (bmi < 29.9) {
                kategori = "Gemuk";
                rekomendasi = "Kurangi makanan tinggi lemak.";
            } else {
                kategori = "Obesitas";
                rekomendasi = "Konsultasikan ke dokter.";
            }

            tf_hasil.setText(String.format("BMI: %.2f (%s)", bmi, kategori));

            String pesan = String.format("Hasil: BMI = %.2f (%s).\nDisarankan: %s", bmi, kategori, rekomendasi);
            JOptionPane.showMessageDialog(this, pesan, "Hasil BMI dan Rekomendasi", JOptionPane.INFORMATION_MESSAGE);

            // Simpan ke database
            simpanKeDatabase(berat, tinggiCm, gender, kategori, rekomendasi);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void simpanKeDatabase(double berat, double tinggiCm, String gender, String kategori, String rekomendasi) {
        try {
            String sql = "INSERT INTO rekomendasi (BB, TB, gender, kriteria, rekomendasi, tanggal) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, berat);
            ps.setDouble(2, tinggiCm);
            ps.setString(3, gender);
            ps.setString(4, kategori);
            ps.setString(5, rekomendasi);
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Rekomendasi Berhasil Disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
        loadDataRekomendasi();
    
    }//GEN-LAST:event_btn_cekBBActionPerformed

    private void btn_resetBBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetBBActionPerformed
        // TODO add your handling code here:
        tf_bb.setText("");
        tf_tb.setText("");
        buttonGroup1.clearSelection();
        tf_hasil.setText("");
    }//GEN-LAST:event_btn_resetBBActionPerformed

    private void rb_lakiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_lakiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_lakiActionPerformed

    private void rb_prActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_prActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_prActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        // TODO add your handling code here:
        try {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin nih mau dihapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM rekam_medis WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(tf_id.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Data berhasil dihapus.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                loadDataMedis();
                kosong();
            }
        } catch (SQLException e) {
            System.out.println("Error Delete Data: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void tf_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_idActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
        try {
            if (tf_id.getText().trim().isEmpty() || tf_riwayatP.getText().trim().isEmpty() || tf_obat.getText().trim().isEmpty() || tf_penyakitdialami.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data tidak lengkap", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO rekam_medis (id, riwayat_penyakit, obat_yang_dikonsumsi, penyakit_pernah_dialami, alergi) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tf_id.getText().trim());
            ps.setString(2, tf_riwayatP.getText().trim());
            ps.setString(3, tf_obat.getText().trim());
            ps.setString(4, tf_penyakitdialami.getText().trim());
            ps.setString(5, tf_alergi.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data saved successfully");
            loadDataMedis();
            kosong();
        } catch (SQLException e) {
            System.out.println("Error Save Data: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "UPDATE rekam_medis SET riwayat_penyakit = ?, obat_yang_dikonsumsi = ?, penyakit_pernah_dialami = ?, alergi = ?WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);//pelingdung untuk si query

            ps.setString(1, tf_riwayatP.getText());
            ps.setString(2, tf_obat.getText());
            ps.setString(3, tf_penyakitdialami.getText());
            ps.setString(4, tf_alergi.getText());
            ps.setInt(5, Integer.parseInt(tf_id.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data updated successfully");
            loadDataMedis();
            kosong();
            
        }catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
            }
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_cetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cetakActionPerformed
        // TODO add your handling code here:
        cetakKePDF();
    }//GEN-LAST:event_btn_cetakActionPerformed

    private void tbl_rekamMedisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_rekamMedisMouseClicked
        // TODO add your handling code here:
        int row = tbl_rekamMedis.getSelectedRow(); 
        
        if (row != -1) {
            tf_id.setText(tbl_rekamMedis.getValueAt(row, 0).toString());
            tf_riwayatP.setText(tbl_rekamMedis.getValueAt(row, 1).toString()); 
            tf_penyakitdialami.setText(tbl_rekamMedis.getValueAt(row, 2).toString()); 
            tf_obat.setText(tbl_rekamMedis.getValueAt(row, 3).toString()); 
            tf_alergi.setText(tbl_rekamMedis.getValueAt(row, 4).toString()); 
//            String kelamin = tbl_rekamMedis.getValueAt(row, 3).toString();
        }
    }//GEN-LAST:event_tbl_rekamMedisMouseClicked

    private void tf_emailTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_emailTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_emailTActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel berandapanel;
    private javax.swing.JButton btn_beranda;
    private javax.swing.JButton btn_cekBB;
    private javax.swing.JButton btn_cekKG1;
    private javax.swing.JButton btn_cetak;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_keluar;
    private javax.swing.JButton btn_kesehatan;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_medis;
    private javax.swing.JButton btn_profil;
    private javax.swing.JButton btn_resetBB;
    private javax.swing.JButton btn_resetKG1;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_simpanDataDiri;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox cb_setuju;
    private javax.swing.JComboBox<String> cb_tipetes;
    private javax.swing.JPanel homepanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JPanel kesehatanpanel;
    private javax.swing.JPanel kesehatanpanel1;
    private javax.swing.JPanel kesehatanpanel2;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JPanel menupanel;
    private javax.swing.JPanel profilpanel;
    private javax.swing.JRadioButton rb_cewek;
    private javax.swing.JRadioButton rb_cowok;
    private javax.swing.JRadioButton rb_laki;
    private javax.swing.JRadioButton rb_pr;
    private javax.swing.JPanel rekampanel;
    private javax.swing.JTable tbl_KG;
    private javax.swing.JTable tbl_bb;
    private javax.swing.JTable tbl_rekamMedis;
    private javax.swing.JTextField tf_alamat;
    private javax.swing.JTextField tf_alamatT;
    private javax.swing.JTextField tf_alergi;
    private javax.swing.JTextField tf_bb;
    private javax.swing.JTextField tf_email;
    private javax.swing.JTextField tf_emailT;
    private javax.swing.JTextField tf_hasil;
    private javax.swing.JTextField tf_hasilKG1;
    private javax.swing.JTextField tf_id;
    private javax.swing.JTextField tf_jeniskelaminT;
    private javax.swing.JTextField tf_kadargula1;
    private javax.swing.JTextField tf_nama;
    private javax.swing.JTextField tf_namaT;
    private javax.swing.JTextField tf_notlpT;
    private javax.swing.JTextField tf_obat;
    private javax.swing.JTextField tf_penyakitdialami;
    private javax.swing.JTextField tf_riwayatP;
    private javax.swing.JTextField tf_tb;
    private javax.swing.JTextField tf_tglT;
    private javax.swing.JTextField tf_tgllahir;
    private javax.swing.JTextField tf_tlp;
    // End of variables declaration//GEN-END:variables
}
