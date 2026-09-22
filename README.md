# PRTMS

Platform Readiness & Telemetry Monitoring System

## Quick Start

Repoyu indirdikten sonra terminali `README.md`, `backend/` ve `frontend/` içeren proje kökünde aç.
Önce `java -version`, `node --version` ve `npm --version` komutlarını kontrol et.
JDK 17 ve Node.js 22.12+ (22.x) veya desteklenen daha yeni sürüm gerekir.
İlk kurulumda Maven ve npm bağımlılıkları internetten indirilir.

**Terminal 1 — backend:**

```bash
cd backend
./mvnw spring-boot:run
```

Windows PowerShell'de `./mvnw` yerine `.\mvnw.cmd` kullan.

**Terminal 2 — frontend (proje kökünden):**

```bash
cd frontend
npm ci
npm run dev
```

Tarayıcıda http://localhost:5173 adresini aç. İlk açılışta platform listesi boştur;
aşağıdaki Demo Scenario ile kayıt ekleyebilirsin. Backend ve frontend terminallerini açık tut.
Sonraki açılışlarda bağımlılıklar değişmediyse `npm ci` tekrar gerekmez.

**Sık karşılaşılan durumlar:**

- Port 8080 veya 5173 doluysa aynı uygulamanın önceki çalışmasını durdur. Backend'i aynı anda
  hem IntelliJ'den hem terminalden başlatma.
- Backend connection error: backend terminalinde `Started PrtmsApplication` satırını kontrol et;
  ardından ekranda Retry seç.
- IntelliJ kullanıyorsan `backend/pom.xml` dosyasını Maven projesi olarak aç ve
  `PrtmsApplication` sınıfını çalıştır.
- `./mvnw: Permission denied` durumunda Linux/macOS için `chmod +x backend/mvnw` komutunu
  proje kökünde çalıştır.
- Backend durdurulunca H2 kayıtları silinir; yeniden boş listeyle başlaması beklenir.

## Overview

Spring Boot ve React temellerini göstermek için geliştirilmiş küçük bir full-stack POC.
Kullanıcı platform oluşturur, sentetik telemetri gönderir, güncel sağlık durumunu ve son 10 kaydı görür.
Bütün ölçümler ve eşikler **demo/synthetic** amaçlıdır. Gerçek operasyonel savunma sistemi değildir;
silah kontrolü, hedef seçimi, angajman veya atış kontrolü içermez.

## Architecture

```text
React Frontend
     |
     | REST API (JSON / fetch)
     v
Spring Boot Controller
     |
     v
Service
     |
     +---- Health Rules
     |
     v
Repository
     |
     v
H2 Database
```

Controller HTTP isteklerini alır ve `@Valid` doğrulamasını başlatır. Service iş akışını yürütür.
Repository, Spring Data JPA ile veriyi saklar. API yanıtlarında entity yerine record DTO'ları kullanılır.
Java record, alanları ve erişim metotlarını kısa biçimde tanımlayan değişmez bir veri taşıyıcısıdır.

`TelemetryService.addTelemetry()` üzerindeki `@Transactional`, telemetri kaydı ile platform durum
 güncellemesini tek işlem yapar: birinde hata olursa ikisi de geri alınır.
`HealthAssessmentService`, constructor üzerinden `List<HealthRule>` alır. Spring üç `@Component`
kuralını otomatik bulur. Service, somut kural sınıfını bilmeden `evaluate()` çağırır (polymorphism).

## Backend

- Java 17, Spring Boot 3.5.16, Maven (wrapper dahil)
- Spring Web, Spring Data JPA, Bean Validation, H2 in-memory database
- JUnit 5; mevcut platform service testlerinde Mockito
- SLF4J logging; springdoc-openapi 2.8.17 / Swagger UI

`com.prtms` altında controller, service, repository, entity, dto, rule, exception ve config paketleri bulunur.
HealthRule dışında gereksiz service interface veya mapper katmanı yoktur.

## Frontend

React 19 + Vite 7, JavaScript, fetch ve plain CSS. Beş küçük component kullanılır:
SummaryCards, PlatformForm, PlatformTable, TelemetryPanel, StatusBadge.

