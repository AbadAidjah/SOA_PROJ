# Documentation — Services *Pharmacie* (SOAP) et *Validation* (REST)

Ce document décrit **2 services** de votre système :

- **pharmacie-service** (SOAP / Spring-WS) — gestion du stock et réservation
- **validation-service** (REST / Spring Boot) — liste des médicaments + validation d’interactions

---

## 1) Pharmacie Service (SOAP)

### 1.1 Informations générales

- **Type** : SOAP (Spring-WS, endpoint annoté `@Endpoint`)
- **Namespace XML** : `http://pharmacie.com/demo`
- **WSDL** : `http://localhost:8080/ws/PharmacyService.wsdl`

> Tous les messages SOAP utilisent l’enveloppe `http://schemas.xmlsoap.org/soap/envelope/` et les éléments métiers dans le namespace `http://pharmacie.com/demo`.

---

### 1.2 Opération SOAP — `getStock`

#### Objectif
Récupérer la **quantité disponible** d’un médicament identifié par `drugCode`.

#### Mapping côté endpoint
- `@PayloadRoot(namespace="http://pharmacie.com/demo", localPart="getStockRequest")`

#### Requête (exemple)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns="http://pharmacie.com/demo">
  <soapenv:Header/>
  <soapenv:Body>
    <getStockRequest>
      <drugCode>DDInter1000</drugCode>
    </getStockRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

#### Réponse (exemple)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns="http://pharmacie.com/demo">
  <soapenv:Header/>
  <soapenv:Body>
    <getStockResponse>
      <quantityAvailable>50</quantityAvailable>
    </getStockResponse>
  </soapenv:Body>
</soapenv:Envelope>
```

#### Logique métier (résumé)
1. Lire `drugCode` depuis la requête
2. Interroger le stock via `StockItemService.getQuantityAvailableByDrugCode(drugCode)`
3. Retourner `0` si null, sinon la valeur trouvée

---

### 1.3 Opération SOAP — `reserveMedicines`

#### Objectif
Créer une **réservation** de médicaments (plusieurs lignes) si le stock est suffisant.

#### Mapping côté endpoint
- `@PayloadRoot(namespace="http://pharmacie.com/demo", localPart="reserveMedicinesRequest")`

#### Requête (exemple)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns="http://pharmacie.com/demo">
  <soapenv:Header/>
  <soapenv:Body>
    <reserveMedicinesRequest>
      <items>
        <drugCode>DDInter1000</drugCode>
        <qtyReserved>3</qtyReserved>
      </items>
      <items>
        <drugCode>DDInter101</drugCode>
        <qtyReserved>2</qtyReserved>
      </items>
    </reserveMedicinesRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

#### Réponse — succès (exemple)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns="http://pharmacie.com/demo">
  <soapenv:Header/>
  <soapenv:Body>
    <reserveMedicinesResponse>
      <reservationId>0f6d3d6a-1c0a-4b7f-9d3d-9c7c8f5d5b2a</reservationId>
      <status>SUCCESS</status>
      <message>Reservation a ete creer</message>
    </reserveMedicinesResponse>
  </soapenv:Body>
</soapenv:Envelope>
```

#### Réponse — échec (exemple)
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns="http://pharmacie.com/demo">
  <soapenv:Header/>
  <soapenv:Body>
    <reserveMedicinesResponse>
      <reservationId></reservationId>
      <status>FAILED</status>
      <message>aucun quantiter sufisante de ce drug DDInter1000</message>
    </reserveMedicinesResponse>
  </soapenv:Body>
</soapenv:Envelope>
```

#### Logique métier actuelle (résumé)
1. Créer un `Reservation` avec `reservationId = UUID`
2. Sauvegarder la réservation (statut mis à jour ensuite)
3. Pour chaque `item` :
   - lire le stock disponible
   - si insuffisant ⇒ répondre `FAILED` immédiatement
   - sinon créer une `ReservationLine` (drugCode, qtyReserved) liée à la réservation
   - sauvegarder la ligne
   - **mettre à jour le stock**
4. Mettre le statut `ReservationStatus.RESERVED`, resauvegarder
5. Répondre `SUCCESS` avec `reservationId`

> Remarque importante : dans votre code, la mise à jour du stock utilise  
> `stockItemService.setQuantityAvailableByDrugCode(item.getDrugCode(), item.getQtyReserved());`  
> Ce comportement ressemble à un *SET = qtyReserved* (remplace la quantité par la quantité réservée).  
> En logique métier classique, on fait plutôt : **nouvelleQuantité = disponible - qtyReserved**.

---

### 1.4 Endpoints prévus mais commentés
Les opérations suivantes existent dans le code mais sont commentées :

- `cancelReservationRequest` → `cancelReservationResponse`
- `dispenseReservationRequest` → `dispenseReservationResponse`

Si vous les activez, documentez :
- l’effet attendu (annulation = remettre en stock, dispense = confirmer la délivrance)
- les statuts (`CANCELLED`, `DISPENSED`, etc.)

---

## 2) Validation Service (REST)

### 2.1 Informations générales

- **Type** : REST (Spring Boot `@RestController`)
- **Base path** : `/api`
- **Port (exemples)** : `http://localhost:8081`

