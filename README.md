# Information about the project

## Installatie Backend

Bij het openen van het ASPCoreApi project moet eerst een deploy worden uitgevoerd op Azure. Ga hiervoor naar de solution explorer
en klik met de rechtermuisknop op het ASPCoreApi project en selecteer Publish. je zal eerst een nieuw profiel moeten aanmaken. Druk dus eerst op New Profile.

Kies een gepaste Api naam, selecteer de juist subscription. maak eventueel een nieuwe recoursegroup aan indien dit nog niet het geval is.
Zelfde geldt voor een hostingplan. Voor studenten kies je best de F1 subscriptie.

vervolgens zal de Database moeten worden aangemaakt. klik op create database, zorg voor een gepaste naam. indien je nog geen server hebt om de database op te hosten klik je op new bij het server vak. je kiest ook hiervoor een gepaste naam en een admin username en password.

als dit gebeurd is kan je op create klikken.
### azure password ophalen

om het server password te krijgen ga je naar je azure dashboard en ga je naar de resources. selecteer de App service en druk op publicatie profiel ophalen. open dit bestand met een text editor en haal het password eruit. Je kan hier ook de servernaam en alle andere benodigde info uit halen.

### ASP deploy configure

In de publish page klik je nu op configure en zal je de nodige gegevens moeten invullen. ook het password dat je uit het publicatie profiel hebt gehaald.

Hierna klik je op Settings en duid klik je op databases dropdown. je vinkt hier "use this connectionstring at runtime". 
je klikt nu op Entity framework migrations, en duid "apply this migration on publish" aan. vervolgens klik je op save.

als dit allemaal in orde is kan je op publish drukken. Je api is nu gelinkt met de database en kan gebruikt worden door de applicatie.

## Sights inladen Swagger

omdat we de Sights hardcoded in de Database toevoegen. zullen deze eerst door de admin moeten worden geinitialiseerd.
Ga hiervoor naar het destination URL, dit kan je vinden bij de configure dialoog uit vorig deel en plaatst hier in de browser /swagger achter.
je ziet hier alle endpoints.
selecteer het post Sights/postarray en kopieer de Sights JSON die je vindt in de map data in de github repo. Plak deze in het value veld en druk op execute. De sights zijn nu toegevoegd.

## Streets inladen