`App.jsx` platformları, seçimi, readiness ve geçmişi `useState` ile tutar.
`useEffect` ilk platform listesini alır. `services/api.js`, HTTP ve hata yanıtlarını yönetir.
Özet sayıları platform listesinden türetilir; ayrıca backend endpointi yoktur.
Bağımsız readiness/history istekleri `Promise.all` ile alınır. İstek sırasında seçim butonları
kilitlenir; başka platformdan kalan veriler seçim başlangıcında temizlenir.

## Health Rules

**Aşağıdaki eşikler tamamen sentetiktir; gerçek platform kuralları değildir.**

| Metric | READY | DEGRADED | NOT_READY |
|---|---|---|---|
| Battery (%) | >= 35 | 20–34 | < 20 |
| Temperature (°C) | <= 70 | > 70 ve <= 85 | > 85 |
| Link quality (%) | >= 50 | 25–49 | < 25 |

Bir kural NOT_READY ise sonuç NOT_READY; yoksa en az bir DEGRADED varsa DEGRADED; diğer durumda READY.
Yeni platform UNKNOWN durumundadır; ilk telemetriye kadar `latestTelemetryTime` null olur.
UNKNOWN platformlar toplam sayıya dahildir; READY/DEGRADED/NOT READY kartlarından birine sayılmaz.
Geçmişteki her satır kendi ölçümleriyle değerlendirilir, platformun bugünkü durumuyla etiketlenmez.
Kurallar ileride değiştirilirse geçmiş sonuçları da yeni kurallarla hesaplanır; kural sürümleme bu POC'de yoktur.

## API Endpoints

| Method | Endpoint | Response |
|---|---|---|
| POST | /api/platforms | 201 PlatformResponse |
| GET | /api/platforms | 200 platform list |
| GET | /api/platforms/{code} | 200 PlatformResponse |
| POST | /api/telemetry | 201 TelemetryResponse |
| GET | /api/platforms/{code}/telemetry | 200 latest 10, timestamp DESC, id DESC |
| GET | /api/platforms/{code}/readiness | 200 ReadinessResponse |

Bulunamayan platform 404, aynı kodla tekrar kayıt 409, validation/geçersiz JSON 400 döner.
Kod büyük/küçük harfe duyarlıdır; harf (A–Z, a–z), rakam, tire ve alt çizgi içerebilir.
Kod ve ad en fazla 255 karakterdir. Kod formatı, URL içinde erişilemeyen platform oluşmasını önler.
Pil ve bağlantı kalitesi 0–100 arasında tam sayı, sıcaklık sayısal ve zorunludur.

```json
{"platformCode":"UAV-001","name":"Demo UAV","type":"UAV"}
```

```json
{"platformCode":"UAV-001","batteryLevel":80,"temperature":55,"linkQuality":90}
```

Validation hata örneği:

```json
{"status":400,"message":"Validation failed","errors":{"batteryLevel":"must be less than or equal to 100"}}
```

## Running Backend

Gereksinim: JDK 17. Maven kurulu olmak zorunda değil; projedeki Maven Wrapper kullanılabilir.

```bash
cd backend
./mvnw clean test
./mvnw spring-boot:run
```

Maven kuruluysa aynı klasörde:

```bash
mvn clean test
mvn spring-boot:run
```

Windows'ta `mvnw.cmd` kullanılır. İlk çalıştırma bağımlılık indirmek için internet gerektirir.

- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:prtmsdb`; kullanıcı: `sa`; şifre: boş

H2 bellekte çalışır, `create-drop` kullanılır. **Backend kapanınca bütün kayıtlar silinir.**
Zaman damgası backend yerel saatiyle `LocalDateTime.now()` kullanır; saat dilimi gönderilmez.
Frontend/backend aynı yerel geliştirme ortamı varsayılır. H2 mikrosaniye hassasiyetinde saklar.

## Running Frontend

Gereksinimler: Node.js 22.12+ (22.x) veya desteklenen daha yeni sürüm ve npm.
Başka bir terminalde:

```bash
cd frontend
npm ci
npm run dev
```

Frontend: http://localhost:5173

Backend CORS yapılandırması `http://localhost:5173` ve `http://127.0.0.1:5173` adreslerine izin verir.
Vite portu doluysa başka porta geçmek yerine hata verir; böylece CORS adresi değişmez.
API adresi `src/services/api.js` içinde `http://localhost:8080/api` olarak tanımlıdır.

```bash
npm run build
```

