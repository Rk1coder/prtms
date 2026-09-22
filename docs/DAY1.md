# PRTMS — DAY 1

Platform Readiness & Telemetry Monitoring System projesinin yalnızca platform yönetimi temeli.
Java 21, Spring Boot 3.5.16, Maven, Spring Web, Spring Data JPA, Bean Validation ve H2 kullanılır.

## Çalıştırma

Bu klasörde, Maven kuruluysa:

```bash
mvn spring-boot:run
```

Maven kurulu değilse sağlanan Maven Wrapper ile:

```bash
./mvnw spring-boot:run
```

Windows: `mvnw.cmd spring-boot:run`. İlk çalıştırma bağımlılıkları indirmek için internet gerektirir.
API adresi: http://localhost:8080/api/platforms
Durdurmak için Ctrl+C kullanılır.

## Mimari

Controller → Service → Repository → H2

- Controller HTTP isteklerini alır, DTO validation işlemini tetikler ve service çağırır.
- Service benzersiz platform kodunu kontrol eder, kayıt/sorgulama yapar ve entity'yi response DTO'ya dönüştürür.
- Repository, Spring Data JPA üzerinden veritabanıyla konuşur.
- H2 kayıtları bellekte saklar; uygulama kapanınca veriler silinir.

`com.prtms` paketindeki ana sınıf alt paketlerin Spring tarafından bulunmasını sağlar.

## Kavramlar

- **Spring Boot:** Spring uygulamasını otomatik yapılandırır ve gömülü sunucuyla başlatır.
- **REST Controller:** HTTP endpointlerini Java metotlarına bağlar ve JSON yanıt üretir.
- **Dependency Injection:** Spring, bağımlılıkları constructor üzerinden sağlar.
- **Service Layer:** İş kurallarını HTTP ve veri erişiminden ayırır.
- **Repository:** Veri erişim sözleşmesidir; JpaRepository temel CRUD metotlarını sağlar.
- **JPA:** Java nesneleri ile ilişkisel tablolar arasındaki eşlemeyi tanımlar; Hibernate uygular.
- **Entity:** Veritabanında saklanan nesnedir. Platform `platforms` tablosuna eşlenir.
- **DTO:** API'nin aldığı ve döndürdüğü verileri tanımlar. Record, Java'nın kısa ve değişmez veri taşıyıcısıdır.
- **Bean Validation:** @NotBlank ve @NotNull ile gelen veriyi doğrular; @Valid kontrolü başlatır.
- **Exception Handling:** @RestControllerAdvice istisnaları ortak JSON hata yanıtlarına dönüştürür.

## API testi

```bash
curl -i -X POST http://localhost:8080/api/platforms \
  -H 'Content-Type: application/json' \
  -d '{"platformCode":"UAV-001","name":"Demo UAV","type":"UAV"}'

curl -i http://localhost:8080/api/platforms

curl -i http://localhost:8080/api/platforms/UAV-001
```

Oluşturma 201, sorgulamalar 200 döner. Yeni platformun durumu UNKNOWN olur.
Aynı kodla tekrar oluşturma 409, bulunamayan kod 404, geçersiz istek 400 döner.
Örnek hata: `{"status":404,"message":"Platform not found: UAV-001"}`.
Platform kodları büyük/küçük harfe duyarlıdır; otomatik normalize edilmez.

## H2 Console

http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:prtmsdb`
- User: `sa`
- Password: boş

`create-drop` geliştirme ayarıdır; her başlangıçta şema yeniden oluşturulur.

## Testler

```bash
mvn test
# Maven kurulu değilse:
./mvnw test
```

PlatformServiceTest içindeki üç Mockito unit testi:

1. Platform oluşturma, alanların DTO'ya aktarılması ve UNKNOWN başlangıç durumu.
2. Yinelenen kodun DuplicatePlatformException ile reddedilmesi ve kayıt yapılmaması.
3. Bulunamayan kodun PlatformNotFoundException üretmesi.

Mockito yalnızca repository interface'ini taklit eder. Test kaynağındaki
`mock-maker-subclass` ayarı JVM'e dinamik agent bağlamadan bu testleri çalıştırır.
Doğrulama sonucu: 3 test, 0 failure, 0 error, 0 skipped; BUILD SUCCESS.
Yerel HTTP kontrolünde boş liste, oluşturma, dolu liste, kodla sorgu, duplicate,
bulunamayan platform, validation ve geçersiz enum olmak üzere 8 senaryo geçti.

## Tasarım kararları

1. Katmanlar tek sorumluluk taşır; controller repository'ye doğrudan erişmez.
2. Constructor injection bağımlılıkları görünür kılar ve Mockito ile test etmeyi kolaylaştırır.
3. Request/response DTO'ları entity'yi API sözleşmesinden ayırır; istemci id veya status atayamaz.
4. Kod benzersizliği service kontrolü ve veritabanındaki unique constraint ile korunur.
5. Enum değerleri STRING saklanır; enum sıralaması değişse de verinin anlamı korunur.
6. Tek somut service ve elle DTO dönüşümü, küçük POC için ek interface ve mapper ihtiyacını kaldırır.

Bu DAY 1 sürümü eşzamanlı aynı kodla oluşturma için özel hata eşleme içermez;
veritabanı benzersizliği korur, ancak böyle bir yarışta kaybeden istek 500 alabilir.

Telemetry, HealthRule, readiness hesaplama, Alert, Security, Swagger, Actuator,
Docker, PostgreSQL, WebSocket, Scheduler ve Simulator bu sürümün kapsamında değildir.
