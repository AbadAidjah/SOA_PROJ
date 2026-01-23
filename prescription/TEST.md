# Guide de Test - Service Prescription

## Prérequis

1. **Services démarrés** :
   - Validation Service sur le port 8081
   - Pharmacy Service sur le port 8080
   - Prescription Service sur le port 8082

2. **Démarrer le service** :
   ```bash
   cd prescription
   mvn spring-boot:run
   ```

   Ou avec Docker :
   ```bash
   docker-compose up prescription
   ```

---

## Test avec cURL

### 1. Test de prescription réussie

```bash
curl -X POST http://localhost:8082/api/prescriptions \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "PRES-001",
    "drugs": [
      {
        "name": "Iohexol",
        "quantity": 5
      }
    ]
  }'
```

**Réponse attendue (succès)** :
```json
{
  "status": "SUCCESS",
  "message": "Reservation a ete creer",
  "reservationId": "0f6d3d6a-1c0a-4b7f-9d3d-9c7c8f5d5b2a"
}
```

### 2. Test avec plusieurs médicaments

```bash
curl -X POST http://localhost:8082/api/prescriptions \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "PRES-002",
    "drugs": [
      {
        "name": "Iohexol",
        "quantity": 3
      },
      {
        "name": "Itraconazole",
        "quantity": 2
      }
    ]
  }'
```

### 3. Test d'erreur - Interaction médicamenteuse

Si deux médicaments ont une interaction, vous recevrez :

```json
{
  "code": "VALIDATION_FAILED",
  "message": "Interaction médicamenteuse détectée",
  "issues": [
    {
      "drugA": "DDInter1000",
      "drugB": "DDInter101",
      "level": "MAJOR"
    }
  ]
}
```

### 4. Test d'erreur - Données invalides

```bash
curl -X POST http://localhost:8082/api/prescriptions \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "",
    "drugs": []
  }'
```

**Réponse attendue** :
```json
{
  "code": "INVALID_REQUEST",
  "message": "L'ID de prescription est requis",
  "issues": null
}
```

### 5. Test d'erreur - Quantité invalide

```bash
curl -X POST http://localhost:8082/api/prescriptions \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "PRES-003",
    "drugs": [
      {
        "name": "Iohexol",
        "quantity": -1
      }
    ]
  }'
```

---

## Test avec Postman

### Configuration

1. **Méthode** : POST
2. **URL** : `http://localhost:8082/api/prescriptions`
3. **Headers** :
   - `Content-Type: application/json`
4. **Body** (raw JSON) :
   ```json
   {
     "prescriptionId": "PRES-001",
     "drugs": [
       {
         "name": "Iohexol",
         "quantity": 5
       }
     ]
   }
   ```

### Collections Postman

Vous pouvez créer une collection avec plusieurs requêtes :
- Test succès
- Test interaction médicamenteuse
- Test données invalides
- Test stock insuffisant

---

## Test avec HTTPie

```bash
http POST http://localhost:8082/api/prescriptions \
  prescriptionId=PRES-001 \
  drugs:='[{"name":"Iohexol","quantity":5}]'
```

---

## Vérification des logs

Pendant les tests, vérifiez les logs du service :

```
2026-01-23 10:00:00 - INFO  [http-nio-8082-exec-1] c.p.d.controller.PrescriptionController - Reçu une nouvelle demande de prescription ID: PRES-001
2026-01-23 10:00:00 - DEBUG [http-nio-8082-exec-1] c.p.d.service.PrescriptionService - Appel du service de validation pour prescription ID: PRES-001
2026-01-23 10:00:01 - INFO  [http-nio-8082-exec-1] c.p.d.service.PrescriptionService - Validation réussie pour prescription ID: PRES-001
2026-01-23 10:00:01 - DEBUG [http-nio-8082-exec-1] c.p.d.service.PrescriptionService - Appel du service pharmacie pour prescription ID: PRES-001
2026-01-23 10:00:02 - INFO  [http-nio-8082-exec-1] c.p.d.service.PrescriptionService - Prescription traitée avec succès. Reservation ID: xxx
```

---

## Codes de réponse HTTP

- **200 OK** : Prescription traitée avec succès
- **400 Bad Request** : Données invalides ou interactions détectées
- **503 Service Unavailable** : Service validation ou pharmacie indisponible
- **500 Internal Server Error** : Erreur interne

---

## Exemples de fichiers JSON

### test-success.json
```json
{
  "prescriptionId": "PRES-001",
  "drugs": [
    {
      "name": "Iohexol",
      "quantity": 5
    }
  ]
}
```

### test-multiple-drugs.json
```json
{
  "prescriptionId": "PRES-002",
  "drugs": [
    {
      "name": "Iohexol",
      "quantity": 3
    },
    {
      "name": "Itraconazole",
      "quantity": 2
    }
  ]
}
```

### test-invalid.json
```json
{
  "prescriptionId": "",
  "drugs": []
}
```

**Utilisation** :
```bash
curl -X POST http://localhost:8082/api/prescriptions \
  -H "Content-Type: application/json" \
  -d @test-success.json
```

---

## Test de santé du service

Vérifier que le service répond :

```bash
curl http://localhost:8082/api/prescriptions
```

Si le service est démarré, vous devriez recevoir une erreur 405 (Method Not Allowed) car GET n'est pas supporté, mais cela confirme que le service fonctionne.

---

## Dépannage

### Service non accessible
- Vérifier que le port 8082 n'est pas utilisé : `netstat -an | findstr 8082`
- Vérifier les logs pour les erreurs de démarrage

### Erreur 503
- Vérifier que Validation Service (8081) est démarré
- Vérifier que Pharmacy Service (8080) est démarré
- Vérifier la connectivité réseau entre les services

### Erreur 400
- Vérifier le format JSON
- Vérifier que tous les champs requis sont présents
- Vérifier les quantités (doivent être > 0)
