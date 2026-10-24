import java.util.Scanner;

abstract class Media {
    protected String judul;
    protected String kreator;
    protected int durasi;

    public Media(String judul, String kreator, int durasi) {
        this.judul = judul;
        this.kreator = kreator;
        this.durasi = durasi;
    }

    public abstract void tampilkanInfo();
}

class Lagu extends Media {
    private String genre;

    public Lagu(String judul, String kreator, int durasi, String genre) {
        super(judul, kreator, durasi);
        this.genre = genre;
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("[Lagu] " + judul + " - " + kreator + " | Genre: " + genre + " | " + durasi + " dtk");
    }
}

class Podcast extends Media {
    private String topik;

    public Podcast(String judul, String kreator, int durasi, String topik) {
        super(judul, kreator, durasi);
        this.topik = topik;
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("[Podcast] " + judul + " - " + kreator + " | Topik: " + topik + " | " + durasi + " dtk");
    }
}


class Node {
    Media data;
    Node prev;
    Node next;

    public Node(Media data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}

class PlaylistDLL {
    private Node head;
    private Node tail;
    private Node current;

    public PlaylistDLL() {
        this.head = null;
        this.tail = null;
        this.current = null;
    }

    public void tambahDiDepan(Media media) {
        Node newNode = new Node(media);
        if (head == null) {
            head = tail = current = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        System.out.println("Berhasil ditambahkan: " + media.judul);
    }

    public void tambahDiBelakang(Media media) {
        Node newNode = new Node(media);
        if (head == null) {
            head = tail = current = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        System.out.println("Berhasil ditambahkan: " + media.judul);
    }

    public void tambahDiTengah(Media media, int posisi) {
        if (posisi <= 1 || head == null) {
            tambahDiDepan(media);
            return;
        }

        Node newNode = new Node(media);
        Node temp = head;
        int hitung = 1;

        while (temp != null && hitung < posisi - 1) {
            temp = temp.next;
            hitung++;
        }

        if (temp == null || temp == tail) {
            tambahDiBelakang(media);
        } else {
            newNode.next = temp.next;
            newNode.prev = temp;
            temp.next.prev = newNode;
            temp.next = newNode;
            System.out.println("Berhasil disisipkan di urutan ke-" + posisi + ": " + media.judul);
        }
    }

    public void tampilkanPlaylist() {
        if (head == null) {
            System.out.println("\n[ Playlist Kosong ]");
            return;
        }
        System.out.println("\n=== DAFTAR PLAYLIST ===");
        Node temp = head;
        int nomor = 1;
        while (temp != null) {
            System.out.print(nomor + ". ");
            temp.data.tampilkanInfo();
            temp = temp.next;
            nomor++;
        }
        System.out.println("=======================\n");
    }

    public void putarSekarang() {
        if (current != null) {
            System.out.print(">>> SEDANG MEMUTAR: ");
            current.data.tampilkanInfo();
        } else {
            System.out.println("Tidak ada media yang diputar.");
        }
    }

    public void next() {
        if (current != null && current.next != null) {
            current = current.next;
            System.out.println("-> Memutar selanjutnya...");
            putarSekarang();
        } else {
            System.out.println("! Ini adalah media terakhir.");
        }
    }

    public void previous() {
        if (current != null && current.prev != null) {
            current = current.prev;
            System.out.println("<- Memutar sebelumnya...");
            putarSekarang();
        } else {
            System.out.println("! Ini adalah media pertama.");
        }
    }

    // Method untuk menghapus media berdasarkan nomor urut di playlist
    public void hapusMedia(int posisi) {
        if (head == null) {
            System.out.println("\n[ Playlist Kosong, tidak ada yang bisa dihapus! ]");
            return;
        }

        if (posisi == 1) {
            System.out.println("Berhasil dihapus: " + head.data.judul);
            if (head == tail) { 
                head = tail = current = null;
            } else {
                head = head.next;
                head.prev = null;
                if (current == head.prev) {
                    current = head;
                }
            }
            return;
        }

        Node temp = head;
        int hitung = 1;

        while (temp != null && hitung < posisi) {
            temp = temp.next;
            hitung++;
        }

        // Jika posisi melebihi jumlah data
        if (temp == null) {
            System.out.println("! Posisi tidak ditemukan di dalam playlist.");
            return;
        }

        if (temp == tail) {
            System.out.println("Berhasil dihapus: " + tail.data.judul);
            tail = tail.prev;
            tail.next = null;
            if (current == temp) {
                current = tail;
            }
            return;
        }

        System.out.println("Berhasil dihapus: " + temp.data.judul);
        if (current == temp) {
            current = temp.next; // Geser current ke setelahnya jika sedang diputar
        }
        temp.prev.next = temp.next;
        temp.next.prev = temp.prev;
    }
}


public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        PlaylistDLL playlistSaya = new PlaylistDLL();
        int pilihanMenu;

        System.out.println("Memuat data awal (Dummy Data)...");
        playlistSaya.tambahDiBelakang(new Lagu("Bohemian Rhapsody", "Queen", 354, "Rock"));
        playlistSaya.tambahDiBelakang(new Lagu("Hati-Hati di Jalan", "Tulus", 242, "Pop"));
        playlistSaya.tambahDiBelakang(new Podcast("Belajar Java OOP", "CodePolitan", 1200, "Edukasi"));
        playlistSaya.tambahDiBelakang(new Lagu("Fix You", "Coldplay", 295, "Alternative"));
        System.out.println("Data awal berhasil dimuat!\n");

        do {
            System.out.println("\n=== MENU PLAYLIST INTERAKTIF ===");
            System.out.println("1. Tambah Media di Depan");
            System.out.println("2. Tambah Media di Belakang");
            System.out.println("3. Sisipkan Media di Tengah");
            System.out.println("4. Tampilkan Playlist");
            System.out.println("5. Putar Sekarang");
            System.out.println("6. Next");
            System.out.println("7. Previous");
            System.out.println("8. Hapus Media");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            pilihanMenu = input.nextInt();
            input.nextLine(); // Membersihkan buffer enter

            if (pilihanMenu >= 1 && pilihanMenu <= 3) {
                System.out.print("Tipe Media (1=Lagu, 2=Podcast): ");
                int tipe = input.nextInt();
                input.nextLine();

                System.out.print("Masukkan Judul   : ");
                String judul = input.nextLine();
                System.out.print("Masukkan Kreator : ");
                String kreator = input.nextLine();
                System.out.print("Durasi (detik)   : ");
                int durasi = input.nextInt();
                input.nextLine();

                Media mediaBaru;
                if (tipe == 1) {
                    System.out.print("Masukkan Genre   : ");
                    String genre = input.nextLine();
                    mediaBaru = new Lagu(judul, kreator, durasi, genre);
                } else {
                    System.out.print("Masukkan Topik   : ");
                    String topik = input.nextLine();
                    mediaBaru = new Podcast(judul, kreator, durasi, topik);
                }

                if (pilihanMenu == 1) {
                    playlistSaya.tambahDiDepan(mediaBaru);
                } else if (pilihanMenu == 2) {
                    playlistSaya.tambahDiBelakang(mediaBaru);
                } else if (pilihanMenu == 3) {
                    System.out.print("Sisipkan di urutan ke-berapa? (misal: 2) : ");
                    int posisi = input.nextInt();
                    playlistSaya.tambahDiTengah(mediaBaru, posisi);
                }
            } 
            else if (pilihanMenu == 4) playlistSaya.tampilkanPlaylist();
            else if (pilihanMenu == 5) playlistSaya.putarSekarang();
            else if (pilihanMenu == 6) playlistSaya.next();
            else if (pilihanMenu == 7) playlistSaya.previous();
            else if (pilihanMenu == 8) {
                playlistSaya.tampilkanPlaylist();
                System.out.print("Masukkan nomor urut media yang ingin dihapus: ");
                int posisiHapus = input.nextInt();
                input.nextLine();
                playlistSaya.hapusMedia(posisiHapus);
            }
        } while (pilihanMenu != 0);

        System.out.println("Program selesai. Sampai jumpa!");
        input.close();
    }
}