---

### 2.2 Endpoint REST — Lister tous les médicaments (dropdown)

#### URL
`GET /api/drugs`

#### Exemple d’appel
```bash
curl -X GET http://localhost:8081/api/drugs
```

#### Réponse (exemple)
```json
[
  { "id": "DDInter1000", "name": "Iohexol" },
  { "id": "DDInter101", "name": "Itraconazole" }
]
```

#### Objectif
Permet au front-end / prescription-service d’afficher un **dropdown** (liste contrôlée) afin que le médecin ne tape pas des noms au hasard.

---

### 2.3 Endpoint REST — Valider les interactions

#### URL
`POST /api/validate`

#### Payload attendu
Le contrôleur reçoit une structure :
- une clé `"drugs"`
- une liste d’objets `{ name, quantity }`

Exemple :
```json
{
  "drugs": [
    { "name": "Iohexol", "quantity": 5 },
    { "name": "Itraconazole", "quantity": 2 }
  ]
}
```

#### Exemple d’appel
```bash
curl -X POST http://localhost:8081/api/validate   -H "Content-Type: application/json"   -d '{"drugs":[{"name": "Iohexol", "quantity": 5}, {"name": "Itraconazole", "quantity": 2}]}'
```

#### Réponse
Le contrôleur retourne un objet `ValidationResult` (votre modèle `ValidationService.ValidationResult`).

Structure typique attendue (à adapter à votre implémentation réelle) :
```json
{
  "ok": true,
  "issues": [],
  "mappedItems": [
    { "drugId": "DDInter1000", "quantity": 5 },
    { "drugId": "DDInter101", "quantity": 2 }
  ]
}
```

En cas de conflit :
```json
{
  "ok": false,
  "issues": [
    { "drugA": "DDInter1000", "drugB": "DDInter101", "level": "MAJOR" }
  ],
  "mappedItems": [
    { "drugId": "DDInter1000", "quantity": 5 },
    { "drugId": "DDInter101", "quantity": 2 }
  ]
}
```

#### Logique métier attendue (résumé)
1. Recevoir les **noms** (`name`) sélectionnés dans le dropdown
2. Mapper `name -> id` depuis la table `drug`
3. Générer toutes les paires d’IDs (N médicaments ⇒ N*(N-1)/2 paires)
4. Vérifier chaque paire dans `drug_interaction(drug_a, drug_b)`
5. Bloquer si le niveau est critique (ex. `MAJOR`, `CONTRAINDICATED`)
6. Retourner `ok`, `issues[]`, `mappedItems[]`

---

## 3) Intégration entre les deux services (flux recommandé)

1. Le client (UI) charge `GET /api/drugs` et affiche le dropdown.
2. Le médecin sélectionne des médicaments (par **nom**).
3. Le client envoie `POST /api/validate`.
4. Si `ok=false`, on affiche les `issues` et on bloque la soumission.
5. Si `ok=true`, la couche d’orchestration (souvent prescription-service) appelle le SOAP :
   - WSDL: `http://localhost:8080/ws/PharmacyService.wsdl`
   - opération: `reserveMedicinesRequest`
6. Pharmacie répond `SUCCESS` (réservation créée) ou `FAILED` (stock insuffisant).

---

## 4) Résumé des endpoints / opérations

### Pharmacie (SOAP)
- **getStockRequest** → **getStockResponse**
- **reserveMedicinesRequest** → **reserveMedicinesResponse**
- (prévu mais commenté) cancelReservation, dispenseReservation

### Validation (REST)
- `GET  /api/drugs`
- `POST /api/validate`

---
