import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class PeopleMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        PeopleMenu menu = new PeopleMenu();

        // atur ukuran window
        menu.setSize(720, 620);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // judul window
        menu.setTitle("Stasiun Antariksa Nusantara-1");

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // objek untuk koneksi ke database
    private Database database;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunField;
    private JTable personTable;
    private JScrollPane scrollPane;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel tahunLabel;
    private JLabel kategoriLabel;
    private JLabel energiLabel;
    private JSlider energiSlider;
    private JLabel energiValueLabel;

    // constructor
    public PeopleMenu() {
        // buat objek database
        try {
            database = new Database();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null,
                    "Tidak dapat terhubung ke database.\n\nDetail: " + ex.getMessage()
                    + "\n\nPastikan MySQL menyala, database db_antariksa sudah di-import,\n"
                    + "dan driver mysql-connector-j sudah ditambahkan ke project.",
                    "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // isi tabel kru
        personTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] kategoriData = { "???", "Komandan Misi", "Pilot Roket", "Insinyur Reaktor", "Astrobiolog", "Dokter Antariksa", "Teknisi Hidroponik"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // atur slider level energi (0 - 100%)
        energiSlider.setMinimum(0);
        energiSlider.setMaximum(100);
        energiSlider.setValue(50);
        energiSlider.setMajorTickSpacing(25);
        energiSlider.setPaintTicks(true);
        energiSlider.setPaintLabels(true);

        energiValueLabel.setText("50%");

        energiSlider.addChangeListener(e -> {
            energiValueLabel.setText(energiSlider.getValue() + "%");
        });

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1){
                    insertData();
                }else{
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // konfirmasi sebelum menghapus data
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data kru \"" + namaField.getText() + "\"?\n"
                        + "Data yang dihapus tidak dapat dikembalikan.",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // jika yes
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        personTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                int row = personTable.getSelectedRow();
                if (row < 0) return;
                selectedIndex = row;

                // simpan value textfield dan combo box
                String curId = personTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curNama = personTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curTahun = personTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = personTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curEnergi = personTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield, combo box, dan slider
                idField.setText(curId);
                namaField.setText(curNama);
                tahunField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                energiSlider.setValue(Integer.parseInt(curEnergi));

                // ID tidak boleh diubah saat edit
                idField.setEnabled(false);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = {"ID", "Nama", "Tahun Lahir", "Kategori", "Level Energi (%)"};

        // buat objek tabel dengan kolom yang sudah dibuat (sel tidak bisa diedit langsung)
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try{
            ResultSet resultSet = database.selectQuery("SELECT * FROM tb_kru ORDER BY id");

            // isi tabel dengan hasil query
            while(resultSet.next()){
                Object[] row = new Object[5];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("nama");
                row[2] = resultSet.getString("tahun_lahir");
                row[3] = resultSet.getString("kategori");
                row[4] = resultSet.getString("level_energi");
                tmp.addRow(row);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return tmp;
    }

    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunStr = tahunField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int energi = energiSlider.getValue();

            // validasi input kosong
            if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua kolom wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // konversi angka
            int tahun = Integer.parseInt(tahunStr);
            int now = Year.now().getValue();
            if (tahun < 1900 || tahun > now) {
                JOptionPane.showMessageDialog(null, "Tahun lahir harus antara 1900 dan " + now + "!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // cek ID yang sudah ada
            ResultSet rs = database.selectQuery("SELECT id FROM tb_kru WHERE id='" + esc(id) + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "ID kru sudah ada! Gunakan ID lain.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // tambahkan data ke dalam database
            String sqlQuery = "INSERT INTO tb_kru (id, nama, tahun_lahir, kategori, level_energi) VALUES ('"
                    + esc(id) + "', '" + esc(nama) + "', " + tahun + ", '" + esc(kategori) + "', " + energi + ")";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan database!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String tahunStr = tahunField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem().toString();
            int energi = energiSlider.getValue();

            // validasi input kosong
            if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty() || kategori.equals("???")) {
                JOptionPane.showMessageDialog(null, "Semua kolom wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // konversi angka
            int tahun = Integer.parseInt(tahunStr);
            int now = Year.now().getValue();
            if (tahun < 1900 || tahun > now) {
                JOptionPane.showMessageDialog(null, "Tahun lahir harus antara 1900 dan " + now + "!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ubah data kru pada database
            String sqlQuery = "UPDATE tb_kru SET " +
                    "nama='" + esc(nama) + "', " +
                    "tahun_lahir=" + tahun + ", " +
                    "kategori='" + esc(kategori) + "', " +
                    "level_energi=" + energi +
                    " WHERE id='" + esc(id) + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan database!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        try {
            // hapus data dari database
            String id = idField.getText();
            String sqlQuery = "DELETE FROM tb_kru WHERE id='" + esc(id) + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            // update tabel
            personTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Delete berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan database!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearForm() {
        // kosongkan semua texfield, combo box, dan slider
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        energiSlider.setValue(50);

        // ID boleh diisi lagi
        idField.setEnabled(true);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // hilangkan highlight baris tabel
        personTable.clearSelection();

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    // escape tanda kutip dan backslash agar nilai aman dimasukkan ke string SQL
    private String esc(String s) {
        return s.replace("\\", "\\\\").replace("'", "''");
    }

}
