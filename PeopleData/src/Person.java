/**
 * Model data kru Stasiun Antariksa.
 * 4 atribut awal : id, nama, tahunLahir, kategori
 * 1 atribut baru : levelEnergi (0-100, diinput lewat JSlider)
 */
public class Person {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    private int levelEnergi;

    public Person(String id, String nama, int tahunLahir, String kategori, int levelEnergi) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.levelEnergi = levelEnergi;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public int getTahunLahir() { return tahunLahir; }
    public void setTahunLahir(int tahunLahir) { this.tahunLahir = tahunLahir; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public int getLevelEnergi() { return levelEnergi; }
    public void setLevelEnergi(int levelEnergi) { this.levelEnergi = levelEnergi; }
}
