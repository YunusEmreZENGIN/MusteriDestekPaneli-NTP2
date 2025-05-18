package org.comu;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MusteriDAOMongoDB implements MusteriDAO {
    private MongoCollection<Document> musteriCollection;

    public MusteriDAOMongoDB(MongoDatabase database) {
        this.musteriCollection = database.getCollection("musteriler");
    }

    @Override
    public void ekle(Musteri m) {
        Document doc = new Document("ad", m.getAd())
                .append("soyad", m.getSoyad())
                .append("email", m.getEmail())
                .append("telefon", m.getTelefon());
        musteriCollection.insertOne(doc);
    }

    @Override
    public List<Musteri> listele() {
        List<Musteri> liste = new ArrayList<>();
        for (Document doc : musteriCollection.find()) {
            Musteri m = new Musteri();
            m.setId(doc.getObjectId("_id").toHexString());
            m.setAd(doc.getString("ad"));
            m.setSoyad(doc.getString("soyad"));
            m.setEmail(doc.getString("email"));
            m.setTelefon(doc.getString("telefon"));
            liste.add(m);
        }
        return liste;
    }

    @Override
    public void guncelle(Musteri m) {
        ObjectId objectId = new ObjectId(m.getId());
        musteriCollection.updateOne(Filters.eq("_id", objectId), Updates.combine(
                Updates.set("ad", m.getAd()),
                Updates.set("soyad", m.getSoyad()),
                Updates.set("email", m.getEmail()),
                Updates.set("telefon", m.getTelefon())
        ));
    }

    @Override
    public void sil(String id) {
        ObjectId objectId = new ObjectId(id);
        musteriCollection.deleteOne(Filters.eq("_id", objectId));
    }
}
