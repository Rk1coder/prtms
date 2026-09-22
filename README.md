# PRTMS

Spring Boot ve React ile geliştirilmiş basit bir telemetri takip projesi.
Platform ekleyebilir, pil, sıcaklık ve bağlantı kalitesi değerleri göndererek
platformun sağlık durumunu ve son 10 ölçümünü görebilirsin.
Veriler ve eşikler demo amaçlıdır.

## Gereksinimler

- Java 17
- Node.js 22.12+ ve npm

## Çalıştırma

Proje kökünden bir terminalde backend'i başlat:

```bash
cd backend
./mvnw spring-boot:run
```

Windows'ta `./mvnw` yerine `.\mvnw.cmd` kullan.

Proje kökünden ikinci terminalde frontend'i başlat:

```bash
cd frontend
npm ci
npm run dev
```

İlk kurulum internet bağlantısı gerektirir. İki terminali de açık tut.

- Uygulama: [localhost:5173](http://localhost:5173)
- Swagger: [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Demo

`UAV-001` koduyla bir platform ekle ve **View** butonuna bas.
Aşağıdaki değerleri sırayla gönder:

| Pil (%) | Sıcaklık (°C) | Bağlantı (%) | Sonuç |
|---|---|---|---|
| 80 | 55 | 90 | READY |
| 30 | 60 | 80 | DEGRADED |
| 15 | 90 | 20 | NOT_READY |

İlk ölçümden önce durum `UNKNOWN` olur. H2 bellekte çalıştığı için backend
kapatılınca kayıtlar silinir.

## Kontrol

Backend klasöründe `./mvnw test`, frontend klasöründe `npm run build` çalıştır.
Ayrıntılı notlar [docs](docs/) klasöründedir.
