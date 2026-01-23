@echo off
REM Script de test pour le service Prescription (Windows)
REM Usage: test.bat

set BASE_URL=http://localhost:8082/api/prescriptions

echo === Test du Service Prescription ===
echo.

REM Test 1: Prescription simple
echo Test 1: Prescription simple avec un medicament
curl -X POST %BASE_URL% ^
  -H "Content-Type: application/json" ^
  -d "{\"prescriptionId\": \"PRES-001\", \"drugs\": [{\"name\": \"Iohexol\", \"quantity\": 5}]}" ^
  -w "\n\nStatus Code: %%{http_code}\n\n"

echo ---
echo.

REM Test 2: Plusieurs medicaments
echo Test 2: Prescription avec plusieurs medicaments
curl -X POST %BASE_URL% ^
  -H "Content-Type: application/json" ^
  -d "{\"prescriptionId\": \"PRES-002\", \"drugs\": [{\"name\": \"Iohexol\", \"quantity\": 3}, {\"name\": \"Itraconazole\", \"quantity\": 2}]}" ^
  -w "\n\nStatus Code: %%{http_code}\n\n"

echo ---
echo.

REM Test 3: Donnees invalides
echo Test 3: Donnees invalides (prescriptionId vide)
curl -X POST %BASE_URL% ^
  -H "Content-Type: application/json" ^
  -d "{\"prescriptionId\": \"\", \"drugs\": [{\"name\": \"Iohexol\", \"quantity\": 5}]}" ^
  -w "\n\nStatus Code: %%{http_code}\n\n"

echo === Tests termines ===
pause
