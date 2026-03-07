# DealZ Android

DealZ ist eine Android-App für Produktsuche, Prospekt-Scan-Demo und Preisvergleich für Online-Shops und Supermärkte.

## Funktionen
- Produktsuche
- Demo-Preisvergleich
- Prospekt-Scan-Demo
- Offline-Demo-Modus ohne Backend
- vorbereitet für Backend-API

## APK mit GitHub bauen
1. Dieses Projekt in ein GitHub-Repository hochladen.
2. Auf **Actions** gehen.
3. **Build Android Debug APK** öffnen.
4. **Run workflow** drücken.
5. Nach dem erfolgreichen Lauf unter **Artifacts** die Datei **dealz-debug-apk** herunterladen.

## Backend-URL
Standardmäßig nutzt die App:
`http://10.0.2.2:8000/`

Für einen echten Online-Betrieb muss in `ApiClient.kt` die `BASE_URL` auf deinen Server geändert werden.
