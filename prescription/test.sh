#!/bin/bash

# Script de test pour le service Prescription
# Usage: ./test.sh

BASE_URL="http://localhost:8082/api/prescriptions"

echo "=== Test du Service Prescription ==="
echo ""

# Test 1: Prescription simple
echo "Test 1: Prescription simple avec un médicament"
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "PRES-001",
    "drugs": [
      {
        "name": "Iohexol",
        "quantity": 5
      }
    ]
  }' \
  -w "\n\nStatus Code: %{http_code}\n\n"

echo "---"
echo ""

# Test 2: Plusieurs médicaments
echo "Test 2: Prescription avec plusieurs médicaments"
curl -X POST $BASE_URL \
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
  }' \
  -w "\n\nStatus Code: %{http_code}\n\n"

echo "---"
echo ""

# Test 3: Données invalides (prescriptionId vide)
echo "Test 3: Données invalides (prescriptionId vide)"
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "",
    "drugs": [
      {
        "name": "Iohexol",
        "quantity": 5
      }
    ]
  }' \
  -w "\n\nStatus Code: %{http_code}\n\n"

echo "---"
echo ""

# Test 4: Quantité invalide
echo "Test 4: Quantité invalide (négative)"
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "prescriptionId": "PRES-003",
    "drugs": [
      {
        "name": "Iohexol",
        "quantity": -1
      }
    ]
  }' \
  -w "\n\nStatus Code: %{http_code}\n\n"

echo "=== Tests terminés ==="
