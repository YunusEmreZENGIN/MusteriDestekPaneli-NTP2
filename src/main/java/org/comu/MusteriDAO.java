package org.comu;

import java.util.List;

public interface MusteriDAO {
    void ekle(Musteri m);
    List<Musteri> listele();
    void guncelle(Musteri m);
    void sil(String id);
}