Üretim çıktısı `frontend/dist/` altında oluşur. Demo için `npm run dev` kullanılır.
Sunucuları durdurmak için ilgili terminalde Ctrl+C.

## Demo Scenario

1. UI'da `UAV-001`, `Demo UAV`, `UAV` oluştur. Başlangıçta UNKNOWN gör.
2. View seç. Geçmiş boş ve Latest Telemetry “No telemetry yet” olmalı.
3. Battery 80, Temperature 55, Link 90 gönder: **READY**.
4. Battery 30, Temperature 60, Link 80 gönder: **DEGRADED**.
5. Battery 15, Temperature 90, Link 20 gönder: **NOT_READY**.
6. Kartların, platform satırının ve readiness'in güncellendiğini; geçmişin en yeni kayıt başta
   NOT_READY, DEGRADED, READY gösterdiğini kontrol et.
7. Aynı `UAV-001` kodunu tekrar ekle: anlaşılır duplicate hata mesajını gör.

Ek platform örnekleri: `UGV-001` / Demo UGV / UGV ve `SENSOR-001` / Demo Sensor / SENSOR.
Otomatik veri eklenmez; UI'dan oluşturulur. Sonuçlar yalnızca son gönderilen veriye dayanır;
telemetri yaşlanması veya gerçek zamanlı izleme yoktur.

## Tests

- BatteryHealthRuleTest: 6 normal/sınır değeri
- TemperatureHealthRuleTest: 6 normal/sınır değeri
- LinkQualityHealthRuleTest: 6 normal/sınır değeri
- HealthAssessmentServiceTest: 8 kombinasyon ve öncelik senaryosu
- PlatformServiceTest: 3 oluşturma/duplicate/not-found senaryosu
- PlatformCodeValidationTest: 4 URL için geçersiz kod regresyon senaryosu

Toplam 33 test. Canlı kontrol sonuçları ve Definition of Done: [VERIFICATION.md](docs/VERIFICATION.md).

## What I Learned

- Spring Boot, REST API ve Controller–Service–Repository ayrımı
- Spring Data JPA ve H2
- DTO, Validation ve Exception Handling
- Dependency Injection ve constructor injection
- Interface, polymorphism ve basit Open/Closed Principle
- React, useState, useEffect ve Fetch API
- Frontend/backend integration
- Basic unit testing ve eşik değerlerinin testi

## Future Improvements

Yalnızca ilerisi için fikirler; bu sürümde uygulanmadı:

- PostgreSQL
- Spring Security
- JWT
- WebSocket
- Alert management
- Docker
- Metrics
- Pagination

## Gelişim Notları

Bu bölüm, mentörlerle paylaşılan ilerleme mesajları ve 22 Eylül'deki geliştirme
çalışmaları temel alınarak sonradan hazırlanmıştır. Tarihler çalışma sürecini
anlatır; Git commitleri 22 Eylül 2026'da oluşturulmuştur.

- **18 Eylül 2026 — Kapsam ve hazırlık:** Java ve OOP konuları tekrar edildi,
  Spring Boot konularına odaklanıldı. Sentetik telemetri üzerinden batarya,
  sıcaklık ve bağlantı kalitesiyle sistem sağlığı değerlendiren POC fikri
  mentörlerle paylaşıldı ve kapsam hakkında görüş istendi.
- **21 Eylül 2026 — Telemetri çalışmaları:** Mentörlere gönderilen ilerleme
  mesajında platform–telemetri ilişkisinin, REST API üzerinden telemetri alma
  ve H2'ye kaydetmenin, request DTO ve temel validation kontrollerinin
  tamamlandığı bildirildi. Platform bazlı telemetri sorgulama akışının çalıştığı,
  sonraki adımın sağlık kuralları olduğu belirtildi.
- **22 Eylül 2026 — POC'nin tamamlanması:** Sağlık kuralları ve durum
  değerlendirmesi, React dashboard ve backend bağlantısı tamamlandı.
  Testler, canlı API ve tarayıcı kontrolleri yapıldı; kurulum ve demo adımları
  yazılarak proje Git'e hazırlandı.

## Notes

Önceki platform başlangıcının README'si `docs/DAY1.md` içinde korunmuştur; bu dosya tarihsel nottur.
Güncel çalıştırma ve kapsam için bu kök README geçerlidir.
Springdoc sürüm ailesi için resmi kaynak: https://springdoc.org/v2/.
